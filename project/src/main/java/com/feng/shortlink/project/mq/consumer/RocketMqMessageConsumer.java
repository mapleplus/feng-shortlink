package com.feng.shortlink.project.mq.consumer;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.Week;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.feng.shortlink.project.common.convention.exception.ServiceException;
import com.feng.shortlink.project.dao.entity.*;
import com.feng.shortlink.project.dao.mapper.*;
import com.feng.shortlink.project.dto.biz.ShortLinkStatsMqToDbDTO;
import com.feng.shortlink.project.dto.biz.ShortLinkStatsRecordDTO;
import com.feng.shortlink.project.dto.request.ShortLinkUpdatePvUvUipDO;
import com.feng.shortlink.project.handler.MessageQueueIdempotentHandler;
import com.feng.shortlink.project.mq.producer.DelayShortLinkStatsProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.feng.shortlink.project.common.constant.RedisCacheConstant.LOCK_GID_UPDATE_KEY;
import static com.feng.shortlink.project.common.constant.ShortLinkConstant.SHORT_LINK_LOCALE_STATS_URL;

/**
 * @author FENGXIN
 * @date 2024/10/9
 * @project feng-shortlink
 * @description 短链接监控消息消费
 **/
@Slf4j
@Service
@RequiredArgsConstructor
@RocketMQMessageListener(topic = "shortlink-stats-topic", consumerGroup = "shortlink-stats-consumer-group")
public class RocketMqMessageConsumer implements RocketMQListener<MessageExt> {

    private final LinkGotoMapper linkGotoMapper;
    private final RedissonClient redissonClient;
    private final LinkAccessStatsMapper linkAccessStatsMapper;
    private final LinkLocaleStatsMapper linkLocaleStatsMapper;
    private final LinkOsStatsMapper linkOsStatsMapper;
    private final LinkBrowserStatsMapper linkBrowserStatsMapper;
    private final LinkAccessLogsMapper linkAccessLogsMapper;
    private final LinkDeviceStatsMapper linkDeviceStatsMapper;
    private final LinkNetworkStatsMapper linkNetworkStatsMapper;
    private final ShortLinkMapper shortLinkMapper;
    private final LinkStatsTodayMapper linkStatsTodayMapper;
    private final DelayShortLinkStatsProducer delayShortLinkStatsProducer;
    private final MessageQueueIdempotentHandler messageQueueIdempotentHandler;
    
    @Value("${short-link.stats.locale.amap-key}")
    private String amapKey;
    
