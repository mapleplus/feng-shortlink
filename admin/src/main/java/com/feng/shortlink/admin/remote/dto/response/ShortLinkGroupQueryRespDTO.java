package com.feng.shortlink.admin.remote.dto.response;

import lombok.Data;

/**
 * @author FENGXIN
 * @date 2024/9/30
 * @project feng-shortlink
 * @description 分组信息查询返回参数
 **/
@Data
public class ShortLinkGroupQueryRespDTO {
    
    /**
     * GID
     */
    private String gid;
    
    /**
     * 组计数
     */
    private Integer groupCount;
}