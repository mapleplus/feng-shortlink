package com.feng.shortlink.admin.config;

import com.feng.shortlink.admin.common.biz.user.UserContext;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author FENGXIN
 * @date 2024/10/13
 * @project feng-shortlink
 * @description openFeign 微服务调用传递用户信息配置
 **/
@Configuration
public class OpenFeignConfiguration {
    @Bean
    public RequestInterceptor requestInterceptor() {
        return template -> {
            template.header ("username", UserContext.getUserName ());
            template.header ("userId", UserContext.getUserId ());
            template.header ("realName", UserContext.getRealName ());
        };
    }
}
