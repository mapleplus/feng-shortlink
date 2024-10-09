package com.feng.shortlink.project.dto.biz;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author FENGXIN
 * @date 2024/10/9
 * @project feng-shortlink
 * @description 消息队列发送实体
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShortLinkStatsMqToDbDTO {
    
    /**
     * 完整短链接
     */
    private String fullShortLink;
    
    /**
     * gid
     */
    private String gid;
    /**
     * 访问用户IP
     */
    private String userIpAddress;
    
    /**
     * 操作系统
     */
    private String os;
    
    /**
     * 浏览器
     */
    private String browser;
    
    /**
     * 操作设备
     */
    private String device;
    
    /**
     * 网络
     */
    private String network;
    
    /**
     * UV
     */
    private String uv;
    
    /**
     * UV访问标识
     */
    private Boolean uvFlag;
    
    /**
     * UIP访问标识
     */
    private Boolean uipFlag;
}
