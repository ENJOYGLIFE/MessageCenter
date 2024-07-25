package com.hdu.message.user.mapper;

import com.hdu.message.common.base.entity.UserNotice;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMessageMapper {

    // 插入用户消息
    Integer insert(UserNotice userNotice);

    // 批量插入用户消息
    Integer insertBatch(List<UserNotice> userNoticeList);

    // 根据ID查询用户消息主体
    UserNotice queryById(String id);

    // 根据条件查询用户消息
    List<UserNotice> queryAllByLimit(@Param("userNotice") UserNotice userNotice, @Param("sort") String sort, @Param("order") String order);

    // 更新用户消息信息
    Integer update(UserNotice userNotice);

}
