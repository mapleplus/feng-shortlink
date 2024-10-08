package com.feng.shortlink.project.mq.producer;

import com.feng.shortlink.project.dto.biz.ShortLinkStatsRecordDTO;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBlockingDeque;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static com.feng.shortlink.project.common.constant.RedisCacheConstant.DELAY_QUEUE_STATS_KEY;

/**
 * @author FENGXIN
 * @date 2024/10/8
 * @project feng-shortlink
 * @description
 **/
@Component
@RequiredArgsConstructor
public class DelayShortLinkStatsProducer {
    
    private final RedissonClient redissonClient;
    
    /**
     * 发送延迟消费短链接统计
     *
     * @param statsRecord 短链接统计实体参数
     */
    public void send(ShortLinkStatsRecordDTO statsRecord) {
        RBlockingDeque<ShortLinkStatsRecordDTO> blockingDeque = redissonClient.getBlockingDeque(DELAY_QUEUE_STATS_KEY);
        RDelayedQueue<ShortLinkStatsRecordDTO> delayedQueue = redissonClient.getDelayedQueue(blockingDeque);
        delayedQueue.offer(statsRecord, 5, TimeUnit.SECONDS);
    }
}
