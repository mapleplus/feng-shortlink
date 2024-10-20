// package com.feng.shortlink.project.mq.producer;
//
// import com.alibaba.fastjson2.JSON;
// import com.feng.shortlink.project.dto.biz.ShortLinkStatsMqToDbDTO;
// import lombok.RequiredArgsConstructor;
// import org.apache.rocketmq.spring.core.RocketMQTemplate;
// import org.springframework.messaging.Message;
// import org.springframework.messaging.support.MessageBuilder;
// import org.springframework.stereotype.Component;
//
// /**
//  * @author FENGXIN
//  * @date 2024/10/8
//  * @project feng-shortlink
//  * @description
//  **/
// @Component
// @Deprecated
// @RequiredArgsConstructor
// public class DelayShortLinkStatsProducer {
//
//     private final RocketMQTemplate rocketMQTemplate;
//
//     /**
//      * 发送延迟消费短链接统计
//      *
//      * @param statsRecord 短链接统计实体参数
//      */
//     public void send(ShortLinkStatsMqToDbDTO statsRecord) {
//         Message<String> msg = MessageBuilder.withPayload (JSON.toJSONString (statsRecord)).build ();
//         // 使用 delayLevel 设置延迟时间，延迟 5 秒
//         rocketMQTemplate.syncSend("shortlink-stats-delayed-topic", msg , 5, 2);
//     }
// }
