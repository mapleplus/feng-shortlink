package com.feng.shortlink.admin.controller;

import cn.hutool.core.bean.BeanUtil;
import com.feng.shortlink.admin.common.convention.result.Result;
import com.feng.shortlink.admin.common.convention.result.Results;
import com.feng.shortlink.admin.dto.request.UserLoginReqDTO;
import com.feng.shortlink.admin.dto.request.UserRegisterReqDTO;
import com.feng.shortlink.admin.dto.request.UserUpdateReqDTO;
import com.feng.shortlink.admin.dto.response.UserActualRespDTO;
import com.feng.shortlink.admin.dto.response.UserLoginRespDTO;
import com.feng.shortlink.admin.dto.response.UserRespDTO;
import com.feng.shortlink.admin.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

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
    
    /**
     * 处理注册新用户的HTTP POST请求。
     *
     * @param registerUserReqDTO 包含新用户注册详细信息的{@link UserRegisterReqDTO}对象
     * @return 一个{@link Result}对象，包含空数据但表示成功
     */
    @PostMapping("/api/fenglink/v1/user")
    public Result<Void> registerUser (@RequestBody UserRegisterReqDTO requestParams) {
        userService.registerUser (requestParams);
        return Results.success ();
    }
    
    /**
     * 处理更新用户的HTTP PUT请求。
     *
     * @param requestParams 包含更新用户详细信息的{@link UserUpdateReqDTO}对象
     * @return 一个{@link Result}对象，包含空数据但表示成功
     */
    @PutMapping("/api/fenglink/v1/user")
    public Result<Void> updateUser (@RequestBody UserUpdateReqDTO requestParams) {
        userService.updateUser (requestParams);
        return Results.success ();
    }
    
    /**
     * 处理用户登录请求。
     *
     * @param requestParams 包含用户登录信息的 {@code UserLoginRespDTO} 对象
     * @return 包含用户登录响应数据的 {@code Result<UserLoginRespDTO>} 对象
     */
    @PostMapping("/api/fenglink/v1/user/login")
    public Result<UserLoginRespDTO> login (@RequestBody UserLoginReqDTO requestParams) {
        return Results.success (userService.login(requestParams));
    }
    
    /**
     * 检查用户登录状态
     *
     * @param username 用户名
     * @param token 授权令牌
     * @return 用户是否已登录的结果
     */
    @GetMapping("/api/short-link/admin/v1/user/check-login")
    public Result<Boolean> checkLogin (@RequestParam("username") String username,@RequestParam("token") String token){
        return Results.success (userService.checkLogin (username,token));
    }
    
    /**
     * 用户登出
     *
     * @param username 用户名
     * @param token 授权令牌
     * @return 成功响应结果
     */
    @DeleteMapping("/api/short-link/admin/v1/user/logout")
    public Result<Void> logout (@RequestParam("username") String username,@RequestParam("token") String token){
        userService.logout(username,token);
        return Results.success ();
    }
}
