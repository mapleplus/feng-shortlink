package com.feng.shortlink.admin.remote.dto.request;

import lombok.Data;

/**
 * @author FENGXIN
 * @date 2024/10/5
 * @project feng-shortlink
 * @description 分页查询短链接监控访问请求参数
 **/
@Data
public class ShortLinkPageStatsReqDTO {
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
}
