package com.hdu.message.user.controller;

import com.hdu.message.common.base.dto.UserSectionDto;
import com.hdu.message.common.base.entity.Section;
import com.hdu.message.common.base.entity.User;
import com.hdu.message.common.base.utils.ResponseUtil;
import com.hdu.message.user.bean.UserSection;
import com.hdu.message.user.service.UserSectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("section")
public class UserSectionController {

    @Autowired
    private UserSectionService userSectionService;

    /**
     * 订阅栏目
     * @param userSectionDto 订阅信息
     * @return 订阅的条数
     */
    @PostMapping("subSection")
    public Object subSection(@RequestBody UserSectionDto userSectionDto) {
        Integer insertNum = userSectionService.subSection(userSectionDto);
        // TODO: 校准值
        return ResponseUtil.ok(insertNum);
    }

    /**
     * 获取所有栏目
     * @return 所有栏目列表
     */
    @GetMapping("getAllSection")
    public Object getAllSection() {
        List<Section> allSection = userSectionService.getAllSection();
        return ResponseUtil.okList(allSection);
    }

    /**
     * 获取当前用户所订阅栏目
     * @param user 用户信息
     * @return 用户栏目列表
     */
    @PostMapping("getUserSection")
    public Object getUserSection(@RequestBody User user) {
        List<UserSection> userSectionList = userSectionService.getUserSection(user);
        return ResponseUtil.okList(userSectionList);
    }

}
