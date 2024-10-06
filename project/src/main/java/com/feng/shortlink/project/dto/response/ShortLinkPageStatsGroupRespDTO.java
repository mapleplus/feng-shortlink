package com.feng.shortlink.project.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author FENGXIN
 * @date 2024/10/5
 * @project feng-shortlink
 * @description 短链接分页监控响应
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShortLinkPageStatsGroupRespDTO {
    
    /**
     * IP 地址
     */
    private String ip;
    
    /**
     * 浏览器信息
     */
    private String browser;
    
    /**
     * 操作系统信息
     */
    private String os;
    
    /**
     * 访问网络
     */
    private String network;
    
    /**
     * 访问设备
     */
    private String device;
    
    /**
     *访问地区
     */
    private String locale;
    
    /**
     * 访客类型
     */
    private String uvType;
    
    /**
     * 访问量
     */
    private Integer cnt;
    
    /**
     * 用户信息
     */
    private String user;
    
    /**
     * 访问时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}
