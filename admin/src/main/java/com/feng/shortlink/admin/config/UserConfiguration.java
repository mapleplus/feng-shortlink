package com.feng.shortlink.admin.config;

import com.feng.shortlink.admin.common.biz.user.UserFlowRiskControlFilter;
import com.feng.shortlink.admin.common.biz.user.UserTransmitInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author FENGXIN
 * @date 2024/9/28
 * @project feng-shortlink
 * @description
 **/
@Configuration
@RequiredArgsConstructor
public class UserConfiguration implements WebMvcConfigurer {
    private final UserTransmitInterceptor userTransmitInterceptor;
    
    /**
     * 用户信息传递拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userTransmitInterceptor);
    }
    
    /**
     * 用户操作流量风控过滤器
     */
    @Bean
    @ConditionalOnProperty(name = "short-link.flow-limit.enable", havingValue = "true")
    public FilterRegistrationBean<UserFlowRiskControlFilter> globalUserFlowRiskControlFilter(
            StringRedisTemplate stringRedisTemplate,
            UserFlowRiskControlConfiguration userFlowRiskControlConfiguration) {
        FilterRegistrationBean<UserFlowRiskControlFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new UserFlowRiskControlFilter(stringRedisTemplate, userFlowRiskControlConfiguration));
        registration.addUrlPatterns("/*");
        registration.setOrder(10);
        return registration;
    }
}
