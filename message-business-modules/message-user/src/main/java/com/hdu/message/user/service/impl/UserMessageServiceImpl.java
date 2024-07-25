package com.hdu.message.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.hdu.message.common.base.constant.CommonConstant;
import com.hdu.message.common.base.entity.ManagerNotice;
import com.hdu.message.common.base.entity.Section;
import com.hdu.message.common.base.enums.error.ErrorEnum;
import com.hdu.message.common.base.exception.UserMessageException;
import com.hdu.message.redis.utils.RedisUtil;
import com.hdu.message.common.base.entity.User;
import com.hdu.message.common.base.entity.UserNotice;
import com.hdu.message.user.bean.UserSection;
import com.hdu.message.user.dto.UserNoticeDto;
import com.hdu.message.user.mapper.UserMapper;
import com.hdu.message.user.mapper.UserMessageMapper;
import com.hdu.message.user.service.UserMessageService;
import com.hdu.message.user.service.UserSectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

@Transactional(rollbackFor = Exception.class)
@Service
public class UserMessageServiceImpl implements UserMessageService {

    @Autowired
    private UserMessageMapper userMessageMapper;

    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private UserSectionService userSectionService;

    // 阻塞队列
    private static final BlockingQueue<Runnable> WORK_QUEUE = new ArrayBlockingQueue<>(20);

