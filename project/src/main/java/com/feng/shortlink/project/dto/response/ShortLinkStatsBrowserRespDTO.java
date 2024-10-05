package com.feng.shortlink.project.dto.response;

/**
 * @author FENGXIN
 * @date 2024/10/5
 * @project feng-shortlink
 * @description 短链接浏览器监控响应参数
 **/
public class ShortLinkStatsBrowserRespDTO {
    /**
     * 统计
     */
    private Integer cnt;
    
    /**
     * 浏览器
     */
    private String browser;
    
    /**
     * 占比
     */
    private Double ratio;
}
