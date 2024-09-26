package com.feng.shortlink.admin.controller;

import com.feng.shortlink.admin.common.convention.result.Result;
import com.feng.shortlink.admin.common.enums.UserErrorCodeEnum;
import com.feng.shortlink.admin.dto.response.UserRespDTO;
import com.feng.shortlink.admin.service.UserService;
import jakarta.annotation.Resource;
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
    @Resource
    private UserService userService;
    
    @GetMapping("/api/fenglink/v1/user/{username}")
    public Result<UserRespDTO> getUserByUserName(@PathVariable String username) {
        UserRespDTO userByUserName = userService.getUserByUserName (username);
        if (userByUserName == null ){
            return new Result<UserRespDTO> ().setCode (UserErrorCodeEnum.USER_NULL.code ()).setMessage (UserErrorCodeEnum.USER_NULL.message ()).setData (userByUserName);
        }else {
            return new Result<UserRespDTO> ().setCode ("0").setMessage ("欢迎你 " + userByUserName.getRealName ()).setData (userByUserName);
        }
    }
}
