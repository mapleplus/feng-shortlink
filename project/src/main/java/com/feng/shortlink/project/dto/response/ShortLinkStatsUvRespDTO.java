package com.feng.shortlink.project.dto.response;

/**
 * @author FENGXIN
 * @date 2024/10/5
 * @project feng-shortlink
 * @description 短链接访客监控响应参数
 **/
public class ShortLinkStatsUvRespDTO {
    /**
     * 统计
     */
    private Integer cnt;
    
    /**
     * 访客类型
     */
    private String uvType;
    
    /**
     * 占比
     */
    private Double ratio;
}
