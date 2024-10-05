package com.feng.shortlink.project.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * @author FENGXIN
 * @date 2024/10/5
 * @project feng-shortlink
 * @description 短链接基础访问监控响应参数
 **/
public class ShortLinkStatsAccessDailyRespDTO {
    /**
     * 日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date date;
    
    /**
     * 访问量
     */
    private Integer pv;
    
    /**
     * 独立访客数
     */
    private Integer uv;
    
    /**
     * 独立IP数
     */
    private Integer uip;
}
