package com.feng.shortlink.admin.common.biz.user;




import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author FENGXIN
 * @date 2024/9/28
 * @project feng-shortlink
 * @description 用户信息实体
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfoDTO {
    
    /**
     * 用户 ID
     */
    private String id;
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 真实姓名
     */
    private String realName;
    
}
