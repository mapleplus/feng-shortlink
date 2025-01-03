package com.feng.shortlink.admin.remote.dto.request;

import lombok.Data;

/**
 * @author FENGXIN
 * @date 2024/10/5
 * @project feng-shortlink
 * @description 短链接监控访问请求参数
 **/
@Data
public class ShortLinkStatsReqDTO {
    /**
     * 完整短链接
     */
    private String fullShortUrl;
    
    /**
     * 分组标识
     */
    private String gid;
    
    /**
     * 开始日期
     */
    private String startDate;
    
    /**
     * 启用标识 0：启用 1：未启用
     */
    private Integer enableStatus;
    
    /**
     * 结束日期
     */
    private String endDate;
}