    @Override
    public void onMessage (MessageExt message) {
        // 幂等 如果同一个消息已经消费则跳过并返回
        if(!messageQueueIdempotentHandler.isMessageQueueIdempotent (message.getMsgId ())){
            // 预防在插入数据失败后但是未执行异常处理 此时需要重试消费确保数据完整插入 因此需要处理异常时删除redis原有的messageId
            // 未完成抛出异常 rocketMQ重试机制
            // 完成了则幂等 直接返回
            if(!messageQueueIdempotentHandler.isAccomplishMessageQueueIdempotent (message.getMsgId ())){
                throw new ServiceException ("消费失败 请重试");
            }
            return;
        }
        // 第一次消费
        try {
            ShortLinkStatsMqToDbDTO shortLinkStatsMqToDbDTO = JSON.parseObject (new String (message.getBody ()) , ShortLinkStatsMqToDbDTO.class);
            actualShortLinkStats (shortLinkStatsMqToDbDTO);
        } catch (Throwable e) {
            log.error ("数据插入异常 重试消费");
            messageQueueIdempotentHandler.removeMessageQueueIdempotent (message.getMsgId ());
        }
        // 正确无异常消费后设置完成标志
        messageQueueIdempotentHandler.setMessageQueueIdempotent (message.getMsgId ());
    }
    public void actualShortLinkStats(ShortLinkStatsMqToDbDTO shortLinkStatsMqToDbDTO) {
        String fullShortLink = shortLinkStatsMqToDbDTO.getFullShortLink ();
        String gid = shortLinkStatsMqToDbDTO.getGid ();
        ShortLinkStatsRecordDTO statsRecord = BeanUtil.copyProperties (shortLinkStatsMqToDbDTO , ShortLinkStatsRecordDTO.class);
        RReadWriteLock readWriteLock = redissonClient.getReadWriteLock(String.format(LOCK_GID_UPDATE_KEY, fullShortLink));
        RLock rLock = readWriteLock.readLock();
        // 如果修改短链接时有用户访问 则延迟统计数据
        if (!rLock.tryLock()) {
            delayShortLinkStatsProducer.send(shortLinkStatsMqToDbDTO);
            return;
        }
        try{
            // 一般数据统计
            if (StrUtil.isBlank (gid)){
                LambdaQueryWrapper<LinkGotoDO> lambdaQueryWrapper = new LambdaQueryWrapper<LinkGotoDO> ()
                        .eq(LinkGotoDO::getFullShortUrl,fullShortLink);
                gid = linkGotoMapper.selectOne (lambdaQueryWrapper).getGid();
            }
            Date fullDate = DateUtil.date (new Date ());
            int hour = DateUtil.hour (fullDate , true);
            Week dayOfWeekEnum = DateUtil.dayOfWeekEnum (fullDate);
            int weekday = dayOfWeekEnum.getIso8601Value ();
            LinkAccessStatsDO linkAccessStatsDO = LinkAccessStatsDO.builder ()
                    .gid(gid)
                    .fullShortUrl (fullShortLink)
                    .date (fullDate)
                    .pv (1)
                    .uv (statsRecord.getUvFlag () ? 1 : 0)
                    .uip (statsRecord.getUipFlag () ? 1 : 0)
                    .hour (hour)
                    .weekday (weekday)
                    .createTime (fullDate)
                    .updateTime (fullDate)
                    .build ();
            linkAccessStatsMapper.shortLinkAccessState (linkAccessStatsDO);
            
            // 地区统计
            // 通过http工具访问高德地图接口获取地区
            Map<String,Object> localParamMap = new HashMap<> ();
            localParamMap.put("key",amapKey);
            localParamMap.put("ip",statsRecord.getUserIpAddress ());
            String localInfo = HttpUtil.get (SHORT_LINK_LOCALE_STATS_URL , localParamMap);
            JSONObject localeObject = JSON.parseObject (localInfo , JSONObject.class);
            String infocode = localeObject.getString ("infocode");
            // 如果状态🐎是10000则表示成功获取
            String actualProvince = "未知";
            String actualCity = "未知";
            if(StrUtil.isNotBlank (infocode) && StrUtil.equals (infocode,"10000")){
                String province = localeObject.getString ("province");
                boolean unKnown = StrUtil.equals (province,"[]");
                LinkLocaleStatsDO linkLocaleStatsDO = LinkLocaleStatsDO.builder ()
                        .gid (gid)
                        .fullShortUrl (fullShortLink)
                        .date (fullDate)
                        .province (actualProvince = unKnown ? "未知" : province)
                        .city (actualCity = unKnown ? "未知" : localeObject.getString ("city"))
                        .adcode (unKnown ? "未知" : localeObject.getString ("adcode"))
                        .country ("中国")
                        .cnt (1)
                        .build ();
                linkLocaleStatsMapper.shortLinkLocaleState (linkLocaleStatsDO);
            }
            
            // 操作系统统计
            LinkOsStatsDO linkOsStatsDO = LinkOsStatsDO.builder ()
                    .gid (gid)
                    .fullShortUrl (fullShortLink)
                    .date (fullDate)
                    .cnt (1)
                    .os (statsRecord.getOs ())
                    .build ();
            linkOsStatsMapper.shortLinkBrowserState (linkOsStatsDO);
            
            // 浏览器统计
            LinkBrowserStatsDO linkBrowserStatsDO = LinkBrowserStatsDO.builder ()
                    .gid (gid)
                    .fullShortUrl (fullShortLink)
                    .date (fullDate)
                    .cnt (1)
                    .browser (statsRecord.getBrowser ())
                    .build ();
            linkBrowserStatsMapper.shortLinkBrowserState (linkBrowserStatsDO);
            
            // 访问设备统计
            LinkDeviceStatsDO linkDeviceStatsDO = LinkDeviceStatsDO.builder ()
                    .gid (gid)
                    .fullShortUrl (fullShortLink)
                    .date (fullDate)
                    .cnt (1)
                    .device (statsRecord.getDevice ())
                    .build ();
            linkDeviceStatsMapper.shortLinkDeviceState (linkDeviceStatsDO);
            
            // 访问网络统计
            LinkNetworkStatsDO linkNetworkStatsDO = LinkNetworkStatsDO.builder ()
                    .gid (gid)
                    .fullShortUrl (fullShortLink)
                    .date (fullDate)
                    .cnt (1)
                    .network (statsRecord.getNetwork ())
                    .build ();
            linkNetworkStatsMapper.shortLinkNetworkState (linkNetworkStatsDO);
            
            // 日志统计
            LinkAccessLogsDO linkAccessLogsDO = LinkAccessLogsDO.builder ()
                    .gid (gid)
                    .fullShortUrl (fullShortLink)
                    .ip (statsRecord.getUserIpAddress ())
                    .user (statsRecord.getUv ())
                    .os (statsRecord.getOs ())
                    .browser (statsRecord.getBrowser ())
                    .network (statsRecord.getNetwork ())
                    .device (statsRecord.getDevice ())
                    .locale (StrUtil.join ("-","中国",actualProvince,actualCity))
                    .cnt (1)
                    .build ();
            linkAccessLogsMapper.shortLinkBrowserState (linkAccessLogsDO);
            
            //total pv uv uip
            ShortLinkUpdatePvUvUipDO shortLinkUpdatePvUvUipDO = ShortLinkUpdatePvUvUipDO.builder ()
                    .gid (gid)
                    .fullShortUrl (fullShortLink)
                    .totalPv (1)
                    .totalUv (statsRecord.getUvFlag () ? 1 : 0)
                    .totalUip (statsRecord.getUipFlag () ? 1 : 0)
                    .build ();
            shortLinkMapper.totalPvUvUipUpdate (shortLinkUpdatePvUvUipDO);
            
            //今日统计
            LinkStatsTodayDO statsTodayDO = LinkStatsTodayDO.builder ()
                    .gid (gid)
                    .fullShortUrl (fullShortLink)
                    .date (fullDate)
                    .todayPv (1)
                    .todayUv (statsRecord.getUvFlag () ? 1 : 0)
                    .todayUip (statsRecord.getUipFlag () ? 1 : 0)
                    .build ();
            linkStatsTodayMapper.linkStatTodayState (statsTodayDO);
        } catch (Throwable ex) {
            log.error ("短链接统计异常{}" , ex.getMessage ());
        } finally {
            rLock.unlock ();
        }
    }
}
