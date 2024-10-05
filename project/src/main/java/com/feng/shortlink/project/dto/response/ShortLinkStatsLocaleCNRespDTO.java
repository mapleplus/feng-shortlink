package com.feng.shortlink.project.dto.response;

import groovy.transform.builder.Builder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author FENGXIN
 * @date 2024/10/5
 * @project feng-shortlink
 * @description 短链接地区监控响应参数
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShortLinkStatsLocaleCNRespDTO {
    /**
     * 统计
     */
    private Integer cnt;
    
    /**
     * 地区
     */
    private String locale;
    
    /**
     * 占比
     */
    private Double ratio;
}
