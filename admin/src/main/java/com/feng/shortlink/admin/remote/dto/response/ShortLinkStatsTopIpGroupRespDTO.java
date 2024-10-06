package com.feng.shortlink.admin.remote.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author FENGXIN
 * @date 2024/10/5
 * @project feng-shortlink
 * @description 短链接高频访问IP监控响应参数
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShortLinkStatsTopIpGroupRespDTO {
    /**
     * 统计
     */
    private Integer cnt;
    
    /**
     * IP
     */
    private String ip;
}
