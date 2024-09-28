package com.feng.shortlink.admin.dto.response;

import lombok.Data;

/**
 * @author FENGXIN
 * @date 2024/9/28
 * @project feng-shortlink
 * @description
 **/
@Data
public class GroupRespDTO {
    
    // 组ID
    private String gid;
    
    // 组名
    private String name;
    
    // 用户名
    private String username;
    
    // 排序顺序
    private Integer sortOrder;
}
