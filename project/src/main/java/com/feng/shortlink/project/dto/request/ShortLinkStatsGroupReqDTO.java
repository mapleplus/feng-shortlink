package com.feng.shortlink.project.dto.request;

import lombok.Data;

/**
 * 分组短链接监控统计 DTO
 *
 * @author FENGXIN
 * @date 2024/10/5
 */
@Data
public class ShortLinkStatsGroupReqDTO {
    
    /**
     * 分组标识
     */
    private String gid;
    
    /** 启用标识 0：已启用 1：未启用 */
    private Integer enableStatus;
    
    /**
     * 开始日期
     */
    private String startDate;
    
    /**
     * 结束日期
     */
    private String endDate;
}
