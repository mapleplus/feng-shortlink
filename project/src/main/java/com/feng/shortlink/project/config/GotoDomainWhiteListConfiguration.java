package com.feng.shortlink.project.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author FENGXIN
 * @date 2024/10/8
 * @project feng-shortlink
 * @description 网站白名单配置
 **/
@Data
@Component
@ConfigurationProperties(prefix = "short-link.goto-domain.white-list")
public class GotoDomainWhiteListConfiguration {
    
    /**
     * 是否开启跳转原始链接域名白名单验证
     */
    private Boolean enable;
    
    /**
     * 跳转原始域名白名单网站名称集合
     */
    private String names;
    
    /**
     * 可跳转的原始链接域名
     */
    private List<String> details;
}
