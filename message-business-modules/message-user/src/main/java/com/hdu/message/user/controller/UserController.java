package com.hdu.message.user.controller;

import com.hdu.message.common.base.entity.User;
import com.hdu.message.common.base.utils.ResponseUtil;
import com.hdu.message.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("addNewUser")
    public Object addNewUser(@RequestBody User user) {
        Integer insertNum = userService.addNewUser(user);
        return ResponseUtil.ok(insertNum);
    }
}
