package com.feng.shortlink.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.Week;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feng.shortlink.project.common.convention.exception.ClientException;
import com.feng.shortlink.project.common.convention.exception.ServiceException;
import com.feng.shortlink.project.common.enums.ValidDateTypeEnum;
import com.feng.shortlink.project.config.GotoDomainWhiteListConfiguration;
import com.feng.shortlink.project.dao.entity.*;
import com.feng.shortlink.project.dao.mapper.*;
import com.feng.shortlink.project.dto.biz.ShortLinkStatsRecordDTO;
import com.feng.shortlink.project.dto.request.ShortLinkPageReqDTO;
import com.feng.shortlink.project.dto.request.ShortLinkSaveReqDTO;
import com.feng.shortlink.project.dto.request.ShortLinkUpdatePvUvUipDO;
import com.feng.shortlink.project.dto.request.ShortLinkUpdateReqDTO;
import com.feng.shortlink.project.dto.response.ShortLinkGroupQueryRespDTO;
import com.feng.shortlink.project.dto.response.ShortLinkPageRespDTO;
import com.feng.shortlink.project.dto.response.ShortLinkSaveRespDTO;
import com.feng.shortlink.project.mq.producer.DelayShortLinkStatsProducer;
import com.feng.shortlink.project.service.LinkStatsTodayService;
import com.feng.shortlink.project.service.ShortLinkService;
import com.feng.shortlink.project.util.HashUtil;
import com.feng.shortlink.project.util.ShortLinkUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static com.feng.shortlink.project.common.constant.RedisCacheConstant.*;
import static com.feng.shortlink.project.common.constant.ShortLinkConstant.SHORT_LINK_LOCALE_STATS_URL;

