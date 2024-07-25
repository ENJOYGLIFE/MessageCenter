package com.hdu.message.user.controller;

import com.hdu.message.common.base.utils.ResponseUtil;
import com.hdu.message.common.base.entity.User;
import com.hdu.message.common.base.entity.UserNotice;
import com.hdu.message.user.dto.UserNoticeDto;
import com.hdu.message.user.service.UserMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("message")
public class UserMessageController {

    @Autowired
    private UserMessageService userMessageService;

    /**
     * 获取用户消息信息
     * @param userNotice 用户信息
     * @return 用户消息列表
     */
    @GetMapping("/getUserMessage")
    public Object getUserMessage(UserNotice userNotice,
                                 @RequestParam(defaultValue = "1") Integer page,
                                 @RequestParam(defaultValue = "10") Integer size,
                                 @RequestParam(defaultValue = "create_time") String sort,
                                 @RequestParam(defaultValue = "desc") String order) {
        List<UserNotice> userMessage = userMessageService.getUserMessage(userNotice, page, size, sort, order);
        return ResponseUtil.okList(userMessage);
    }

    /**
     * 拉取中央栏目消息
     * @param user 用户信息
     * @return 拉取管理员推送的数据
     */
    @PostMapping("pullManagerNotice")
    public Object pullManagerNotice(@RequestBody User user) throws ExecutionException, InterruptedException {
        // 异常抛给全局异常处理
        Integer noticeNum = userMessageService.pullManagerNotice(user);
        return ResponseUtil.ok(noticeNum);
    }

    /**
     * 推送接收方，发送方消息
     * @param userNoticeDto 用户发送DTO
     * @return 推送条数
     */
    @PostMapping("pushUserMessage")
    public Object pushUserMessage(@RequestBody UserNoticeDto userNoticeDto) {
        // 推送发送方，接收方消息
        Integer userMessageNoticeNum = userMessageService.pushUserMessage(userNoticeDto);
        return ResponseUtil.ok(userMessageNoticeNum);
    }

    /**
     * 更新消息信息（已读未读）
     * @param userNotice 消息信息
     * @return 是否更新成功
     */
    @PostMapping("updateUserMessage")
    public Object updateUserMessage(@RequestBody UserNotice userNotice) {
        Integer updateNum = userMessageService.updateUserMessage(userNotice);
        return ResponseUtil.ok(updateNum);
    }
}
