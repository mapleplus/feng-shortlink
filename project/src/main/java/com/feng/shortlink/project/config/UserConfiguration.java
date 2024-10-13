package com.feng.shortlink.project.config;

import com.feng.shortlink.project.common.biz.user.UserTransmitInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author FENGXIN
 * @date 2024/10/13
 * @project feng-shortlink
 * @description 用户配置
 **/
@Configuration
@RequiredArgsConstructor
public class UserConfiguration implements WebMvcConfigurer {
    private final UserTransmitInterceptor userTransmitInterceptor;
    
    @Override
    public void addInterceptors (InterceptorRegistry registry) {
        registry.addInterceptor (userTransmitInterceptor)
                .addPathPatterns ("/**");
    }
}
