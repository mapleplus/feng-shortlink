package com.feng.shortlink.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.util.TimeZone;

/**
 * @author FENGXIN
 * @date 2024/9/24
 * @project feng-shortlink
 * @description
 **/
@SpringBootApplication
@MapperScan("com.feng.shortlink.admin.dao.mapper")
@EnableDiscoveryClient
@EnableFeignClients("com.feng.shortlink.admin.remote")
public class ShortLinkAdminApplication {
    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
        SpringApplication.run(ShortLinkAdminApplication.class, args);
    }
    
}
