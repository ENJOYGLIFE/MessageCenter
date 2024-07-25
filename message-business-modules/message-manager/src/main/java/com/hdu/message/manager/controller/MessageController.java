package com.hdu.message.manager.controller;

import com.hdu.message.common.base.enums.error.ErrorEnum;
import com.hdu.message.common.base.exception.ManagerMessageException;
import com.hdu.message.common.base.utils.ResponseUtil;
import com.hdu.message.manager.bean.ManagerNotice;
import com.hdu.message.manager.dto.ManagerNoticeDto;
import com.hdu.message.manager.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("message")
public class MessageController {

    @Autowired
    private MessageService messageService;

    /**
     * 新增管理员消息板块
     * @param managerNoticeDto 管理员消息板块
     * @return 插入成功
     */
    @PostMapping("addManagerMessage")
    public Object addManagerMessage(@RequestBody ManagerNoticeDto managerNoticeDto) {
        // 校验数据
        validateManagerMessage(managerNoticeDto.getManagerNotice());
        // 插入通知消息
        messageService.addManagerMessage(managerNoticeDto);
        return ResponseUtil.ok("插入成功");
    }

    /**
     * 管理员端查看消息列表
     * @param managerNotice 消息体
     * @param page 页
     * @param size 每页大小
     * @param sort 排序字段
     * @param order 排序顺序
     * @return 消息体列表
     */
    @GetMapping("getManagerMessage")
    public Object getManagerMessage(ManagerNotice managerNotice,
                                    @RequestParam(defaultValue = "1") Integer page,
                                    @RequestParam(defaultValue = "10") Integer size,
                                    @RequestParam(defaultValue = "create_time") String sort,
                                    @RequestParam(defaultValue = "desc") String order) {
        List<ManagerNotice> managerMessage = messageService.getManagerMessage(managerNotice, page, size, sort, order);
        return ResponseUtil.okList(managerMessage);
    }

    public Boolean validateManagerMessage(ManagerNotice managerNotice) {
        if (managerNotice.getTitle() == null || managerNotice.getType() == null || managerNotice.getManagerId() == null) {
            throw new ManagerMessageException(ErrorEnum.A0400);
        }
        return true;
    }

}
