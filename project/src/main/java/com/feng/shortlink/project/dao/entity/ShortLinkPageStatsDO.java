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
public class ShortLinkPageStatsDO {
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
     * 结束日期
     */
    private String endDate;
    
    /**
     * 用户访问日志列表
     */
    private List<String> userAccessLogsList;
}
