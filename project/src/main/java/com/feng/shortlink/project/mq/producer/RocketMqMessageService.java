package com.feng.shortlink.project.mq.producer;

import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.stereotype.Service;

/**
 * @author FENGXIN
 * @date 2024/10/9
 * @project feng-shortlink
 * @description 短链接监控消息生产
 **/
@Service
@RequiredArgsConstructor
public class RocketMqMessageService {
    
    private final RocketMQTemplate rocketMqTemplate;
    
    public void sendMessage(String topic, String message) {
        rocketMqTemplate.convertAndSend(topic, message);
    }
}
