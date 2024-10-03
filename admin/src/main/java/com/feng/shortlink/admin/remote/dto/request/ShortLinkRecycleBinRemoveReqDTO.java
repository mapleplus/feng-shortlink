package com.feng.shortlink.admin.remote.dto.request;

import lombok.Data;

/**
 * @author FENGXIN
 * @date 2024/10/3
 * @project feng-shortlink
 * @description 短链接移除请求参数
 **/
@Data
public class ShortLinkRecycleBinRemoveReqDTO {
    /**
     * GID
     */
    private String gid;
    /**
     * 完整短 URL
     */
    private String fullShortUrl;
}