    // 拒绝策略
    private static final RejectedExecutionHandler HANDLER = new ThreadPoolExecutor.CallerRunsPolicy();

    ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 15, 1000, TimeUnit.MILLISECONDS, WORK_QUEUE, HANDLER);

    @Override
    public List<UserNotice> getUserMessage(UserNotice userNotice, Integer page, Integer size, String sort, String order) {
        PageHelper.startPage(page, size);
        return userMessageMapper.queryAllByLimit(userNotice, sort, order);
    }

    @Override
    public Integer pullManagerNotice(User user) throws ExecutionException, InterruptedException {
        // 拉取用户上次登录时间戳到now时间戳之间的数据
        long nowTimestamp = Instant.now().toEpochMilli();
        long lastLoginTimestamp = user.getLastLoginTime().toInstant(ZoneOffset.of("+8")).toEpochMilli();
        // 拉取全局热点消息
        // 拉取栏目热点消息
        // 所有信息需要根据栏目key for循环拉取多线程
        List<FutureTask<List<UserNotice>>> futureTaskList = new ArrayList<>();
        // 首先判断是否存在key，不存在查询数据库，存在查询redis
        if (RedisUtil.KeyOps.hasKey(user.getUserId() + "-" + CommonConstant.SECTION_KEY)) {
            // 从redis中获取user-section栏目信息
            List<String> sectionStringList = RedisUtil.ListOps.lRange(user.getUserId() + "-" + CommonConstant.SECTION_KEY, 0, Integer.MAX_VALUE);
            // 遍历栏目
            for (String sectionString : sectionStringList) {
                Section section = JSON.parseObject(sectionString, Section.class);
                // 0代表全局板块，大于0代表栏目版块
                multiTask(user, nowTimestamp, lastLoginTimestamp, futureTaskList, section.getSectionEnum());
            }
        } else {
            // Redis数据不存在，查询数据库并写入缓存
            List<UserSection> userSectionList = userSectionService.getUserSection(user);
            for (UserSection userSection : userSectionList) {
                multiTask(user, nowTimestamp, lastLoginTimestamp, futureTaskList, userSection.getSectionEnum());
            }
        }
        List<UserNotice> batchList = new ArrayList<>();
        for (FutureTask<List<UserNotice>> futureTask : futureTaskList) {
            batchList.addAll(futureTask.get());
        }
        // 插入统一拉取信息
        Integer ans = batchList.size();
        if (!batchList.isEmpty()) {
            userMessageMapper.insertBatch(batchList);
        }
        // 刷新用户登录时间戳
        user.setLastLoginTime(LocalDateTime.now());
        // 刷新用户登录时间
        userMapper.update(user);
        return ans;
    }

    // 处理任务
    private void multiTask(User user, long nowTimestamp, long lastLoginTimestamp, List<FutureTask<List<UserNotice>>> futureTaskList, String sectionEnum) {
        Callable<List<UserNotice>> sectionCallable = () -> pullMessage(sectionEnum, nowTimestamp, lastLoginTimestamp, user);
        FutureTask<List<UserNotice>> sectionFutureTask = new FutureTask<>(sectionCallable);
        futureTaskList.add(sectionFutureTask);
        threadPoolExecutor.submit(sectionFutureTask);
    }

    /**
     * 将拉取任务封装成方法，不同栏目使用多线程调用
     * @param key redis键
     * @param nowTimestamp 当前时间戳
     * @param lastLoginTimestamp 上次登陆时间戳
     * @return 是否拉取数据
     */
    public List<UserNotice> pullMessage(String key, long nowTimestamp, long lastLoginTimestamp, User user) {
        // 全局消息使用拉模式
        // 拉取用户上次登录时间戳到now时间戳之间的数据
        // 从redis中拉取全局消息
        Set<String> managerNoticeSet = RedisUtil.ZSetOps.zRangeByScore(key, lastLoginTimestamp, nowTimestamp);
        // 遍历该通知内容插入数据库存储
        List<UserNotice> userNoticeList = new ArrayList<>();
        for (String managerNoticeString : managerNoticeSet) {
            ManagerNotice managerNotice = JSON.parseObject(managerNoticeString, ManagerNotice.class);
            // 将数据分装为对应的UserNotice消息中
            UserNotice userNotice = fillUserNotice(managerNotice);
            userNotice.setRecipientId(user.getUserId());
            // 使用分片键自动插入
            // 分片键算法使用snowflake雪花算法，暂时该workId定义为1
            userNotice.setState(0);
            userNoticeList.add(userNotice);
        }
        // 如果当前消息板块中的消息为空，说明没有消息拉取，return 0
        if (managerNoticeSet.isEmpty()) {
            return userNoticeList;
        }
        // 批量插入用户消息数据
        return userNoticeList;
    }

    /**
     * 用户点对点推送消息
     * @param userNoticeDto 用户消息封装DTO
     * @return 推送消息数量
     */
    public Integer pushUserMessage(UserNoticeDto userNoticeDto) {
        // 抛出参数异常错误
        if (userNoticeDto == null || userNoticeDto.getAimUserIdList().isEmpty() || userNoticeDto.getSendUserId() == null) {
            throw new UserMessageException(ErrorEnum.A0400);
        }
        // 设置用户消息列表批量插入
        List<UserNotice> userNoticeList = new ArrayList<>();
        // 直接点到点插入mysql
        // 获取每个目标用户的接收方信息
        for (Long aimUserId : userNoticeDto.getAimUserIdList()) {
            // 设置消息接收方消息ID
            userNoticeDto.getUserNotice().setRecipientId(aimUserId);
            UserNotice userNotice = userNoticeDto.createNewUserNotice(userNoticeDto.getUserNotice());
            userNoticeList.add(userNotice);
        }
        // 进行批量插入点对点用户信息
        return userMessageMapper.insertBatch(userNoticeList);
    }

    @Override
    public Integer updateUserMessage(UserNotice userNotice) {
        return userMessageMapper.update(userNotice);
    }

    public UserNotice fillUserNotice(ManagerNotice managerNotice) {
        // 设置用户通知信息
        UserNotice userNotice = new UserNotice();
        userNotice.setTitle(managerNotice.getTitle());
        userNotice.setContent(managerNotice.getContent());
        userNotice.setState(managerNotice.getState());
        userNotice.setType(1);
        // 要根据拉取板块填写对应的SourceId
        // 如果是中央板块，应该为来源文章ID 如果为用户，则为发送方用户ID
        userNotice.setSectionId(managerNotice.getSectionId());
        userNotice.setSourceId(managerNotice.getManagerId());
        userNotice.setSendId(managerNotice.getSystemNoticeId());
        userNotice.setRecipientId(managerNotice.getRecipientId());
        userNotice.setPullTime(LocalDateTime.now());
        return userNotice;
    }

}
