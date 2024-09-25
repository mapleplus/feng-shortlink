package com.feng.shortlink.admin.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author FENGXIN
 * @date 2024/9/25
 * @project feng-shortlink
 * @description 根据用户名查询用户信息
 **/
@RestController
public class UserController {
    
    @GetMapping("/api/fenglink/v1/user/{username}")
    public String getUserByUserName(@PathVariable String username) {
        return username;
    }
}
