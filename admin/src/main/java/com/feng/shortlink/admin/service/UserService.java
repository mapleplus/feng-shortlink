package com.feng.shortlink.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.feng.shortlink.admin.common.convention.exception.ClientException;
import com.feng.shortlink.admin.dao.entity.UserDO;
import com.feng.shortlink.admin.dto.request.UserLoginReqDTO;
import com.feng.shortlink.admin.dto.request.UserRegisterReqDTO;
import com.feng.shortlink.admin.dto.request.UserUpdateReqDTO;
import com.feng.shortlink.admin.dto.response.UserLoginRespDTO;
import com.feng.shortlink.admin.dto.response.UserRespDTO;

/**
 * @author FENGXIN
 * @date 2024/9/26
 * @project feng-shortlink
 * @description 用户管理接口
 **/
public interface UserService extends IService<UserDO>{
    
    /**
     * 根据给定的用户名检索用户。
     *
     * @param username 要检索的用户名。
     * @return 指定用户名的用户详细信息。
     */
    UserRespDTO getUserByUserName (String username);
    
    /**
     * 检查用户名是否存在于Cache中。
     * 布隆过滤器存储用户名
     *
     * @param username 要检查的用户名。
     * @return 如果用户名不存在于数据库中则返回 {@code true}，否则返回 {@code false}。
     */
    Boolean hasUserName (String username);
    
    /**
     * 在系统中注册一个新用户。
     *
     * @param requestParams 包含要注册用户的详细信息的数据传输对象。
     */
    void registerUser (UserRegisterReqDTO requestParams);
    
    /**
     * 更新用户信息。
     *
     * @param requestParams 包含要更新用户的详细信息的数据传输对象。
     */
    void updateUser (UserUpdateReqDTO requestParams);
    
    /**
     * 处理用户的登录过程。
     *
     * @param requestParams 包含用户凭据的登录请求参数。
     * @return 包含用户登录详细信息的响应DTO，例如身份验证令牌。
     */
    UserLoginRespDTO login (UserLoginReqDTO requestParams);
    
    /**
     * 检查用户是否使用特定的令牌登录。
     *
     * @param username 要检查的用户名。
     * @param token    用于验证用户登录状态的令牌。
     * @return 如果用户使用给定令牌登录，则返回 {@code true}，否则返回 {@code false}。
     */
    Boolean checkLogin (String username , String token);
    
    /**
     * 通过从Redis中删除用户的会话令牌来注销用户，如果用户当前已登录，
     * 并在注销操作失败时抛出异常。
     *
     * @param username 要注销的用户用户名
     * @param token    用于注销的用户会话令牌
     * @throws ClientException 如果用户未登录或注销过程中出现错误
     */
    void logout (String username , String token);
}
