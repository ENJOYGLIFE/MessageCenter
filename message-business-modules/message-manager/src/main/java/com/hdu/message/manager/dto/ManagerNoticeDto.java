package com.hdu.message.manager.dto;

import com.hdu.message.manager.bean.ManagerNotice;
import lombok.Data;

import java.util.List;

@Data
public class ManagerNoticeDto {

    // 消息中心
    private ManagerNotice managerNotice;

    // 消息类型
    private Integer type;

    // 如果类型为用户点对点类型，则userList不为空
    private List<Long> userList;
}
