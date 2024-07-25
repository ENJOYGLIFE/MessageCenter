package com.hdu.message.user.service;

import com.hdu.message.common.base.entity.User;
import com.hdu.message.common.base.entity.UserNotice;
import com.hdu.message.user.dto.UserNoticeDto;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface UserMessageService {

    List<UserNotice> getUserMessage(UserNotice userNotice, Integer page, Integer size, String sort, String order);

    // 拉取缓存通知消息
    Integer pullManagerNotice(User user) throws ExecutionException, InterruptedException;

    // 推送发送到接收方消息
    Integer pushUserMessage(UserNoticeDto userNoticeDto);

    // 更新用户消息（已读未读）
    Integer updateUserMessage(UserNotice userNotice);

}
