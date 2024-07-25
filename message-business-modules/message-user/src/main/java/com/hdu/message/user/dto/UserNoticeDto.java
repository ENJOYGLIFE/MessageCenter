package com.hdu.message.user.dto;

import com.hdu.message.common.base.entity.UserNotice;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 封装的消息DTO 用于新增点到点消息通知
 */
@Data
public class UserNoticeDto {

    /**
     * 用户消息
     */
    private UserNotice userNotice;

    /**
     * 接收方用户ID列表
     */
    private List<Long> aimUserIdList;

    /**
     * 发送方用户ID
     */
    private Long sendUserId;

    public UserNotice createNewUserNotice(UserNotice userNotice) {
        UserNotice newUserNotice = new UserNotice();
        BeanUtils.copyProperties(userNotice, newUserNotice);
        return newUserNotice;
    }

}
