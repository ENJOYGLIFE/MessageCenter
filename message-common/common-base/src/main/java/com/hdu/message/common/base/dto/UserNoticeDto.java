package com.hdu.message.common.base.dto;

import com.hdu.message.common.base.entity.UserNotice;
import lombok.Data;

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
        newUserNotice.setId(userNotice.getId());
        newUserNotice.setUserNoticeId(userNotice.getUserNoticeId());
        newUserNotice.setTitle(userNotice.getTitle());
        newUserNotice.setContent(userNotice.getContent());
        newUserNotice.setState(userNotice.getState());
        newUserNotice.setType(userNotice.getType());
        newUserNotice.setSourceId(userNotice.getSourceId());
        newUserNotice.setRecipientId(userNotice.getRecipientId());
        // 设置推模式的消息时间当前为当前时间
        newUserNotice.setPullTime(LocalDateTime.now());
        return newUserNotice;
    }

}
