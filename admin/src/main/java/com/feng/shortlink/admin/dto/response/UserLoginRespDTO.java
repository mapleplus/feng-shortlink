package com.feng.shortlink.admin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author FENGXIN
 * @date 2024/9/27
 * @project feng-shortlink
 * @description 用户登录响应
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginRespDTO {
    private String token;
}
