package com.feng.shortlink.admin.remote.dto.request;

import lombok.Data;

/**
 * @author FENGXIN
 * @date 2024/10/3
 * @project feng-shortlink
 * @description
 **/
@Data
public class RecycleBinSaveReqDTO {
    
    /**
     * GID
     */
    private String gid;
    
    /**
     * 完整短链接
     */
    private String fullShortUrl;
}
