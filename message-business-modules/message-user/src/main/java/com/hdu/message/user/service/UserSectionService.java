package com.hdu.message.user.service;

import com.hdu.message.common.base.dto.UserSectionDto;
import com.hdu.message.common.base.entity.Section;
import com.hdu.message.common.base.entity.User;
import com.hdu.message.user.bean.UserSection;

import java.util.List;

public interface UserSectionService {

    Integer subSection(UserSectionDto userSectionDto);

    List<Section> getAllSection();

    List<UserSection> getUserSection(User user);
}
