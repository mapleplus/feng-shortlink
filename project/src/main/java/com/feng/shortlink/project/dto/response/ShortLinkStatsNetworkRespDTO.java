package com.feng.shortlink.project.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author FENGXIN
 * @date 2024/10/5
 * @project feng-shortlink
 * @description 短链接访问网络监控响应参数
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShortLinkStatsNetworkRespDTO {
    /**
     * 统计
     */
    private Integer cnt;
    
    /**
     * 访问网络
     */
    private String network;
    
    /**
     * 占比
     */
    private Double ratio;
}
