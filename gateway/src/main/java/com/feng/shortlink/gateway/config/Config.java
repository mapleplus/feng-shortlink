package com.feng.shortlink.gateway.config;

import lombok.Data;

import java.util.List;

/**
 * @author FENGXIN
 * @date 2024/10/10
 * @project feng-shortlink
 * @description 过滤器配置
 **/
@Data
public class Config {
    /**
     * 白名单前置路径
     */
    private List<String> whitePathList;
}
