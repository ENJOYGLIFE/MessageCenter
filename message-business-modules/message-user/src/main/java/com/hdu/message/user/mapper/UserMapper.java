package com.hdu.message.user.mapper;

import com.hdu.message.common.base.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {

    User queryById(User user);

    List<User> queryAllByLimit(User user);

    Integer insert(User user);

    Integer insertBatch(List<User> users);

    Integer update(User user);
}
