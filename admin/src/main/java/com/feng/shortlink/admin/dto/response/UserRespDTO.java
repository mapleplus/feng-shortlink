package com.feng.shortlink.admin.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.feng.shortlink.admin.common.serializer.PhoneDesensitizationSerializer;
import lombok.Data;

/**
 * @author FENGXIN
 * @date 2024/9/26
 * @project feng-shortlink
 * @description 用户创建响应
 **/
@Data
public class UserRespDTO {
    
    /**
     * 用户的唯一标识
     */
    private Long id;
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 用户真实姓名
     */
    private String realName;
    
    /**
     * 用户电话号码
     * &#064;JsonSerialize  json提供的手机号序列化器 这里用于脱敏展示
     */
    @JsonSerialize(using = PhoneDesensitizationSerializer.class)
    private String phone;
    
    /**
     * 用户邮箱地址
     */
    private String mail;
}
