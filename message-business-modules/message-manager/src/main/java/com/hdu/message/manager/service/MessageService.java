package com.hdu.message.manager.service;

import com.hdu.message.manager.bean.ManagerNotice;
import com.hdu.message.manager.dto.ManagerNoticeDto;

import java.util.List;

public interface MessageService {

    Integer addManagerMessage(ManagerNoticeDto managerNoticeDto);

    List<ManagerNotice> getManagerMessage(ManagerNotice managerNotice, Integer page, Integer size, String sort, String order);

}
