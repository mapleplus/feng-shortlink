package com.feng.shortlink.admin.dto.request;

import lombok.Data;

/**
 * @author FENGXIN
 * @date 2024/9/27
 * @project feng-shortlink
 * @description
 **/
@Data
public class UserLoginReqDTO {
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 用户密码
     */
    private String password;
}
