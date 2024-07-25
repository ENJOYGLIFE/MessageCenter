package com.hdu.message.manager.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.hdu.message.common.base.constant.CommonConstant;
import com.hdu.message.common.base.dto.UserNoticeDto;
import com.hdu.message.common.base.entity.Section;
import com.hdu.message.common.base.entity.UserNotice;
import com.hdu.message.common.base.enums.error.ErrorEnum;
import com.hdu.message.common.base.exception.SectionException;
import com.hdu.message.manager.bean.ManagerNotice;
import com.hdu.message.manager.dto.ManagerNoticeDto;
import com.hdu.message.manager.enums.managermessagetype.ManagerMessageTypeEnum;
import com.hdu.message.manager.mapper.MessageMapper;
import com.hdu.message.manager.service.CacheService;
import com.hdu.message.manager.service.MessageService;
import com.hdu.message.redis.utils.RedisUtil;
import com.hdu.message.user.api.UserMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional(rollbackFor = Exception.class)
@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private UserMessageService userMessageService;

    @Autowired
    private CacheService cacheService;

    @Override
    public Integer addManagerMessage(ManagerNoticeDto managerNoticeDto) {
        Integer insertNum = messageMapper.insert(managerNoticeDto.getManagerNotice());
        System.out.println(managerNoticeDto.getManagerNotice().getSystemNoticeId());
        handleDiffType(managerNoticeDto);
        // 将数据持久化到管理员数据库中
        return insertNum;
    }

    @Override
    public List<ManagerNotice> getManagerMessage(ManagerNotice managerNotice, Integer page, Integer size, String sort, String order) {
        PageHelper.startPage(page, size);
        return messageMapper.queryAllByLimit(managerNotice, sort, order);
    }

    /**
     * 中央管理员发布消息对应的处理方式
     * @param managerNoticeDto 中央消息DTO
     */
    public void handleDiffType(ManagerNoticeDto managerNoticeDto) {
        // 根据Redis栏目信息分装为对应的map
        Map<Integer, String> redisMessageKeyMap = getRedisMessageKey();
        ManagerNotice managerNotice = managerNoticeDto.getManagerNotice();
        // 该种情况当作redis热点数据存入redis，先写入mysql持久化
        String sectionEnum = redisMessageKeyMap.get(managerNotice.getSectionId());
        if (managerNotice.getSectionId() != -1 && sectionEnum == null) {
            throw new SectionException(ErrorEnum.A0500);
        }
        // 1.全局消息
        // 拉模式实现消息推送
        if (managerNotice.getType() == ManagerMessageTypeEnum.T1.getTypeNum()) {
            // 将数据序列化为JSON字符串并存储起来推送到key为ManagerAllMessage的key中
            String jsonString = JSON.toJSONString(managerNotice);
            // 获取当前时间戳
            long timestamp = Instant.now().toEpochMilli();
            System.out.println(timestamp);
            // 通知类型 字符串对象 时间戳
            // 全局键固定可用枚举类
            RedisUtil.ZSetOps.zAdd(sectionEnum, jsonString, timestamp);
        }
        // 2.订阅板块内容属于热点数据，存储redis
        // 拉模式实现消息推送
        if (managerNotice.getType() == ManagerMessageTypeEnum.T2.getTypeNum()) {
            // 根据
            String jsonString = JSON.toJSONString(managerNotice);
            // 获取当前时间戳
            long timestamp = Instant.now().toEpochMilli();
            // 推送到消息不同板块
            RedisUtil.ZSetOps.zAdd(sectionEnum, jsonString, timestamp);
        }
        // 3.点到点用户直接插入用户消息数据库中
        // 推模式实现消息推送
        if (managerNotice.getType() == ManagerMessageTypeEnum.T3.getTypeNum()) {
            // 插入用户数据库考虑分库分表，根据用户id插入对应hash算法对应数据库和表中
            // 调用UserMessage板块feign接口
            UserNotice userNotice = fillUserNotice(managerNotice);
            // 设置用户发送封装消息体
            UserNoticeDto userNoticeDto = new UserNoticeDto();
            userNoticeDto.setUserNotice(userNotice);
            userNoticeDto.setSendUserId(managerNotice.getManagerId());
            userNoticeDto.setAimUserIdList(managerNoticeDto.getUserList());
            userMessageService.pushUserMessage(userNoticeDto);
        }
    }

    /**
     * 获取Redis存储的key value 为 sectionId sectionEnum
     * @return 以map形式存储
     */
    public Map<Integer, String> getRedisMessageKey() {
        Map<Integer, String> res = new HashMap<>();
        // 存在Key则从Redis中取数据
        if (RedisUtil.KeyOps.hasKey(CommonConstant.SECTION_KEY)) {
            List<String> sectionStringList = RedisUtil.ListOps.lRange(CommonConstant.SECTION_KEY, 0, Integer.MAX_VALUE);
            // 封装为Map
            for (String sectionString : sectionStringList) {
                Section section = JSON.parseObject(sectionString, Section.class);
                res.put(section.getSectionId(), section.getSectionEnum());
            }
        } else {
            // 此处先完成Redis缓存再返回数据
            List<Section> sections = cacheService.initSectionCache();
            for (Section section : sections) {
                res.put(section.getSectionId(), section.getSectionEnum());
            }
        }
        return res;
    }

    public UserNotice fillUserNotice(ManagerNotice managerNotice) {
        // 设置用户通知信息
        UserNotice userNotice = new UserNotice();
        userNotice.setTitle(managerNotice.getTitle());
        userNotice.setContent(managerNotice.getContent());
        userNotice.setState(managerNotice.getState());
        // 1代表管理员拉取
        userNotice.setType(managerNotice.getType());
        // 要根据拉取板块填写对应的SourceId
        userNotice.setSourceId(managerNotice.getManagerId());
        userNotice.setSectionId(managerNotice.getSectionId());
        userNotice.setSendId(managerNotice.getSystemNoticeId());
        userNotice.setRecipientId(managerNotice.getRecipientId());
        userNotice.setPullTime(LocalDateTime.now());
        return userNotice;
    }

}
