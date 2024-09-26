package com.feng.shortlink.admin.controller;

import cn.hutool.core.bean.BeanUtil;
import com.feng.shortlink.admin.common.convention.result.Result;
import com.feng.shortlink.admin.common.convention.result.Results;
import com.feng.shortlink.admin.dto.response.UserActualRespDTO;
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
    
    /**
     * 根据用户名检索用户信息。
     *
     * @param username 要检索的用户名
     * @return 包含用户详细信息的 {@code Result<UserRespDTO>} 对象，如果找到该用户
     */
    @GetMapping("/api/fenglink/v1/user/{username}")
    public Result<UserRespDTO> getUserByUserName (@PathVariable String username) {
        return Results.success (userService.getUserByUserName (username));
    }
    
    /**
     * 根据用户名检索实际用户信息。
     *
     * @param username 要检索的用户名
     * @return 包含实际用户详细信息的 {@code Result<UserActualRespDTO>} 对象，如果找到该用户
     */
    @GetMapping("/api/fenglink/v1/actual/user/{username}")
    public Result<UserActualRespDTO> getActualUserByUserName (@PathVariable String username) {
        return Results.success (BeanUtil.toBean (userService.getUserByUserName (username) , UserActualRespDTO.class));
    }
    
    /**
     * 检查是否存在指定用户名的用户。
     *
     * @param username 要检查的用户名
     * @return 包含布尔值的 Result 对象，表示用户名是否存在
     */
    @GetMapping("/api/fenglink/v1/user/has-username")
    public Result<Boolean> hasUserName (String username) {
        return Results.success (userService.hasUserName(username));
    }
}
