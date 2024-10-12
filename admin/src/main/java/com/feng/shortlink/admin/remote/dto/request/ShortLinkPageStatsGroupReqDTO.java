package com.feng.shortlink.admin.remote.dto.request;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author FENGXIN
 * @date 2024/10/5
 * @project feng-shortlink
 * @description 分组分页查询短链接监控访问请求参数
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class ShortLinkPageStatsGroupReqDTO extends Page {
    
    /**
     * 分组标识
     */
    private String gid;
    
    /**
     * 开始日期
     */
    private String startDate;
    
    /**
     * 启用状态
     */
    private Integer enableStatus;
    
    /**
     * 结束日期
     */
    private String endDate;
}
