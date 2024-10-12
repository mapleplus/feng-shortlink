package com.feng.shortlink.project.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author FENGXIN
 * @date 2024/10/7
 * @project feng-shortlink
 * @description 更新total pv uv uip 请求参数
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShortLinkUpdatePvUvUipDO {
    
    /** 分组标识 */
    private String gid;
    
    /**
     * 点击量
     */
    private Integer clickNum;
    
    /** 完整短链接 */
    private String fullShortUrl;
    
    /**
     * 总uv
     */
    private Integer  totalUv;
    
    /**
     * 总 PV
     */
    private Integer  totalPv;
    
    /**
     * 总 UIP
     */
    private Integer  totalUip;
}
