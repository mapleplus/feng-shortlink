package com.feng.shortlink.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.feng.shortlink.project.dao.entity.LinkAccessStatsDO;
import com.feng.shortlink.project.dao.entity.LinkDeviceStatsDO;
import com.feng.shortlink.project.dao.entity.LinkLocaleStatsDO;
import com.feng.shortlink.project.dao.entity.LinkNetworkStatsDO;
import com.feng.shortlink.project.dao.mapper.*;
import com.feng.shortlink.project.dto.request.ShortLinkStatsReqDTO;
import com.feng.shortlink.project.dto.response.*;
import com.feng.shortlink.project.service.ShortLinkStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author FENGXIN
 * @date 2024/10/5
 * @project feng-shortlink
 * @description
 **/
@Service
@RequiredArgsConstructor
public class ShortLinkStatsServiceImpl implements ShortLinkStatsService {
    
    private final LinkAccessStatsMapper linkAccessStatsMapper;
    private final LinkLocaleStatsMapper linkLocaleStatsMapper;
    private final LinkAccessLogsMapper linkAccessLogsMapper;
    private final LinkBrowserStatsMapper linkBrowserStatsMapper;
    private final LinkOsStatsMapper linkOsStatsMapper;
    private final LinkDeviceStatsMapper linkDeviceStatsMapper;
    private final LinkNetworkStatsMapper linkNetworkStatsMapper;
    
