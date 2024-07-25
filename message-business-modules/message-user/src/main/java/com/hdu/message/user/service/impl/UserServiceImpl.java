package com.hdu.message.user.service.impl;

import com.hdu.message.common.base.dto.UserSectionDto;
import com.hdu.message.common.base.entity.Section;
import com.hdu.message.common.base.entity.User;
import com.hdu.message.common.base.enums.error.ErrorEnum;
import com.hdu.message.common.base.enums.redismessage.RedisMessageKeyEnum;
import com.hdu.message.common.base.exception.SectionException;
import com.hdu.message.user.mapper.UserMapper;
import com.hdu.message.user.service.UserSectionService;
import com.hdu.message.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional(rollbackFor = Exception.class)
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserSectionService userSectionService;

    @Override
    public Integer addNewUser(User user) {
        Integer insertNum = userMapper.insert(user);
        // 默认给用户添加全局接受信息
        UserSectionDto userSectionDto = new UserSectionDto();
        userSectionDto.setUserId(user.getUserId());
        List<Section> sectionList = new ArrayList<>();
        // 全局默认权限
        Section section = new Section();
        section.setSectionEnum(RedisMessageKeyEnum.M1.getMessageKeyType());
        section.setSectionId(RedisMessageKeyEnum.M1.getSectionId());
        sectionList.add(section);
        userSectionDto.setSectionList(sectionList);
        if (userSectionService.subSection(userSectionDto) < 1) {
            throw new SectionException(ErrorEnum.A0400);
        }
        // 默认订阅全局板块
        return insertNum;
    }
}
