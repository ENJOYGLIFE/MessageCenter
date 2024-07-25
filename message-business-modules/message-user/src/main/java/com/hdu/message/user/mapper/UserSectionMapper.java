package com.hdu.message.user.mapper;

import com.hdu.message.user.bean.UserSection;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserSectionMapper {

    Integer insertBatch(List<UserSection> userSectionList);

    List<UserSection> queryAllByLimit(UserSection userSection);
}
