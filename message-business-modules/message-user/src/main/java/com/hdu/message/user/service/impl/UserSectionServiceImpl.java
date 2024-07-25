package com.hdu.message.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hdu.message.common.base.constant.CommonConstant;
import com.hdu.message.common.base.dto.UserSectionDto;
import com.hdu.message.common.base.entity.Section;
import com.hdu.message.common.base.entity.User;
import com.hdu.message.common.base.enums.error.ErrorEnum;
import com.hdu.message.common.base.exception.SectionException;
import com.hdu.message.manager.api.CacheService;
import com.hdu.message.redis.utils.RedisUtil;
import com.hdu.message.user.bean.UserSection;
import com.hdu.message.user.mapper.UserSectionMapper;
import com.hdu.message.user.service.UserSectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional(rollbackFor = Exception.class)
@Service
public class UserSectionServiceImpl implements UserSectionService {

    @Autowired
    private UserSectionMapper userSectionMapper;

    @Autowired
    private CacheService cacheService;

    @Override
    public Integer subSection(UserSectionDto userSectionDto) {
        // 先更新数据库
        List<UserSection> userSectionList = new ArrayList<>();
        for (Section section : userSectionDto.getSectionList()) {
            UserSection userSection = new UserSection();
            userSection.setUserId(userSectionDto.getUserId());
            userSection.setSectionEnum(section.getSectionEnum());
            userSection.setSectionId(section.getSectionId());
            userSectionList.add(userSection);
        }
        Integer insertNum = userSectionMapper.insertBatch(userSectionList);
        // 清除Redis该用户栏目信息
        RedisUtil.KeyOps.delete(userSectionDto.getUserId() + "-" + CommonConstant.SECTION_KEY);
        return insertNum;
    }

    @Override
    public List<Section> getAllSection() {
        // 定义section的列表返回参数
        List<Section> sectionList = new ArrayList<>();
        //  先查询Redis
        if (RedisUtil.KeyOps.hasKey(CommonConstant.SECTION_KEY)) {
            List<String> strings = RedisUtil.ListOps.lRange(CommonConstant.SECTION_KEY, 0, Integer.MAX_VALUE);
            for (String string : strings) {
                Section section = JSON.parseObject(string, Section.class);
                sectionList.add(section);
            }
        } else {
            sectionList = cacheService.cacheSectionFeign();
        }
        return sectionList;
    }

    @Override
    public List<UserSection> getUserSection(User user) {
        List<UserSection> userSections = new ArrayList<>();
        if (RedisUtil.KeyOps.hasKey(user.getUserId() + "-" + CommonConstant.SECTION_KEY)) {
            // 如果缓存数据存在
            List<String> strings = RedisUtil.ListOps.lRange(user.getUserId() + "-" + CommonConstant.SECTION_KEY, 0, Integer.MAX_VALUE);
            for (String s : strings) {
                UserSection userSection = JSON.parseObject(s, UserSection.class);
                userSections.add(userSection);
            }
        } else {
            // 如果缓存中无数据先查数据库
            UserSection userSection = new UserSection();
            userSection.setUserId(user.getUserId());
            // 查询数据库数据
            userSections = userSectionMapper.queryAllByLimit(userSection);
            if (userSections.isEmpty()) {
                throw new SectionException(ErrorEnum.A0501);
            }
            List<String> list = new ArrayList<>();
            // 把值放入Redis保存
            for (UserSection us : userSections) {
                String userSectionString = JSONObject.toJSONString(us);
                list.add(userSectionString);
            }
            RedisUtil.ListOps.lLeftPushAll(user.getUserId() + "-" + CommonConstant.SECTION_KEY, list);
        }
        return userSections;
    }
}
