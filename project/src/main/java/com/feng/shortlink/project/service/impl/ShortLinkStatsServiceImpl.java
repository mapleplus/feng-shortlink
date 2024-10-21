package com.feng.shortlink.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.feng.shortlink.project.common.biz.user.UserContext;
import com.feng.shortlink.project.common.convention.exception.ServiceException;
import com.feng.shortlink.project.dao.entity.*;
import com.feng.shortlink.project.dao.mapper.*;
import com.feng.shortlink.project.dto.request.ShortLinkPageStatsGroupReqDTO;
import com.feng.shortlink.project.dto.request.ShortLinkPageStatsReqDTO;
import com.feng.shortlink.project.dto.request.ShortLinkStatsGroupReqDTO;
import com.feng.shortlink.project.dto.request.ShortLinkStatsReqDTO;
import com.feng.shortlink.project.dto.response.*;
import com.feng.shortlink.project.service.ShortLinkStatsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author FENGXIN
 * @date 2024/10/5
 * @project feng-shortlink
 * @description
 **/
@Slf4j
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
    private final LinkGroupMapper linkGroupMapper;
    private final LinkGotoMapper linkGotoMapper;
    
    @Override
    public ShortLinkStatsRespDTO getShortLinkStats (ShortLinkStatsReqDTO requestParam) {
        checkGroupBelongToUser(requestParam.getGid ());
        /*
        基础监控统计
         */
        List<LinkAccessStatsDO> listStatsByShortLink = linkAccessStatsMapper.listStatsByShortLink (requestParam);
        /*
        PVC UV UIP
         */
        AtomicInteger pv = new AtomicInteger();
        AtomicInteger uv = new AtomicInteger();
        AtomicInteger uip = new AtomicInteger();
        listStatsByShortLink.forEach (each ->{
            pv.set (pv.get ()+each.getPv ());
            uv.set (uv.get ()+each.getUv ());
            uip.set (uip.get ()+each.getUip ());
        });
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
            double ratio = (double) Integer.parseInt (each.get ("count").toString ()) / osCnt;
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
        double oldUserRatio = (double) oldUserCnt / userCnt;
        double newUserRatio = (double) newUserCnt / userCnt;
        double actualOldUserRatio = Math.round (oldUserRatio * 100.0) / 100.0;
        double actualNewUserRatio = Math.round (newUserRatio * 100.0) / 100.0;
        ShortLinkStatsUvRespDTO oldUser = ShortLinkStatsUvRespDTO.builder ()
                .uvType ("oldUser")
                .ratio (actualOldUserRatio)
                .cnt (oldUserCnt)
                .build ();
        ShortLinkStatsUvRespDTO newUser = ShortLinkStatsUvRespDTO.builder ()
                .uvType ("newUser")
                .ratio (actualNewUserRatio)
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
                    .cnt (each.getCnt ())
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
            double ratio = (double) each.getCnt () / networkCnt;
            double actualRatio = Math.round (ratio * 100.0) / 100.0;
            ShortLinkStatsNetworkRespDTO build = ShortLinkStatsNetworkRespDTO.builder ()
                    .cnt (each.getCnt ())
                    .ratio (actualRatio)
                    .network (each.getNetwork ())
                    .build ();
            shortLinkNetworkRespDTOList.add (build);
        });
        return ShortLinkStatsRespDTO.builder ()
                .pv (pv.get ())
                .uv (uv.get ())
                .uip (uip.get ())
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
    
    @Override
    public IPage<ShortLinkPageStatsRespDTO> pageShortLinkStats (ShortLinkPageStatsReqDTO requestParam) {
        checkGroupBelongToUser(requestParam.getGid ());
        // 查询要请求的数据
        LambdaQueryWrapper<LinkAccessLogsDO> lambdaQueryWrapper = new LambdaQueryWrapper<LinkAccessLogsDO> ()
                .eq (LinkAccessLogsDO::getFullShortUrl, requestParam.getFullShortUrl ())
                .eq (LinkAccessLogsDO::getDelFlag,0)
                .between (LinkAccessLogsDO::getCreateTime,requestParam.getStartDate (),requestParam.getEndDate () )
                .orderByAsc (LinkAccessLogsDO::getCreateTime );
        IPage<LinkAccessLogsDO> pageStatsReqDTO = linkAccessLogsMapper.selectPage (requestParam , lambdaQueryWrapper);
        if (CollUtil.isEmpty (pageStatsReqDTO.getRecords ())) {
            return new Page<> ();
        }
        
        // convert成响应对象
        IPage<ShortLinkPageStatsRespDTO> result = pageStatsReqDTO.convert (each -> BeanUtil.toBean (each , ShortLinkPageStatsRespDTO.class));
        // 设置db查询参数 设置uvType
        List<String> userAccessLogsList = result.getRecords ().stream ()
                .map (ShortLinkPageStatsRespDTO::getUser)
                .toList ();
        if (CollectionUtil.isEmpty(userAccessLogsList)) {
            return result;
        }
        LinkPageStatsDO logMapperRequestParam = LinkPageStatsDO.builder ()
                .fullShortUrl (requestParam.getFullShortUrl ())
                .gid (requestParam.getGid ())
                .startDate (requestParam.getStartDate ())
                .endDate (requestParam.getEndDate ())
                .userAccessLogsList (userAccessLogsList)
                .build ();
        List<HashMap<String, Object>> uvTypeList = linkAccessLogsMapper.listAccessRecordByShortLink (logMapperRequestParam);
        // 设置uvType
        result.getRecords ().forEach (each ->{
            String uvType = uvTypeList.stream ()
                    .filter (item -> Objects.equals (each.getUser (),item.get("user")))
                    .findFirst ()
                    .map(item -> item.get("uvType"))
                    .orElse ("老访客")
                    .toString ();
            each.setUvType (uvType);
        });
        return result;
    }
    
    @Override
    public ShortLinkStatsGroupRespDTO groupShortLinkStats (ShortLinkStatsGroupReqDTO requestParam) {
        checkGroupBelongToUser(requestParam.getGid ());
        /*
        基础监控统计
         */
        List<LinkAccessStatsDO> listStatsByShortLinkGroup = linkAccessStatsMapper.listStatsByShortLinkGroup (requestParam);
        /*
        PVC UV UIP
         */
        AtomicInteger pv = new AtomicInteger();
        AtomicInteger uv = new AtomicInteger();
        AtomicInteger uip = new AtomicInteger();
        listStatsByShortLinkGroup.forEach (each ->{
            pv.set (pv.get ()+each.getPv ());
            uv.set (uv.get ()+each.getUv ());
            uip.set (uip.get ()+each.getUip ());
        });
        /*
        地区监控统计
         */
        List<ShortLinkStatsLocaleCNRespDTO> linkStatsLocaleCnRespDTOList = new ArrayList<> ();
        List<LinkLocaleStatsDO> listLocaleByShortLinkGroup = linkLocaleStatsMapper.listLocaleByShortLinkGroup (requestParam);
        // 统计所有地区访问数量
        int localeStatsCnSum = listLocaleByShortLinkGroup
                .stream ()
                .mapToInt (LinkLocaleStatsDO::getCnt)
                .sum ();
        // 统计百分比 并赋值响应体
        listLocaleByShortLinkGroup
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
        List<LinkAccessStatsDO> listHourStatsByShortLinkGroup = linkAccessStatsMapper.listHourStatsByShortLinkGroup (requestParam);
        for (int i = 0 ; i < 24 ; i++) {
            // 统计该条短链接每小时的访问量 没有就设置0
            AtomicInteger hour = new AtomicInteger (i);
            int hourPv = listHourStatsByShortLinkGroup.stream ()
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
        linkAccessLogsMapper.listTopIpByShortLinkGroup (requestParam)
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
        List<LinkAccessStatsDO> listWeekdayStatsByShortLinkGroup = linkAccessStatsMapper.listWeekdayStatsByShortLinkGroup (requestParam);
        for (int i = 1 ; i < 8 ; i++) {
            AtomicInteger weekday = new AtomicInteger (i);
            Integer weekdayCnt = listWeekdayStatsByShortLinkGroup
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
        List<HashMap<String, Object>> listBrowserStatsByShortLinkGroup = linkBrowserStatsMapper.listBrowserStatsByShortLinkGroup (requestParam);
        // 总浏览器数
        int browserCnt = listBrowserStatsByShortLinkGroup.stream ()
                .mapToInt (each -> Integer.parseInt (each.get ("count").toString ()))
                .sum ();
        listBrowserStatsByShortLinkGroup.forEach (each ->{
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
        List<HashMap<String, Object>> listOsStatsByShortLinkGroup = linkOsStatsMapper.listOsStatsByShortLinkGroup (requestParam);
        int osCnt = listOsStatsByShortLinkGroup.stream ()
                .mapToInt (each -> Integer.parseInt (each.get("count").toString ()))
                .sum ();
        listOsStatsByShortLinkGroup.forEach (each ->{
            double ratio = (double) Integer.parseInt (each.get ("count").toString ()) / osCnt;
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
        HashMap<String, Object> uvTypeCntByShortLinkGroup = linkAccessLogsMapper.findUvTypeCntByShortLinkGroup (requestParam);
        int oldUserCnt = Integer.parseInt (uvTypeCntByShortLinkGroup.get ("oldUserCnt").toString ());
        int newUserCnt = Integer.parseInt (uvTypeCntByShortLinkGroup.get ("newUserCnt").toString ());
        int userCnt = oldUserCnt + newUserCnt;
        double oldUserRatio = (double) oldUserCnt / userCnt;
        double newUserRatio = (double) newUserCnt / userCnt;
        double actualOldUserRatio = Math.round (oldUserRatio * 100.0) / 100.0;
        double actualNewUserRatio = Math.round (newUserRatio * 100.0) / 100.0;
        ShortLinkStatsUvRespDTO oldUser = ShortLinkStatsUvRespDTO.builder ()
                .uvType ("oldUser")
                .ratio (actualOldUserRatio)
                .cnt (oldUserCnt)
                .build ();
        ShortLinkStatsUvRespDTO newUser = ShortLinkStatsUvRespDTO.builder ()
                .uvType ("newUser")
                .ratio (actualNewUserRatio)
                .cnt (newUserCnt)
                .build ();
        shortLinkUvRespDTOList.add (oldUser);
        shortLinkUvRespDTOList.add (newUser);
        /*
        访问设备类型详情
         */
        List<ShortLinkStatsDeviceRespDTO> shortLinkDeviceRespDTOList = new ArrayList<> ();
        List<LinkDeviceStatsDO> listDeviceStatsByShortLinkGroup = linkDeviceStatsMapper.listDeviceStatsByShortLinkGroup (requestParam);
        int deviceCnt = listDeviceStatsByShortLinkGroup.stream ()
                .mapToInt (LinkDeviceStatsDO::getCnt)
                .sum ();
        listDeviceStatsByShortLinkGroup.forEach (each ->{
            double ratio = (double) each.getCnt () / deviceCnt;
            double actualRatio = Math.round (ratio * 100.0) / 100.0;
            ShortLinkStatsDeviceRespDTO build = ShortLinkStatsDeviceRespDTO.builder ()
                    .cnt (each.getCnt ())
                    .ratio (actualRatio)
                    .device (each.getDevice ())
                    .build ();
            shortLinkDeviceRespDTOList.add (build);
        });
        /*
        访问网络类型详情
         */
        List<ShortLinkStatsNetworkRespDTO> shortLinkNetworkRespDTOList = new ArrayList<> ();
        List<LinkNetworkStatsDO> listNetworkStatsByShortLinkGroup = linkNetworkStatsMapper.listNetworkStatsByShortLinkGroup (requestParam);
        int networkCnt = listNetworkStatsByShortLinkGroup.stream ()
                .mapToInt (each -> Integer.parseInt (each.getCnt ().toString ()))
                .sum ();
        listNetworkStatsByShortLinkGroup.forEach (each ->{
            double ratio = (double) each.getCnt () / networkCnt;
            double actualRatio = Math.round (ratio * 100.0) / 100.0;
            ShortLinkStatsNetworkRespDTO build = ShortLinkStatsNetworkRespDTO.builder ()
                    .cnt (each.getCnt ())
                    .ratio (actualRatio)
                    .network (each.getNetwork ())
                    .build ();
            shortLinkNetworkRespDTOList.add (build);
        });
        return ShortLinkStatsGroupRespDTO.builder ()
                .pv (pv.get ())
                .uv (uv.get ())
                .uip (uip.get ())
                .daily (BeanUtil.copyToList (listStatsByShortLinkGroup,ShortLinkStatsAccessDailyRespDTO.class))
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
    
    @Override
    public IPage<ShortLinkPageStatsGroupRespDTO> pageGroupShortLinkStats (ShortLinkPageStatsGroupReqDTO requestParam) {
        checkGroupBelongToUser(requestParam.getGid ());
        LambdaQueryWrapper<LinkGotoDO> linkGotoDoLambdaQueryWrapper = new LambdaQueryWrapper<LinkGotoDO> ()
                .eq (LinkGotoDO::getGid,requestParam.getGid ());
        List<LinkGotoDO> linkGotoDOList = linkGotoMapper.selectList (linkGotoDoLambdaQueryWrapper);
        List<String> fullLinkList = linkGotoDOList.stream ().map (LinkGotoDO::getFullShortUrl).toList ();
        // 查询要请求的数据
        LambdaQueryWrapper<LinkAccessLogsDO> lambdaQueryWrapper = new LambdaQueryWrapper<LinkAccessLogsDO> ()
                .eq (LinkAccessLogsDO::getDelFlag,0)
                .in (LinkAccessLogsDO::getFullShortUrl,fullLinkList)
                .between (LinkAccessLogsDO::getCreateTime,requestParam.getStartDate (),requestParam.getEndDate ())
                .orderByAsc (LinkAccessLogsDO::getCreateTime );
        IPage<LinkAccessLogsDO> pageStatsReqDTO = linkAccessLogsMapper.selectPage (requestParam , lambdaQueryWrapper);
        if (CollUtil.isEmpty (pageStatsReqDTO.getRecords ())) {
            return new Page<> ();
        }
        // convert成响应对象
        IPage<ShortLinkPageStatsGroupRespDTO> result = pageStatsReqDTO.convert (each -> BeanUtil.toBean (each , ShortLinkPageStatsGroupRespDTO.class));
        // 设置db查询参数 设置uvType
        List<String> userAccessLogsList = result.getRecords ().stream ()
                .map (ShortLinkPageStatsGroupRespDTO::getUser)
                .toList ();
        if (CollectionUtil.isEmpty(userAccessLogsList)) {
            return result;
        }
        LinkPageStatsGroupDO logMapperRequestParam = LinkPageStatsGroupDO.builder ()
                .gid (requestParam.getGid ())
                .startDate (requestParam.getStartDate ())
                .endDate (requestParam.getEndDate ())
                .userAccessLogsList (userAccessLogsList)
                .build ();
        List<HashMap<String, Object>> uvTypeList = linkAccessLogsMapper.listGroupAccessRecordByShortLink (logMapperRequestParam);
        // 设置uvType
        result.getRecords ().forEach (each ->{
            String uvType = uvTypeList.stream ()
                    .filter (item -> Objects.equals (each.getUser (),item.get("user")))
                    .findFirst ()
                    .map(item -> item.get("uvType"))
                    .orElse ("老访客")
                    .toString ();
            each.setUvType (uvType);
        });
        return result;
    }
    
    public void checkGroupBelongToUser(String gid){
        String userName = Optional.ofNullable (UserContext.getUsername ())
                .orElseThrow (() -> new ServiceException ("用户未登录"));
        LambdaQueryWrapper<GroupDO> lambdaQueryWrapper = new LambdaQueryWrapper<GroupDO> ()
                .eq (GroupDO::getGid,gid)
                .eq(GroupDO::getUsername,userName);
        List<GroupDO> groupDoList = linkGroupMapper.selectList (lambdaQueryWrapper);
        if (CollUtil.isEmpty (groupDoList)) {
            throw new ServiceException ("用户信息和分组不匹配");
        }
    }
}
