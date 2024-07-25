package com.hdu.message.user.api;

import com.hdu.message.common.base.config.FeignConfig;
import com.hdu.message.common.base.constant.ServiceNameConstant;
import com.hdu.message.common.base.dto.UserNoticeDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = ServiceNameConstant.MESSAGE_USER, path = "message", configuration = FeignConfig.class)
public interface UserMessageService {

    /**
     * 推送点到点用户消息
     * @param userNoticeDto 传递UserNotice 用户消息
     * @return 消息信息 成功插入对方消息中心返回条数
     */
    @PostMapping("pushUserMessage")
    Object pushUserMessage(@RequestBody UserNoticeDto userNoticeDto);

}
