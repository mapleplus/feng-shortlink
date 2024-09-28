package com.feng.shortlink.admin.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.feng.shortlink.admin.common.database.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author FENGXIN
 * @date 2024/9/26
 * @project feng-shortlink
 * @description 用户实体
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("t_user")
public class UserDO extends BaseDO {
    
    /**
     * 用户的唯一标识
     */
    private Long id;
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 用户密码
     */
    private String password;
    
    /**
     * 用户真实姓名
     */
    private String realName;
    
    /**
     * 用户电话号码
     */
    private String phone;
    
    /**
     * 用户邮箱地址
     */
    private String mail;
    
    /**
     * 删除时间（时间戳）
     */
    private Long deletionTime;
    
}
