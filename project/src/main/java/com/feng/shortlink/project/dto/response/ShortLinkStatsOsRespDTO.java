package com.feng.shortlink.project.dto.response;

/**
 * @author FENGXIN
 * @date 2024/10/5
 * @project feng-shortlink
 * @description 短链接操作系统监控响应参数
 **/
public class ShortLinkStatsOsRespDTO {
    /**
     * 统计
     */
    private Integer cnt;
    
    /**
     * 操作系统
     */
    private String os;
    
    /**
     * 占比
     */
    private Double ratio;
}
