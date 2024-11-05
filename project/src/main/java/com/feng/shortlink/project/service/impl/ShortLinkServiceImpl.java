package com.feng.shortlink.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feng.shortlink.project.common.convention.exception.ClientException;
import com.feng.shortlink.project.common.convention.exception.ServiceException;
import com.feng.shortlink.project.common.enums.ValidDateTypeEnum;
import com.feng.shortlink.project.config.GotoDomainWhiteListConfiguration;
import com.feng.shortlink.project.dao.entity.LinkGotoDO;
import com.feng.shortlink.project.dao.entity.ShortLinkDO;
import com.feng.shortlink.project.dao.mapper.LinkGotoMapper;
import com.feng.shortlink.project.dao.mapper.ShortLinkMapper;
import com.feng.shortlink.project.dto.biz.ShortLinkStatsMqToDbDTO;
import com.feng.shortlink.project.dto.biz.ShortLinkStatsRecordDTO;
import com.feng.shortlink.project.dto.request.ShortLinkPageReqDTO;
import com.feng.shortlink.project.dto.request.ShortLinkSaveReqDTO;
import com.feng.shortlink.project.dto.request.ShortLinkUpdateReqDTO;
import com.feng.shortlink.project.dto.response.ShortLinkGroupQueryRespDTO;
import com.feng.shortlink.project.dto.response.ShortLinkPageRespDTO;
import com.feng.shortlink.project.dto.response.ShortLinkSaveRespDTO;
import com.feng.shortlink.project.mq.producer.RocketMqMessageService;
import com.feng.shortlink.project.service.ShortLinkService;
import com.feng.shortlink.project.util.HashUtil;
import com.feng.shortlink.project.util.ShortLinkUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static com.feng.shortlink.project.common.constant.RedisCacheConstant.*;

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
    private final GotoDomainWhiteListConfiguration gotoDomainWhiteListConfiguration;
    private final RocketMqMessageService rocketMqMessageService;
    
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
            log.warn ("short link already exists, short link = {}" , savedLinkDO.getFullShortUrl ());
            // 判断布隆过滤器是否存在 防止布隆过滤器丢失而数据库新增成功
            if(!linkUriCreateCachePenetrationBloomFilter.contains (fullLink)){
                linkUriCreateCachePenetrationBloomFilter.add (fullLink);
            }
            throw new ServiceException ("短链接生成重复");
        }
        // 不冲突 添加短链接进入布隆过滤器 并响应前端
        linkUriCreateCachePenetrationBloomFilter.add (fullLink);
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
        // 处理有效期
        if (Objects.equals (requestParam.getValidDateType (),ValidDateTypeEnum.PERMANENT.getValue ())) {
            requestParam.setValidDate (null);
        }
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
                    .favicon (getFavicon(requestParam.getOriginUrl()))
                    .describe (requestParam.getDescribe ())
                    .validDateType (requestParam.getValidDateType ())
                    .validDate (requestParam.getValidDate ())
                    .build ();
            // gid一致 说明在同一组 直接新增 gid用谁的都可以
            LambdaUpdateWrapper<ShortLinkDO> lambdaUpdateWrapper = new LambdaUpdateWrapper<ShortLinkDO>()
                    .eq (ShortLinkDO::getGid , requestParam.getGid ())
                    .eq (ShortLinkDO::getFullShortUrl , requestParam.getFullShortUrl ())
                    .eq (ShortLinkDO::getEnableStatus , 0)
                    .eq (ShortLinkDO::getDelFlag , 0);
            baseMapper.update (shortLinkDO,lambdaUpdateWrapper);
            // 更新缓存的有效期
            stringRedisTemplate.opsForValue ()
                    .set (  String.format (SHORTLINK_GOTO_KEY , requestParam.getFullShortUrl ())
                            ,requestParam.getOriginUrl ()
                            , ShortLinkUtil.getShortLinkValidTime (requestParam.getValidDate ())
                            ,TimeUnit.MILLISECONDS);
        }else {
            // gid 不一致 说明需要换组 需要删除之前的短链接gid用selectOne的 再新增到新组里
            /*
            获取写锁 如果用户正在访问短链接 则读锁被占有 那么此链接将无法被修改
            如果写锁获取成功 那么读锁将无法被获取 但是用户正常重定向访问 只是使用延迟队列 延迟一会儿再统计链接访问数据，此时链接已经修改好 统计的就是最新的数据
             */
            RReadWriteLock readWriteLock = redissonClient.getReadWriteLock(String.format(LOCK_GID_UPDATE_KEY, requestParam.getFullShortUrl()));
            RLock rLock = readWriteLock.writeLock();
            // if (!rLock.tryLock()) {
            //     throw new ServiceException("短链接正在被访问，请稍后再试...");
            // }
            rLock.lock ();
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
                        .clickNum (selectOne.getClickNum())
                        .favicon(getFavicon(requestParam.getOriginUrl()))
                        .delTime (0L)
                        .build();
                baseMapper.insert(shortLinkDO);
                // 更新GOTO表
                LambdaQueryWrapper<LinkGotoDO> linkGotoDoLambdaQueryWrapper = new LambdaQueryWrapper<LinkGotoDO> ()
                        .eq (LinkGotoDO::getFullShortUrl,requestParam.getFullShortUrl ())
                        .eq (LinkGotoDO::getGid,selectOne.getGid ());
                LinkGotoDO linkGotoDO = linkGotoMapper.selectOne (linkGotoDoLambdaQueryWrapper);
                linkGotoDO.setGid (requestParam.getGid ());
                linkGotoMapper.delete (linkGotoDoLambdaQueryWrapper);
                linkGotoMapper.insert (linkGotoDO);
            } finally {
                rLock.unlock();
            }
            // 更新链接相关缓存
            // 如果新链接和旧链接的有效期或原始链接不一致，应该删除旧链接的缓存 确保下一次访问的时候正确重新设置缓存（方便）
            if (!Objects.equals (selectOne.getValidDateType (),requestParam.getValidDateType ()) ||
                !Objects.equals (selectOne.getValidDate (),requestParam.getValidDate ()) ||
                !Objects.equals (selectOne.getOriginUrl (),requestParam.getOriginUrl ())) {
                stringRedisTemplate.delete(SHORTLINK_GOTO_KEY);
                // 如果旧链接在数据库过期了 但是更新的链接有有效期 删除缓存的null link
                LocalDateTime currentDate = LocalDateTime.now ();
                if (selectOne.getValidDate() != null && selectOne.getValidDate ().isBefore (currentDate)) {
                    if (requestParam.getValidDateType ().equals(ValidDateTypeEnum.PERMANENT.getValue ()) ||
                        requestParam.getValidDate ().isAfter (currentDate)) {
                        stringRedisTemplate.delete (SHORTLINK_ISNULL_GOTO_KEY);
                    }
                }
            }
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
        // 1.查询缓存的link
        String originalLink = stringRedisTemplate.opsForValue ().get (String.format (SHORTLINK_GOTO_KEY , fullLink));
        if (StrUtil.isBlank (originalLink)){
            try {
                response.sendRedirect ("/page/notfound");
            } catch (IOException e) {
                throw new ClientException ("重定向不存在页面失败");
            }
            return;
        }
        // 1.1 如果缓存数据不为NULL直接返回
        if (!StrUtil.equals (originalLink, "-")) {
            ShortLinkStatsRecordDTO statsRecord = buildLinkStatsRecordAndSetUser(fullLink, request, response);
            shortLinkStats(fullLink, statsRecord);
            // 返回重定向链接
            try {
                // 重定向
                response.sendRedirect (originalLink);
            } catch (IOException e) {
                throw new ClientException ("短链接重定向失败");
            }
            return;
        }
        // 1.2 缓存为空值
        if (StrUtil.equals (originalLink, "-")) {
            try {
                response.sendRedirect ("/page/notfound");
            } catch (IOException e) {
                throw new ClientException ("重定向不存在页面失败");
            }
            return;
        }
        // 2.如果缓存没有数据 查询布隆过滤器（短链接存入数据库是就添加入了布隆过滤器）
        boolean contains = linkUriCreateCachePenetrationBloomFilter.contains (fullLink);
        // 2.1 布隆过滤器不存在 则数据库也没有数据 直接返回
        if (!contains) {
            try {
                response.sendRedirect ("/page/notfound");
            } catch (IOException e) {
                throw new ClientException ("重定向不存在页面失败");
            }
            return;
        }
        // 3.缓存数据过期 布隆过滤器有数据 获取分布式🔒查询数据库
        RLock lock = redissonClient.getLock (String.format (LOCK_SHORTLINK_GOTO_KEY , fullLink));
        lock.lock ();
        try {
            // 3.1 双重判断🔒缓存数据 如果上一个线程已经在缓存设置新数据 可直接返回
            // 查询缓存的link
            originalLink = stringRedisTemplate.opsForValue ().get (String.format (SHORTLINK_GOTO_KEY , fullLink));
            // 如果缓存数据不为NULL直接返回
            if (!StrUtil.equals (originalLink, "-")) {
                ShortLinkStatsRecordDTO statsRecord = buildLinkStatsRecordAndSetUser(fullLink, request, response);
                shortLinkStats(fullLink, statsRecord);
                // 返回重定向链接
                try {
                    // 重定向
                    response.sendRedirect (originalLink);
                } catch (IOException e) {
                    throw new ClientException ("短链接重定向失败");
                }
                return;
            }
            if (StrUtil.equals (originalLink, "-")) {
                try {
                    response.sendRedirect ("/page/notfound");
                } catch (IOException e) {
                    throw new ClientException ("重定向不存在页面失败");
                }
                return;
            }
            // 缓存还是没有任何数据 说明这时候是第一个线程查询
            // 3.2 查询路由表中的短链接（短链接做分片键 因为短链接表用gid分片键 不能直接根据完整短链接快速查询结果）
            LambdaQueryWrapper<LinkGotoDO> linkGotoDoLambdaQueryWrapper = new LambdaQueryWrapper<LinkGotoDO> ()
                    .eq (LinkGotoDO::getFullShortUrl , fullLink);
            LinkGotoDO linkGotoDO = linkGotoMapper.selectOne (linkGotoDoLambdaQueryWrapper);
            // 3.3 路由表没有数据
            if (linkGotoDO == null) {
                // 设置空值 直接返回 该链接在数据库是不存在值的 但是布隆过滤器没有删除值
                stringRedisTemplate.opsForValue ().set (String.format (SHORTLINK_GOTO_KEY , fullLink), "-",30, TimeUnit.SECONDS);
                // 严谨 需要进行风控
                try {
                    response.sendRedirect ("/page/notfound");
                } catch (IOException e) {
                    throw new ClientException ("重定向不存在页面失败");
                }
                return;
            }
            // 3.4 路由表有数据 使用路由表的gid快速查询短链接表的数据
            LambdaQueryWrapper<ShortLinkDO> shortLinkDoLambdaQueryWrapper = new LambdaQueryWrapper<ShortLinkDO> ()
                    .eq (ShortLinkDO::getGid , linkGotoDO.getGid ())
                    .eq (ShortLinkDO::getFullShortUrl , fullLink)
                    .eq (ShortLinkDO::getEnableStatus , 0)
                    .eq (ShortLinkDO::getDelFlag , 0);
            ShortLinkDO shortLinkDO = baseMapper.selectOne (shortLinkDoLambdaQueryWrapper);
            if (shortLinkDO == null || shortLinkDO.getValidDate () != null && shortLinkDO.getValidDate ().isBefore (LocalDateTime.now ())) {
                // 3.4.1 如果数据库的链接过期
                stringRedisTemplate.opsForValue ().set (String.format (SHORTLINK_GOTO_KEY , fullLink), "-",30, TimeUnit.SECONDS);
                // 严谨 需要进行风控
                try {
                    response.sendRedirect ("/page/notfound");
                } catch (IOException e) {
                    throw new ClientException ("重定向不存在页面失败");
                }
                return;
            }
            ShortLinkStatsRecordDTO statsRecord = buildLinkStatsRecordAndSetUser(fullLink, request, response);
            shortLinkStats(fullLink, statsRecord);
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
    
    private ShortLinkStatsRecordDTO buildLinkStatsRecordAndSetUser(
            String fullShortLink,
            HttpServletRequest request ,
            HttpServletResponse response) {
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
        LocalDateTime fullDate = LocalDateTime.now ();
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
                .createTime (fullDate)
                .build();
    }
    
    @Override
     public void shortLinkStats (String fullShortLink, ShortLinkStatsRecordDTO statsRecord) {
        ShortLinkStatsMqToDbDTO shortLinkStatsMqToDbDTO = BeanUtil.copyProperties (statsRecord , ShortLinkStatsMqToDbDTO.class);
        Map<String,String> producerMap = new HashMap<> ();
        producerMap.put ("statsRecord",JSON.toJSONString (shortLinkStatsMqToDbDTO));
        rocketMqMessageService.sendMessage ("shortlink-stats-topic", producerMap);
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
            // 避免重复生成 加上UUID下一次重新生成 不影响实际url
            originUrl += UUID.randomUUID ().toString ();
            generatingCount++;
        }
    }
    
    /**
     * 获取网站图标
     *
     * @param url 网址
     * @return 网站图标链接
     */
    public String getFavicon(String url) {
        return url + "/favicon.ico";
    }
    
    /**
     * 距离一天结束毫秒数
     *
     * @return Long
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
            throw new ClientException("为避免恶意攻击，请生成安全网站跳转链接：" + gotoDomainWhiteListConfiguration.getNames());
        }
    }
}
