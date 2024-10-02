package com.feng.shortlink.project.common.constant;

/**
 * @author FENGXIN
 * @date 2024/9/27
 * @project feng-shortlink
 * @description redis常量
 **/
public class RedisCacheConstant {
    public static final String LOCK_SHORTLINK_GOTO_KEY = "lock:shortlink:goto_%s";
    public static final String SHORTLINK_GOTO_KEY = "shortlink:goto_%s";
}