    @Override
    public ShortLinkStatsRespDTO getShortLinkStats (ShortLinkStatsReqDTO requestParam) {
        /*
        基础监控统计
         */
        List<LinkAccessStatsDO> listStatsByShortLink = linkAccessStatsMapper.listStatsByShortLink (requestParam);
        /*
        地区监控统计
         */
        List<ShortLinkStatsLocaleCNRespDTO> linkStatsLocaleCnRespDTOList = new ArrayList<> ();
        List<LinkLocaleStatsDO> listLocaleByShortLink = linkLocaleStatsMapper.listLocaleByShortLink (requestParam);
        // 统计所有地区访问数量
        int localeStatsCnSum = listLocaleByShortLink
                .stream ()
                .mapToInt (LinkLocaleStatsDO::getCnt)
                .sum ();
        // 统计百分比 并赋值响应体
        listLocaleByShortLink
                .forEach (each ->{
                    double ratio = (double) each.getCnt () /localeStatsCnSum;
                    double actualRatio = Math.round (ratio * 100.0) / 100.0;
                    ShortLinkStatsLocaleCNRespDTO build = ShortLinkStatsLocaleCNRespDTO.builder ()
                            .cnt (each.getCnt ())
                            .locale (each.getProvince ())
                            .ratio (actualRatio)
                            .build ();
                    linkStatsLocaleCnRespDTOList.add (build);
                });
        /*
        小时监控统计
         */
        List<Integer> hourStats = new ArrayList<> ();
        List<LinkAccessStatsDO> listHourStatsByShortLink = linkAccessStatsMapper.listHourStatsByShortLink (requestParam);
        for (int i = 0 ; i < 24 ; i++) {
            // 统计该条短链接每小时的访问量 没有就设置0
            AtomicInteger hour = new AtomicInteger (i);
            int hourPv = listHourStatsByShortLink.stream ()
                    .filter (each -> Objects.equals (each.getHour () , hour.get ()))
                    .findFirst ()
                    .map (LinkAccessStatsDO::getPv)
                    .orElse (0);
            hourStats.add (hourPv);
        }
        /*
        高频访问IP详情
         */
        List<ShortLinkStatsTopIpRespDTO> shortLinkStatsTopIpRespDTOList = new ArrayList<> ();
        linkAccessLogsMapper.listTopIpByShortLink (requestParam)
                .forEach (each ->{
                    ShortLinkStatsTopIpRespDTO build = ShortLinkStatsTopIpRespDTO.builder ()
                            .ip (each.get ("ip").toString ())
                            .cnt (Integer.valueOf (each.get ("count").toString ()))
                            .build ();
                    shortLinkStatsTopIpRespDTOList.add (build);
                });
        /*
        一周访问详情
         */
        List<Integer> weekdayStats = new ArrayList<> ();
        List<LinkAccessStatsDO> weekdayStatsByShortLink = linkAccessStatsMapper.listWeekdayStatsByShortLink (requestParam);
        for (int i = 1 ; i < 8 ; i++) {
            AtomicInteger weekday = new AtomicInteger (i);
            Integer weekdayCnt = weekdayStatsByShortLink
                    .stream ()
                    .filter (each -> Objects.equals (each.getWeekday () , weekday.get ()))
                    .findFirst ()
                    .map (LinkAccessStatsDO::getPv)
                    .orElse (0);
            weekdayStats.add (weekdayCnt);
        }
        /*
        浏览器访问详情
         */
        List<ShortLinkStatsBrowserRespDTO> shortLinkBrowserRespDTOList = new ArrayList<> ();
        List<HashMap<String, Object>> listBrowserStatsByShortLink = linkBrowserStatsMapper.listBrowserStatsByShortLink (requestParam);
        // 总浏览器数
        int browserCnt = listBrowserStatsByShortLink.stream ()
                .mapToInt (each -> Integer.parseInt (each.get ("count").toString ()))
                .sum ();
        listBrowserStatsByShortLink.forEach (each ->{
            double ratio = (double) Integer.parseInt (each.get ("count").toString ()) / browserCnt;
            double actualRatio = Math.round (ratio * 100.0) / 100.0;
            ShortLinkStatsBrowserRespDTO build = ShortLinkStatsBrowserRespDTO.builder ()
                    .browser (each.get ("browser").toString ())
                    .ratio (actualRatio)
                    .cnt (Integer.parseInt (each.get ("count").toString ()))
                    .build ();
            shortLinkBrowserRespDTOList.add (build);
        });
        /*
        操作系统访问详情
         */
        List<ShortLinkStatsOsRespDTO> shortLinkOsRespDTOList = new ArrayList<> ();
        List<HashMap<String, Object>> listOsStatsByShortLink = linkOsStatsMapper.listOsStatsByShortLink (requestParam);
        int osCnt = listOsStatsByShortLink.stream ()
                .mapToInt (each -> Integer.parseInt (each.get("count").toString ()))
                .sum ();
        listOsStatsByShortLink.forEach (each ->{
            double ratio = (double) Integer.parseInt (each.get ("count").toString ()) / browserCnt;
            double actualRatio = Math.round (ratio * 100.0) / 100.0;
            ShortLinkStatsOsRespDTO build = ShortLinkStatsOsRespDTO.builder ()
                    .os (each.get ("os").toString ())
                    .ratio (actualRatio)
                    .cnt (Integer.parseInt (each.get ("count").toString ()))
                    .build ();
            shortLinkOsRespDTOList.add (build);
        });
        /*
        访客访问类型详情
         */
        List<ShortLinkStatsUvRespDTO> shortLinkUvRespDTOList = new ArrayList<> ();
        HashMap<String, Object> uvTypeCntByShortLink = linkAccessLogsMapper.findUvTypeCntByShortLink (requestParam);
        int oldUserCnt = Integer.parseInt (uvTypeCntByShortLink.get ("oldUserCnt").toString ());
        int newUserCnt = Integer.parseInt (uvTypeCntByShortLink.get ("newUserCnt").toString ());
        int userCnt = oldUserCnt + newUserCnt;
        double oldUserRatio = Math.round (oldUserCnt * 100.0) / 100.0;
        double newUserRatio = Math.round (newUserCnt * 100.0) / 100.0;
        ShortLinkStatsUvRespDTO oldUser = ShortLinkStatsUvRespDTO.builder ()
                .uvType ("oldUser")
                .ratio (oldUserRatio)
                .cnt (oldUserCnt)
                .build ();
        ShortLinkStatsUvRespDTO newUser = ShortLinkStatsUvRespDTO.builder ()
                .uvType ("newUser")
                .ratio (newUserRatio)
                .cnt (newUserCnt)
                .build ();
        shortLinkUvRespDTOList.add (oldUser);
        shortLinkUvRespDTOList.add (newUser);
        /*
        访问设备类型详情
         */
        List<ShortLinkStatsDeviceRespDTO> shortLinkDeviceRespDTOList = new ArrayList<> ();
        List<LinkDeviceStatsDO> linkDeviceStatsDOList = linkDeviceStatsMapper.listDeviceStatsByShortLink (requestParam);
        int deviceCnt = linkDeviceStatsDOList.stream ()
                .mapToInt (LinkDeviceStatsDO::getCnt)
                .sum ();
        linkDeviceStatsDOList.forEach (each ->{
            double ratio = (double) each.getCnt () / deviceCnt;
            double actualRatio = Math.round (ratio * 100.0) / 100.0;
            ShortLinkStatsDeviceRespDTO build = ShortLinkStatsDeviceRespDTO.builder ()
                    .cnt (deviceCnt)
                    .ratio (actualRatio)
                    .device (each.getDevice ())
                    .build ();
            shortLinkDeviceRespDTOList.add (build);
        });
        /*
        访问网络类型详情
         */
        List<ShortLinkStatsNetworkRespDTO> shortLinkNetworkRespDTOList = new ArrayList<> ();
        List<LinkNetworkStatsDO> listNetworkStatsByShortLink = linkNetworkStatsMapper.listNetworkStatsByShortLink (requestParam);
        int networkCnt = listNetworkStatsByShortLink.stream ()
                .mapToInt (each -> Integer.parseInt (each.getCnt ().toString ()))
                .sum ();
        listNetworkStatsByShortLink.forEach (each ->{
            double ratio = (double) each.getCnt () / deviceCnt;
            double actualRatio = Math.round (ratio * 100.0) / 100.0;
            ShortLinkStatsNetworkRespDTO build = ShortLinkStatsNetworkRespDTO.builder ()
                    .cnt (each.getCnt ())
                    .ratio (actualRatio)
                    .network (each.getNetwork ())
                    .build ();
            shortLinkNetworkRespDTOList.add (build);
        });
        return ShortLinkStatsRespDTO.builder ()
                .daily (BeanUtil.copyToList (listStatsByShortLink,ShortLinkStatsAccessDailyRespDTO.class))
                .localeCnStats (linkStatsLocaleCnRespDTOList)
                .hourStats (hourStats)
                .topIpStats (shortLinkStatsTopIpRespDTOList)
                .weekdayStats (weekdayStats)
                .browserStats (shortLinkBrowserRespDTOList)
                .osStats (shortLinkOsRespDTOList)
                .uvTypeStats (shortLinkUvRespDTOList)
                .deviceStats (shortLinkDeviceRespDTOList)
                .networkStats (shortLinkNetworkRespDTOList)
                .build();
    }
}