package com.feng.shortlink.project.handler;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static com.feng.shortlink.project.common.constant.RedisCacheConstant.SHORTLINK_MESSAGE_QUEUE_IDEMPOTENT_KEY;

/**
 * @author FENGXIN
 * @date 2024/10/9
 * @project feng-shortlink
 * @description 消息幂等处理
 **/
@Component
@RequiredArgsConstructor
public class MessageQueueIdempotentHandler {
    private final StringRedisTemplate stringRedisTemplate;
    
    /**
     * 设置 Message Queue 幂等
     *
     * @param messageId 消息 ID
     * @return boolean
     */
    public boolean isMessageQueueIdempotent(String messageId) {
        return Boolean.TRUE.equals (stringRedisTemplate
                .opsForValue ()
                .setIfAbsent (String.format (SHORTLINK_MESSAGE_QUEUE_IDEMPOTENT_KEY , messageId)
                , "0"
                , 2 ,
                TimeUnit.MINUTES));
    }
    
    /**
     * 校验消息消费完成
     *
     * @param messageId 消息 ID
     * @return boolean
     */
    public boolean isAccomplishMessageQueueIdempotent(String messageId) {
        return StrUtil.equals (stringRedisTemplate.opsForValue ().get (String.format (SHORTLINK_MESSAGE_QUEUE_IDEMPOTENT_KEY,messageId)) , "1");
    }
    
    /**
     * 删除 Message Queue 幂等
     *
     * @param messageId 消息 ID
     */
    public void removeMessageQueueIdempotent(String messageId) {
        stringRedisTemplate.delete (String.format (SHORTLINK_MESSAGE_QUEUE_IDEMPOTENT_KEY, messageId));
    }
    
    /**
     * 设置消息队列幂等
     * 正确无异常完成消费后设置完成标识1
     * @param messageId 消息 ID
     */
    public void setMessageQueueIdempotent(String messageId) {
        stringRedisTemplate.opsForValue ()
                .set (String.format (SHORTLINK_MESSAGE_QUEUE_IDEMPOTENT_KEY , messageId)
                , "1"
                , 2
                , TimeUnit.MINUTES);
    }
}