/**
 * @author FENGXIN
 * @date 2024/9/29
 * @project feng-shortlink
 * @description 短链接业务实现
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class ShortLinkServiceImpl extends ServiceImpl<ShortLinkMapper, ShortLinkDO> implements ShortLinkService {
    
    private final RBloomFilter<String> linkUriCreateCachePenetrationBloomFilter;
    private final LinkGotoMapper linkGotoMapper;
    private final StringRedisTemplate stringRedisTemplate;
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
    private final LinkStatsTodayService linkStatsTodayService;
    private final DelayShortLinkStatsProducer delayShortLinkStatsProducer;
    private final GotoDomainWhiteListConfiguration gotoDomainWhiteListConfiguration;
    
    @Value ("${short-link.stats.locale.amap-key}")
    private String amapKey;
    @Value ("${short-link.domain}")
    private String shortLinkDomain;
    
    @Override
    public ShortLinkSaveRespDTO saveShortLink (ShortLinkSaveReqDTO requestParam) {
        // 校验原始链接是否可创建
        verificationWhitelist (requestParam.getOriginUrl ());
        // 生成短链接 一个originUrl可以有多个短链接 只是要求短链接不能重复
        String shortLinkSuffix = generateShortLink (requestParam);
        String fullLink = shortLinkDomain + "/" + shortLinkSuffix;
        // 设置插入数据实体
        ShortLinkDO savedLinkDO = ShortLinkDO.builder ()
                .domain (shortLinkDomain)
                .shortUri (shortLinkSuffix)
                .fullShortUrl (fullLink)
                .originUrl (requestParam.getOriginUrl ())
                .clickNum (0)
                .gid (requestParam.getGid ())
                .favicon (getFavicon (requestParam.getOriginUrl ()))
                .enableStatus (0)
                .createdType (requestParam.getCreatedType ())
                .validDateType (requestParam.getValidDateType ())
                .validDate (requestParam.getValidDate ())
                .describe (requestParam.getDescribe ())
                .totalPv (0)
                .totalUv (0)
                .totalUip (0)
                .delTime (0L)
                .build ();
        LinkGotoDO linkGotoDO = LinkGotoDO.builder ()
                .gid (requestParam.getGid ())
                .fullShortUrl (fullLink)
                .build ();
        try {
            baseMapper.insert (savedLinkDO);
            linkGotoMapper.insert (linkGotoDO);
        } catch (DuplicateKeyException e) {
            // TODO 为什么布隆过滤器判断不存在后还要查询数据库校验？
            // 防止数据库误判 在抛出此异常后查询数据库校验是否真的短链接冲突
            LambdaQueryWrapper<ShortLinkDO> lambdaQueryWrapper = new LambdaQueryWrapper<ShortLinkDO> ()
                    .eq (ShortLinkDO::getFullShortUrl , fullLink)
                    .eq (ShortLinkDO::getDelFlag , 0);
            // 如果真的冲突 抛异常
            if (baseMapper.selectOne (lambdaQueryWrapper) != null) {
                log.warn ("short link already exists, short link = {}" , savedLinkDO.getFullShortUrl ());
                throw new ServiceException ("短链接生成重复");
            }
        }
        // 不冲突 添加短链接进入布隆过滤器 并响应前端
        boolean add = linkUriCreateCachePenetrationBloomFilter.add (fullLink);
        log.info ("add short link = {}" , savedLinkDO.getFullShortUrl ());
        log.info ("bloom add: {}" , add );
        // 缓存预热
        stringRedisTemplate.opsForValue ()
                .set (  String.format (SHORTLINK_GOTO_KEY , fullLink)
                        ,requestParam.getOriginUrl ()
                        , ShortLinkUtil.getShortLinkValidTime (requestParam.getValidDate ())
                        ,TimeUnit.MILLISECONDS);
        return ShortLinkSaveRespDTO.builder ()
                .fullShortUrl (savedLinkDO.getFullShortUrl ())
                .gid (savedLinkDO.getGid ())
                .originUrl (savedLinkDO.getOriginUrl ())
                .build ();
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateShortLink (ShortLinkUpdateReqDTO requestParam) {
        // 校验原始链接是否可更新
        verificationWhitelist (requestParam.getOriginUrl ());
        // 查询db里的短链接
        LambdaQueryWrapper<ShortLinkDO> lambdaQueryWrapper = new LambdaQueryWrapper<ShortLinkDO> ()
                .eq (ShortLinkDO::getGid , requestParam.getOriginGid ())
                .eq (ShortLinkDO::getFullShortUrl , requestParam.getFullShortUrl ())
                .eq (ShortLinkDO::getEnableStatus , 0)
                .eq (ShortLinkDO::getDelFlag , 0);
        ShortLinkDO selectOne = baseMapper.selectOne (lambdaQueryWrapper);
        if (selectOne == null) {
            throw new ClientException ("短链接不存在此分组");
        }
        if (Objects.equals (selectOne.getGid () , requestParam.getGid ())) {
            // 设置更新或插入的短链接
            ShortLinkDO shortLinkDO = ShortLinkDO.builder ()
                    .domain (selectOne.getDomain ())
                    .shortUri (selectOne.getShortUri ())
                    .createdType (selectOne.getCreatedType ())
                    .originUrl (selectOne.getOriginUrl ())
                    .clickNum (selectOne.getClickNum ())
                    // 可更新的参数
                    .fullShortUrl (requestParam.getFullShortUrl ())
                    .gid (requestParam.getGid ())
                    .originUrl (requestParam.getOriginUrl ())
                    .favicon (requestParam.getFavicon ())
                    .describe (requestParam.getDescribe ())
                    .validDateType (requestParam.getValidDateType ())
                    .validDate (requestParam.getValidDate ())
                    .build ();
            // gid一致 说明在同一组 直接新增 gid用谁的都可以
            LambdaUpdateWrapper<ShortLinkDO> lambdaUpdateWrapper = new LambdaUpdateWrapper<ShortLinkDO>()
                    .eq (ShortLinkDO::getGid , requestParam.getGid ())
                    .eq (ShortLinkDO::getFullShortUrl , requestParam.getFullShortUrl ())
                    .eq (ShortLinkDO::getEnableStatus , 0)
                    .eq (ShortLinkDO::getDelFlag , 0)
                    // 如果是永久有效 则不设置有效期
                    .set (Objects.equals (requestParam.getValidDateType (),ValidDateTypeEnum.PERMANENT.getValue ()),ShortLinkDO::getValidDateType , null );
            baseMapper.update (shortLinkDO,lambdaUpdateWrapper);
            // 更新缓存的有效期
            stringRedisTemplate.opsForValue ()
                    .set (  String.format (SHORTLINK_GOTO_KEY , requestParam.getFullShortUrl ())
                            ,requestParam.getOriginUrl ()
                            , ShortLinkUtil.getShortLinkValidTime (requestParam.getValidDate ())
                            ,TimeUnit.MILLISECONDS);
        }else {
            // gid 不一致 说明需要换组 需要删除之前的短链接gid用selectOne的 再新增到新组里
            RReadWriteLock readWriteLock = redissonClient.getReadWriteLock(String.format(LOCK_GID_UPDATE_KEY, requestParam.getFullShortUrl()));
            RLock rLock = readWriteLock.writeLock();
            if (!rLock.tryLock()) {
                throw new ServiceException("短链接正在被访问，请稍后再试...");
            }
            try {
                // 删除原链接
                LambdaUpdateWrapper<ShortLinkDO> linkUpdateWrapper = Wrappers.lambdaUpdate(ShortLinkDO.class)
                        .eq(ShortLinkDO::getFullShortUrl, requestParam.getFullShortUrl())
                        .eq(ShortLinkDO::getGid, selectOne.getGid())
                        .eq(ShortLinkDO::getDelFlag, 0)
                        .eq(ShortLinkDO::getDelTime, 0L)
                        .eq(ShortLinkDO::getEnableStatus, 0);
                ShortLinkDO delShortLinkDO = ShortLinkDO.builder()
                        .delTime(System.currentTimeMillis())
                        .build();
                delShortLinkDO.setDelFlag(1);
                baseMapper.update(delShortLinkDO, linkUpdateWrapper);
                // 插入新链接
                ShortLinkDO shortLinkDO = ShortLinkDO.builder()
                        .domain(shortLinkDomain)
                        .originUrl(requestParam.getOriginUrl())
                        .gid(requestParam.getGid())
                        .validDateType(requestParam.getValidDateType())
                        .validDate(requestParam.getValidDate())
                        .describe(requestParam.getDescribe())
                        .createdType(selectOne.getCreatedType())
                        .shortUri(selectOne.getShortUri())
                        .enableStatus(selectOne.getEnableStatus())
                        .totalPv(selectOne.getTotalPv())
                        .totalUv(selectOne.getTotalUv())
                        .totalUip(selectOne.getTotalUip())
                        .fullShortUrl(selectOne.getFullShortUrl())
                        .favicon(getFavicon(requestParam.getOriginUrl()))
                        .delTime(0L)
                        .build();
                baseMapper.insert(shortLinkDO);
                
                // 更新today短链接数据
                LambdaQueryWrapper<LinkStatsTodayDO> statsTodayQueryWrapper = Wrappers.lambdaQuery(LinkStatsTodayDO.class)
                        .eq(LinkStatsTodayDO::getFullShortUrl, requestParam.getFullShortUrl())
                        .eq(LinkStatsTodayDO::getGid, selectOne.getGid())
                        .eq(LinkStatsTodayDO::getDelFlag, 0);
                List<LinkStatsTodayDO> linkStatsTodayDOList = linkStatsTodayMapper.selectList(statsTodayQueryWrapper);
                if (CollUtil.isNotEmpty(linkStatsTodayDOList)) {
                    linkStatsTodayMapper.deleteBatchIds(linkStatsTodayDOList.stream()
                            .map(LinkStatsTodayDO::getId)
                            .toList()
                    );
                    linkStatsTodayDOList.forEach(each -> each.setGid(requestParam.getGid()));
                    linkStatsTodayService.saveBatch(linkStatsTodayDOList);
                }
                
                // 更新goto表短链接数据
                LambdaQueryWrapper<LinkGotoDO> linkGotoQueryWrapper = Wrappers.lambdaQuery(LinkGotoDO.class)
                        .eq(LinkGotoDO::getFullShortUrl, requestParam.getFullShortUrl())
                        .eq(LinkGotoDO::getGid, selectOne.getGid());
                LinkGotoDO shortLinkGotoDO = linkGotoMapper.selectOne(linkGotoQueryWrapper);
                linkGotoMapper.deleteById(shortLinkGotoDO.getId());
                shortLinkGotoDO.setGid(requestParam.getGid());
                linkGotoMapper.insert(shortLinkGotoDO);
                
                // 更新监控统计短链接数据
                LambdaUpdateWrapper<LinkAccessStatsDO> linkAccessStatsUpdateWrapper = Wrappers.lambdaUpdate(LinkAccessStatsDO.class)
                        .eq(LinkAccessStatsDO::getFullShortUrl, requestParam.getFullShortUrl())
                        .eq(LinkAccessStatsDO::getGid, selectOne.getGid())
                        .eq(LinkAccessStatsDO::getDelFlag, 0);
                LinkAccessStatsDO linkAccessStatsDO = LinkAccessStatsDO.builder()
                        .gid(requestParam.getGid())
                        .build();
                linkAccessStatsMapper.update(linkAccessStatsDO, linkAccessStatsUpdateWrapper);
                
                // 更新地区统计短链接数据
                LambdaUpdateWrapper<LinkLocaleStatsDO> linkLocaleStatsUpdateWrapper = Wrappers.lambdaUpdate(LinkLocaleStatsDO.class)
                        .eq(LinkLocaleStatsDO::getFullShortUrl, requestParam.getFullShortUrl())
                        .eq(LinkLocaleStatsDO::getGid, selectOne.getGid())
                        .eq(LinkLocaleStatsDO::getDelFlag, 0);
                LinkLocaleStatsDO linkLocaleStatsDO = LinkLocaleStatsDO.builder()
                        .gid(requestParam.getGid())
                        .build();
                linkLocaleStatsMapper.update(linkLocaleStatsDO, linkLocaleStatsUpdateWrapper);
                
                // 更新os短链接数据
                LambdaUpdateWrapper<LinkOsStatsDO> linkOsStatsUpdateWrapper = Wrappers.lambdaUpdate(LinkOsStatsDO.class)
                        .eq(LinkOsStatsDO::getFullShortUrl, requestParam.getFullShortUrl())
                        .eq(LinkOsStatsDO::getGid, selectOne.getGid())
                        .eq(LinkOsStatsDO::getDelFlag, 0);
                LinkOsStatsDO linkOsStatsDO = LinkOsStatsDO.builder()
                        .gid(requestParam.getGid())
                        .build();
                linkOsStatsMapper.update(linkOsStatsDO, linkOsStatsUpdateWrapper);
                
                // 更新browser短链接数据
                LambdaUpdateWrapper<LinkBrowserStatsDO> linkBrowserStatsUpdateWrapper = Wrappers.lambdaUpdate(LinkBrowserStatsDO.class)
                        .eq(LinkBrowserStatsDO::getFullShortUrl, requestParam.getFullShortUrl())
                        .eq(LinkBrowserStatsDO::getGid, selectOne.getGid())
                        .eq(LinkBrowserStatsDO::getDelFlag, 0);
                LinkBrowserStatsDO linkBrowserStatsDO = LinkBrowserStatsDO.builder()
                        .gid(requestParam.getGid())
                        .build();
                linkBrowserStatsMapper.update(linkBrowserStatsDO, linkBrowserStatsUpdateWrapper);
                
                // 更新device短链接数据
                LambdaUpdateWrapper<LinkDeviceStatsDO> linkDeviceStatsUpdateWrapper = Wrappers.lambdaUpdate(LinkDeviceStatsDO.class)
                        .eq(LinkDeviceStatsDO::getFullShortUrl, requestParam.getFullShortUrl())
                        .eq(LinkDeviceStatsDO::getGid, selectOne.getGid())
                        .eq(LinkDeviceStatsDO::getDelFlag, 0);
                LinkDeviceStatsDO linkDeviceStatsDO = LinkDeviceStatsDO.builder()
                        .gid(requestParam.getGid())
                        .build();
                linkDeviceStatsMapper.update(linkDeviceStatsDO, linkDeviceStatsUpdateWrapper);
                
                // 更新network短链接数据
                LambdaUpdateWrapper<LinkNetworkStatsDO> linkNetworkStatsUpdateWrapper = Wrappers.lambdaUpdate(LinkNetworkStatsDO.class)
                        .eq(LinkNetworkStatsDO::getFullShortUrl, requestParam.getFullShortUrl())
                        .eq(LinkNetworkStatsDO::getGid, selectOne.getGid())
                        .eq(LinkNetworkStatsDO::getDelFlag, 0);
                LinkNetworkStatsDO linkNetworkStatsDO = LinkNetworkStatsDO.builder()
                        .gid(requestParam.getGid())
                        .build();
                linkNetworkStatsMapper.update(linkNetworkStatsDO, linkNetworkStatsUpdateWrapper);
                
                // 更新日志短链接数据
                LambdaUpdateWrapper<LinkAccessLogsDO> linkAccessLogsUpdateWrapper = Wrappers.lambdaUpdate(LinkAccessLogsDO.class)
                        .eq(LinkAccessLogsDO::getFullShortUrl, requestParam.getFullShortUrl())
                        .eq(LinkAccessLogsDO::getGid, selectOne.getGid())
                        .eq(LinkAccessLogsDO::getDelFlag, 0);
                LinkAccessLogsDO linkAccessLogsDO = LinkAccessLogsDO.builder()
                        .gid(requestParam.getGid())
                        .build();
                linkAccessLogsMapper.update(linkAccessLogsDO, linkAccessLogsUpdateWrapper);
            } finally {
                rLock.unlock();
            }
            // 更新缓存的有效期
            stringRedisTemplate.opsForValue ()
                    .set (  String.format (SHORTLINK_GOTO_KEY , requestParam.getFullShortUrl ())
                            ,requestParam.getOriginUrl ()
                            , ShortLinkUtil.getShortLinkValidTime (requestParam.getValidDate ())
                            ,TimeUnit.MILLISECONDS);
        }
    }
    
    @Override
    public void restoreLink (String shortLink , HttpServletRequest request , HttpServletResponse response) {
        // 获取服务名 如baidu.com
        String serverName = request.getServerName ();
        // 获取端口
        String serverPort = Optional.of (request.getServerPort ())
                .filter (each -> !Objects.equals (each,80))
                .map(String::valueOf)
                .map(each -> ":" + each)
                .orElse ("");
        String fullLink = serverName + serverPort + "/" + shortLink;
        // 查询缓存的link
        String originalLink = stringRedisTemplate.opsForValue ().get (String.format (SHORTLINK_GOTO_KEY , fullLink));
        // 如果缓存有数据直接返回
        if (StringUtils.isNotBlank (originalLink)) {
            ShortLinkStatsRecordDTO statsRecord = buildLinkStatsRecordAndSetUser(fullLink, request, response);
            shortLinkStats(fullLink, null, statsRecord);
            // 返回重定向链接
            try {
                // 重定向
                response.sendRedirect (originalLink);
            } catch (IOException e) {
                throw new ClientException ("短链接重定向失败");
            }
            return;
        }
        // 如果缓存没有数据 查询布隆过滤器（短链接存入数据库是就添加入了布隆过滤器）
        boolean contains = linkUriCreateCachePenetrationBloomFilter.contains (fullLink);
        // 布隆过滤器不存在 则数据库也没有数据 直接返回
        if (!contains) {
            try {
                response.sendRedirect ("/page/notfound");
            } catch (IOException e) {
                throw new ClientException ("重定向不存在页面失败");
            }
            return;
        }
        // 布隆过滤器存在值 判断缓存是否有link空值
        String linkIsNull = stringRedisTemplate.opsForValue ().get (String.format (SHORTLINK_ISNULL_GOTO_KEY , fullLink));
        if (StringUtils.isNotBlank (linkIsNull)) {
            try {
                response.sendRedirect ("/page/notfound");
            } catch (IOException e) {
                throw new ClientException ("重定向不存在页面失败");
            }
            return;
        }
        // 缓存没有空值
        //如果缓存数据过期 获取分布式🔒查询数据库
        RLock lock = redissonClient.getLock (String.format (LOCK_SHORTLINK_GOTO_KEY , fullLink));
        lock.lock ();
        try {
            // 双重判断缓存数据 如果上一个线程已经在缓存设置新数据 可直接返回
            // 查询缓存的link
            originalLink = stringRedisTemplate.opsForValue ().get (String.format (SHORTLINK_GOTO_KEY , fullLink));
            // 如果缓存有数据直接返回
            if (StringUtils.isNotBlank (originalLink)) {
                ShortLinkStatsRecordDTO statsRecord = buildLinkStatsRecordAndSetUser(fullLink, request, response);
                shortLinkStats(fullLink, null, statsRecord);
                // 返回重定向链接
                try {
                    // 重定向
                    response.sendRedirect (originalLink);
                } catch (IOException e) {
                    throw new ClientException ("短链接重定向失败");
                }
            }
            // 查询路由表中的短链接（短链接做分片键 因为短链接表用gid分片键 不能直接根据完整短链接快速查询结果）
            LambdaQueryWrapper<LinkGotoDO> linkGotoDoLambdaQueryWrapper = new LambdaQueryWrapper<LinkGotoDO> ()
                    .eq (LinkGotoDO::getFullShortUrl , fullLink);
            LinkGotoDO linkGotoDO = linkGotoMapper.selectOne (linkGotoDoLambdaQueryWrapper);
            if (linkGotoDO == null) {
                // 设置空值 直接返回 该链接在数据库是不存在值的 但是布隆过滤器没有删除值
                stringRedisTemplate.opsForValue ().set (String.format (SHORTLINK_ISNULL_GOTO_KEY , fullLink), "-",30, TimeUnit.SECONDS);
                // 严谨 需要进行风控
                try {
                    response.sendRedirect ("/page/notfound");
                } catch (IOException e) {
                    throw new ClientException ("重定向不存在页面失败");
                }
                return;
            }
            // 使用路由表的gid快速查询短链接表的数据
            LambdaQueryWrapper<ShortLinkDO> shortLinkDoLambdaQueryWrapper = new LambdaQueryWrapper<ShortLinkDO> ()
                    .eq (ShortLinkDO::getGid , linkGotoDO.getGid ())
                    .eq (ShortLinkDO::getFullShortUrl , fullLink)
                    .eq (ShortLinkDO::getEnableStatus , 0)
                    .eq (ShortLinkDO::getDelFlag , 0);
            ShortLinkDO shortLinkDO = baseMapper.selectOne (shortLinkDoLambdaQueryWrapper);
            if (shortLinkDO == null || shortLinkDO.getValidDate () != null && shortLinkDO.getValidDate ().before (new Date ())) {
                // 如果数据库的链接过期
                stringRedisTemplate.opsForValue ().set (String.format (SHORTLINK_ISNULL_GOTO_KEY , fullLink), "-",30, TimeUnit.SECONDS);
                // 严谨 需要进行风控
                try {
                    response.sendRedirect ("/page/notfound");
                } catch (IOException e) {
                    throw new ClientException ("重定向不存在页面失败");
                }
                return;
            }
            ShortLinkStatsRecordDTO statsRecord = buildLinkStatsRecordAndSetUser(fullLink, request, response);
            shortLinkStats(fullLink, shortLinkDO.getGid (), statsRecord);
            // 返回重定向链接
            try {
                // 设置缓存新数据
                stringRedisTemplate.opsForValue ()
                        .set (  String.format (SHORTLINK_GOTO_KEY , shortLinkDO.getFullShortUrl ())
                                ,shortLinkDO.getOriginUrl ()
                                , ShortLinkUtil.getShortLinkValidTime (shortLinkDO.getValidDate ())
                                ,TimeUnit.MILLISECONDS);
                // 重定向
                response.sendRedirect (shortLinkDO.getOriginUrl ());
            } catch (IOException e) {
                throw new ClientException ("短链接重定向失败");
            }
        } finally {
            lock.unlock ();
        }
    }
    
    private ShortLinkStatsRecordDTO buildLinkStatsRecordAndSetUser(String fullShortLink,  HttpServletRequest request , HttpServletResponse response) {
        AtomicBoolean uvFlag = new AtomicBoolean ();
        Cookie[] cookies = request.getCookies ();
        AtomicReference<String> uv = new AtomicReference<> ();
        // 添加cookie进入响应 并设置缓存用于校验下次访问是否已经存在
        Runnable generateCookieTask = () ->{
            // 设置响应cookie
            uv.set (UUID.fastUUID ().toString ());
            Cookie cookie = new Cookie ("uv",uv.get ());
            // cookie设置为30天
            cookie.setMaxAge (60 * 60 * 24 * 30);
            // 设置路径 只有当前短链接后缀访问时才携带cookie（不过也不影响 默认是当前路径及其子路径）
            cookie.setPath (StrUtil.sub (fullShortLink,fullShortLink.indexOf ("/"),fullShortLink.length ()));
            response.addCookie (cookie);
            uvFlag.set (Boolean.TRUE);
            // 设置到当天的有效期
            stringRedisTemplate.opsForSet ().add (String.format (SHORTLINK_STATS_UV_KEY , fullShortLink) , uv.get ());
            stringRedisTemplate.expire (String.format (SHORTLINK_STATS_UV_KEY , fullShortLink),millisecondsUntilEndOfDay(), TimeUnit.MILLISECONDS);
        };
        // 首先判断请求是否已经含有用户cookie
        if(ArrayUtil.isNotEmpty (cookies)) {
            // 已经拥有 设置缓存 并设置uv添加标志 使uv不叠加
            Arrays.stream (cookies)
                    .filter (each -> Objects.equals (each.getName (),"uv"))
                    .findFirst ()
                    .map (Cookie::getValue)
                    .ifPresentOrElse (each ->{
                        // 设置uv 方便后续使用
                        uv.set(each);
                        // 如果缓存有cookie 说明在当天该用户是同一个 uv不能叠加 如果cookie不存在缓存则需要叠加（此时是第二天）
                        Long uvAdd = stringRedisTemplate.opsForSet ().add (String.format (SHORTLINK_STATS_UV_KEY , fullShortLink) , each);
                        uvFlag.set (uvAdd != null && uvAdd > 0L);
                        // 设置到当天的有效期
                        if (uvFlag.get () == Boolean.TRUE) {
                            stringRedisTemplate.expire (String.format (SHORTLINK_STATS_UV_KEY , fullShortLink),millisecondsUntilEndOfDay(), TimeUnit.MILLISECONDS);
                        }
                    },generateCookieTask);
        }else {
            // 没有cookie 第一次访问短链接 创建cookie并设置响应
            generateCookieTask.run ();
        }
        // 设置uip
        String userIpAddress = ShortLinkUtil.getUserIpAddress (request);
        String os = ShortLinkUtil.getOperatingSystem (request);
        String browser = ShortLinkUtil.getBrowser (request);
        String device = ShortLinkUtil.getDevice (request);
        String network = ShortLinkUtil.getUserNetwork (request);
        Long uipAdd = stringRedisTemplate.opsForSet ().add (String.format (SHORTLINK_STATS_UIP_KEY , fullShortLink) , userIpAddress);
        boolean uipFlag = uipAdd != null && uipAdd > 0L;
        if(uipFlag == Boolean.TRUE) {
            // 设置到当天的有效期
            stringRedisTemplate.expire (String.format (SHORTLINK_STATS_UIP_KEY , fullShortLink) , millisecondsUntilEndOfDay () , TimeUnit.MILLISECONDS);
        }
        return ShortLinkStatsRecordDTO.builder()
                .fullShortLink(fullShortLink)
                .uv(uv.get())
                .uvFlag(uvFlag.get())
                .uipFlag(uipFlag)
                .userIpAddress(userIpAddress)
                .os(os)
                .browser(browser)
                .device(device)
                .network(network)
                .build();
    }
    
    /**
     * 链接统计
     *
     * @param gid           GID
     * @param fullShortLink 完整短链接
     */
    @Override
     public void shortLinkStats (String fullShortLink,String gid, ShortLinkStatsRecordDTO statsRecord) {
        fullShortLink = Optional.ofNullable(fullShortLink).orElse(statsRecord.getFullShortLink ());
        RReadWriteLock readWriteLock = redissonClient.getReadWriteLock(String.format(LOCK_GID_UPDATE_KEY, fullShortLink));
        RLock rLock = readWriteLock.readLock();
        if (!rLock.tryLock()) {
            delayShortLinkStatsProducer.send(statsRecord);
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
            Map<String,Object> localParamMap = new HashMap<>();
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
    
    @Override
    public IPage<ShortLinkPageRespDTO> pageShortLink (ShortLinkPageReqDTO requestParam) {
        IPage<ShortLinkDO> resultPage = baseMapper.pageLink (requestParam);
        return resultPage.convert (each -> {
            ShortLinkPageRespDTO result = BeanUtil.copyProperties (each , ShortLinkPageRespDTO.class);
            result.setDomain ("http://" + result.getDomain ());
            return result;
        });
    }

    @Override
    public List<ShortLinkGroupQueryRespDTO> listShortLinkGroup (List<String> requestParam) {
        QueryWrapper<ShortLinkDO> queryWrapper = new QueryWrapper<ShortLinkDO> ()
                .select ("gid as gid","COUNT(*) AS groupCount")
                .eq ("enable_status",0)
                .eq("del_flag", 0)
                .eq("del_time", 0L)
                .in ("gid",requestParam)
                .groupBy ("gid");
        List<Map<String, Object>> listLinkGroup = baseMapper.selectMaps (queryWrapper);
        return BeanUtil.copyToList (listLinkGroup, ShortLinkGroupQueryRespDTO.class);
    }
    
    /**
     * 将请求参数中的原始 URL 转换为短链接。
     *
     * @param requestParam 包含原始 URL 和其他元数据的请求参数
     * @return 以 base62 格式生成的短链接
     */
    public String generateShortLink (ShortLinkSaveReqDTO requestParam) {
        int generatingCount = 0;
        String originUrl = requestParam.getOriginUrl ();
        while (true) {
            // 防止死循环 无限生成（高并发下许多用户生成的link可能一直冲突）
            if (generatingCount > 10) {
                throw new ServiceException ("短链接创建频繁，请稍后再试");
            }
            String shortLink = HashUtil.hashToBase62 (originUrl);
            // 布隆过滤器不存在直接返回结果
            if (!linkUriCreateCachePenetrationBloomFilter.contains (shortLinkDomain + "/" + shortLink)) {
                return shortLink;
            }
            // 避免重复生成 加上时间毫秒下一次重新生成 不影响实际url
            originUrl += System.currentTimeMillis ();
            generatingCount++;
        }
    }
    
    /**
     * 获取网站图标
     *
     * @param url 网址
     * @return {@code String }
     */
    public String getFavicon(String url) {
        try {
            // 通过Jsoup连接到指定的URL并解析HTML文档
            Document document = Jsoup.connect(url)
                    // 设置超时时间
                    .timeout(5000)
                    .get();
            // 尝试查找<link>标签中包含favicon的元素
            Element iconElement = document.select("link[rel~=(icon|shortcut icon)]").first();
            if (iconElement != null) {
                String iconUrl = iconElement.attr("href");
                return resolveUrl(url, iconUrl);
            } else {
                return "未找到网站图标";
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    
    /**
     * 解析相对URL为绝对URL
     *
     * @param baseUrl 基本 URL
     * @param iconUrl 图标路径
     * @return {@code String 绝对路径}
     */
    private String resolveUrl(String baseUrl, String iconUrl) {
        if (iconUrl.startsWith("http://") || iconUrl.startsWith("https://")) {
            // 如果是绝对路径，直接返回
            return iconUrl;
        } else {
            // 如果是相对路径，拼接成绝对路径
            // 根据需要，可以使用URL的解析方法
            return baseUrl + iconUrl;
        }
    }
    
    /**
     * 距离一天结束毫秒数
     *
     * @return long
     */
    public static Long millisecondsUntilEndOfDay() {
        // 获取当前时间
        long now = System.currentTimeMillis();
        // 获取今天结束的时间
        long endOfDay = DateUtil.endOfDay(new Date ()).getTime();
        // 计算剩余毫秒数
        return Long.valueOf (String.valueOf (endOfDay - now));
    }
    
    /**
     * 验证原始链接白名单
     *
     * @param originUrl 源 URL
     */
    private void verificationWhitelist(String originUrl) {
        Boolean enable = gotoDomainWhiteListConfiguration.getEnable();
        // 配置没开
        if (enable == null || !enable) {
            return;
        }
        String domain = ShortLinkUtil.extractDomain(originUrl);
        if (StrUtil.isBlank(domain)) {
            throw new ClientException("跳转链接填写错误");
        }
        List<String> details = gotoDomainWhiteListConfiguration.getDetails();
        if (!details.contains(domain)) {
            throw new ClientException("演示环境为避免恶意攻击，请生成以下网站跳转链接：" + gotoDomainWhiteListConfiguration.getNames());
        }
    }
}
