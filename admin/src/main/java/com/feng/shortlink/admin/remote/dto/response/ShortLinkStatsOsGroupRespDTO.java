package com.feng.shortlink.admin.remote.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author FENGXIN
 * @date 2024/10/5
 * @project feng-shortlink
 * @description 短链接操作系统监控响应参数
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShortLinkStatsOsGroupRespDTO {
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
