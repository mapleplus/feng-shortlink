package com.feng.shortlink.admin.remote.dto.request;

import lombok.Data;

/**
 * @author FENGXIN
 * @date 2024/10/3
 * @project feng-shortlink
 * @description 短链接回收站请求
 **/
@Data
public class ShortLinkRecycleBinSaveReqDTO {
    
    /**
     * GID
     */
    private String gid;
    
    /**
     * 完整短链接
     */
    private String fullShortUrl;
}
