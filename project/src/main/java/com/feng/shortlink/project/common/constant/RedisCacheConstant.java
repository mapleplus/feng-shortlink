package com.feng.shortlink.project.common.constant;

/**
 * @author FENGXIN
 * @date 2024/9/27
 * @project feng-shortlink
 * @description redis常量
 **/
public class RedisCacheConstant {
    /**
     * 锁 ShortLink Goto Key
     */
    public static final String LOCK_SHORTLINK_GOTO_KEY = "lock:shortlink:goto_%s";
    
    /**
     * 短链接 GoTo Key
     */
    public static final String SHORTLINK_GOTO_KEY = "shortlink:goto_%s";
    
    /**
     * shortlink isnull 跳转键
     */
    public static final String SHORTLINK_ISNULL_GOTO_KEY = "shortlink:is_null_goto_%s";
    
    /**
     * 短链接统计 UV 键
     */
    public static final String SHORTLINK_STATS_UV_KEY = "shortlink:stats_uv_%s";
    
    /**
     * 短链接统计 UIP 键
     */
    public static final String SHORTLINK_STATS_UIP_KEY = "shortlink:stats_uip_%s";
    
    /**
     * 短链接修改分组 ID 锁前缀 Key
     */
    public static final String LOCK_GID_UPDATE_KEY = "lock:shortlink:update-gid_%s";
    
    /**
     * 短链接延迟队列消费统计 Key
     */
    public static final String SHORTLINK_MESSAGE_QUEUE_IDEMPOTENT_KEY = "shortlink:message_queue_stats:%s";
}
