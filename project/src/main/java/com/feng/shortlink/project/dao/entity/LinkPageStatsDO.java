package com.feng.shortlink.project.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author FENGXIN
 * @date 2024/10/5
 * @project feng-shortlink
 * @description 分页查询短链接监控访问请求参数
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LinkPageStatsDO {
    /**
     * 完整短链接
     */
    private String fullShortUrl;
    
    /**
     * 开始日期
     */
    private String startDate;
    
    /** 启用标识 0：已启用 1：未启用 */
    private Integer enableStatus;
    
    /**
     * 结束日期
     */
    private String endDate;
    
    /**
     * 用户访问日志列表
     */
    private List<String> userAccessLogsList;
}
