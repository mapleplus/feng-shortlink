package com.feng.shortlink.project.dto.response;

/**
 * @author FENGXIN
 * @date 2024/10/5
 * @project feng-shortlink
 * @description 短链接地区监控响应参数
 **/
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
