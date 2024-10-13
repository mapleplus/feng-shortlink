package com.feng.shortlink.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

/**
 * @author FENGXIN
 * @date 2024/10/10
 * @project feng-shortlink
 * @description
 **/
@SpringBootApplication
public class GatewayServiceApplication {
    public static void main (String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
        SpringApplication.run(GatewayServiceApplication.class, args);
    }
}
