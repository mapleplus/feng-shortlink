package com.feng.shortlink.project;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author FENGXIN
 * @date 2024/9/29
 * @project feng-shortlink
 * @description
 **/
@SpringBootApplication
@MapperScan("com.feng.shortlink.project.dao.mapper")
public class ShortLinkApplication {
    public static void main (String[] args) {
        SpringApplication.run(ShortLinkApplication.class, args);
    }
}
