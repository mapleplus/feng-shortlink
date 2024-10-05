package com.feng.shortlink.project.dto.response;

/**
 * @author FENGXIN
 * @date 2024/10/5
 * @project feng-shortlink
 * @description 短链接访问设备监控响应参数
 **/
public class ShortLinkStatsDeviceRespDTO {
    /**
     * 统计
     */
    private Integer cnt;
    
    /**
     * 设备类型
     */
    private String device;
    
    /**
     * 占比
     */
    private Double ratio;
}
