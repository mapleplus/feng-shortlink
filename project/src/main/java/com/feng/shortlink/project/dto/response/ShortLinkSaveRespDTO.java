package com.feng.shortlink.project.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author FENGXIN
 * @date 2024/9/29
 * @project feng-shortlink
 * @description 短链接创建响应参数
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShortLinkSaveRespDTO {
    
    /** 分组标识 */
    private String gid;
    
    /** 原始链接 */
    private String originUrl;
    
    /** 完整短链接 */
    private String fullShortUrl;
    
}
