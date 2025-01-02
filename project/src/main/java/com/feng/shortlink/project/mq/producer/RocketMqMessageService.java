package com.feng.shortlink.project.mq.producer;

import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson2.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.MessageConst;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author FENGXIN
 * @date 2024/10/9
 * @project feng-shortlink
 * @description 短链接监控消息生产
 **/
@Service
@RequiredArgsConstructor
@Slf4j
public class RocketMqMessageService {
    
    private final RocketMQTemplate rocketMqTemplate;
    
    public void sendMessage(String topic, Map<String,String> producerMap) {
        String keys = UUID.randomUUID().toString();
        producerMap.put("keys", keys);
        Message<Map<String, String>> build = MessageBuilder
                .withPayload(producerMap)
                .setHeader(MessageConst.PROPERTY_KEYS, keys)
                .build();
        SendResult sendResult;
        try {
            sendResult = rocketMqTemplate.syncSend(topic, build, 2000L);
            log.info("[消息访问统计监控] 消息发送结果：{}，消息ID：{}，消息Keys：{}", sendResult.getSendStatus(), sendResult.getMsgId(), keys);
        } catch (Throwable ex) {
            log.error("[消息访问统计监控] 消息发送失败，消息体：{}", JSON.toJSONString (producerMap) , ex);
        }
        
    }
}
