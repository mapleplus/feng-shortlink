package com.feng.shortlink.project.dto.request;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.feng.shortlink.project.dao.entity.LinkAccessLogsDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author FENGXIN
 * @date 2024/10/5
 * @project feng-shortlink
 * @description 分页查询短链接监控访问请求参数
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class ShortLinkPageStatsReqDTO extends Page<LinkAccessLogsDO> {
    /**
     * 完整短链接
     */
    private String fullShortUrl;
    
    /**
     * 分组标识
     */
    private String gid;
    
    /**
     * 启用状态
     */
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
