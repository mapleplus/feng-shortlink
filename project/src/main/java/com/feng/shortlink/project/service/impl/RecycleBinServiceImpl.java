package com.feng.shortlink.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feng.shortlink.project.dao.entity.*;
import com.feng.shortlink.project.dao.mapper.*;
import com.feng.shortlink.project.dto.request.ShortLinkRecycleBinPageReqDTO;
import com.feng.shortlink.project.dto.request.ShortLinkRecycleBinRecoverReqDTO;
import com.feng.shortlink.project.dto.request.ShortLinkRecycleBinRemoveReqDTO;
import com.feng.shortlink.project.dto.request.ShortLinkRecycleBinSaveReqDTO;
import com.feng.shortlink.project.dto.response.ShortLinkPageRespDTO;
import com.feng.shortlink.project.service.RecycleBinService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.feng.shortlink.project.common.constant.RedisCacheConstant.SHORTLINK_GOTO_KEY;
import static com.feng.shortlink.project.common.constant.RedisCacheConstant.SHORTLINK_ISNULL_GOTO_KEY;

/**
 * @author FENGXIN
 * @date 2024/10/3
 * @project feng-shortlink
 * @description 回收站业务实现
 **/
@Service
@RequiredArgsConstructor
public class RecycleBinServiceImpl extends ServiceImpl<ShortLinkMapper, ShortLinkDO> implements RecycleBinService {
    private final StringRedisTemplate stringRedisTemplate;
    private final LinkAccessLogsMapper linkAccessLogsMapper;
    private final LinkAccessStatsMapper linkAccessStatsMapper;
    private final LinkBrowserStatsMapper linkBrowserStatsMapper;
    private final LinkDeviceStatsMapper linkDeviceStatsMapper;
    private final LinkLocaleStatsMapper linkLocaleStatsMapper;
    private final LinkNetworkStatsMapper linkNetworkStatsMapper;
    private final LinkOsStatsMapper linkOsStatsMapper;
    private final LinkStatsTodayMapper linkStatsTodayMapper;
    
    @Override
    public void saveRecycleBin (ShortLinkRecycleBinSaveReqDTO requestParam) {
        LambdaUpdateWrapper<ShortLinkDO> lambdaUpdateWrapper = new LambdaUpdateWrapper<ShortLinkDO>()
                .eq (ShortLinkDO::getGid, requestParam.getGid())
                .eq (ShortLinkDO::getFullShortUrl, requestParam.getFullShortUrl())
                .eq (ShortLinkDO::getEnableStatus,0)
                .eq(ShortLinkDO::getDelFlag,0);
        baseMapper.update (ShortLinkDO.builder ().enableStatus (1).delTime (0L).build (), lambdaUpdateWrapper);
        // 删除缓存
        stringRedisTemplate.delete (String.format (SHORTLINK_GOTO_KEY , requestParam.getFullShortUrl ()));
    }
    
    @Override
    public IPage<ShortLinkPageRespDTO> pageRecycleBinShortLink (ShortLinkRecycleBinPageReqDTO requestParam) {
        LambdaQueryWrapper<ShortLinkDO> lambdaQueryWrapper = new LambdaQueryWrapper<ShortLinkDO> ()
                .in (ShortLinkDO::getGid , requestParam.getGidList ())
                .eq (ShortLinkDO::getDelFlag , 0)
                .eq (ShortLinkDO::getEnableStatus,1)
                .orderByDesc (ShortLinkDO::getUpdateTime);
        IPage<ShortLinkDO> selectPage = baseMapper.selectPage (requestParam , lambdaQueryWrapper);
        return selectPage.convert (each -> {
            ShortLinkPageRespDTO result = BeanUtil.copyProperties (each , ShortLinkPageRespDTO.class);
            result.setDomain ("http://" + result.getDomain ());
            return result;
        });
    }
    
    @Override
    public void recoverRecycleBin (ShortLinkRecycleBinRecoverReqDTO requestParam) {
        LambdaUpdateWrapper<ShortLinkDO> lambdaUpdateWrapper = new LambdaUpdateWrapper<ShortLinkDO> ()
                .eq (ShortLinkDO::getGid, requestParam.getGid())
                .eq (ShortLinkDO::getFullShortUrl, requestParam.getFullShortUrl())
                .eq (ShortLinkDO::getDelFlag,0);
        baseMapper.update (ShortLinkDO.builder ().enableStatus (0).build (), lambdaUpdateWrapper);
        // 删除缓存NULL
        stringRedisTemplate.delete (String.format (SHORTLINK_ISNULL_GOTO_KEY , requestParam.getFullShortUrl ()));
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeRecycleBin (ShortLinkRecycleBinRemoveReqDTO requestParam) {
        LambdaUpdateWrapper<ShortLinkDO> lambdaUpdateWrapper = new LambdaUpdateWrapper<ShortLinkDO> ()
                .eq (ShortLinkDO::getGid, requestParam.getGid())
                .eq (ShortLinkDO::getFullShortUrl, requestParam.getFullShortUrl())
                .eq (ShortLinkDO::getEnableStatus,1)
                .eq(ShortLinkDO::getDelTime, 0L)
                .eq (ShortLinkDO::getDelFlag,0);
        ShortLinkDO delShortLinkDO = ShortLinkDO.builder()
                .delTime(System.currentTimeMillis ())
                .build();
        delShortLinkDO.setDelFlag(1);
        baseMapper.update(delShortLinkDO, lambdaUpdateWrapper);
        LinkAccessLogsDO linkAccessLogsDO = new LinkAccessLogsDO ();
        LambdaUpdateWrapper<LinkAccessLogsDO> accessLogsDoLambdaUpdateWrapper = new LambdaUpdateWrapper<LinkAccessLogsDO>()
                .eq (LinkAccessLogsDO::getFullShortUrl, requestParam.getFullShortUrl())
                .eq (LinkAccessLogsDO::getDelFlag,0)
                .set (LinkAccessLogsDO::getDelFlag,1)
                .set (LinkAccessLogsDO::getDelTime, System.currentTimeMillis ());
        linkAccessLogsMapper.update (linkAccessLogsDO, accessLogsDoLambdaUpdateWrapper);
        
        LinkAccessStatsDO linkAccessStatsDO = new LinkAccessStatsDO ();
        LambdaUpdateWrapper<LinkAccessStatsDO> accessStatsDoLambdaUpdateWrapper = new LambdaUpdateWrapper<LinkAccessStatsDO> ()
                .eq (LinkAccessStatsDO::getFullShortUrl, requestParam.getFullShortUrl())
                .eq (LinkAccessStatsDO::getDelFlag,0)
                .set (LinkAccessStatsDO::getDelFlag,1)
                .set (LinkAccessStatsDO::getDelTime, System.currentTimeMillis ());
        linkAccessStatsMapper.update (linkAccessStatsDO, accessStatsDoLambdaUpdateWrapper);
        
        LinkBrowserStatsDO linkBrowserStatsDO = new LinkBrowserStatsDO ();
        LambdaUpdateWrapper<LinkBrowserStatsDO> browserStatsDoLambdaUpdateWrapper = new LambdaUpdateWrapper<LinkBrowserStatsDO> ()
                .eq (LinkBrowserStatsDO::getFullShortUrl, requestParam.getFullShortUrl())
                .eq (LinkBrowserStatsDO::getDelFlag,0)
                .set (LinkBrowserStatsDO::getDelFlag,1)
                .set (LinkBrowserStatsDO::getDelTime, System.currentTimeMillis ());
        linkBrowserStatsMapper.update (linkBrowserStatsDO, browserStatsDoLambdaUpdateWrapper);
        
        LinkDeviceStatsDO linkDeviceStatsDO = new LinkDeviceStatsDO ();
        LambdaUpdateWrapper<LinkDeviceStatsDO> deviceStatsDoLambdaUpdateWrapper = new LambdaUpdateWrapper<LinkDeviceStatsDO> ()
                .eq (LinkDeviceStatsDO::getFullShortUrl, requestParam.getFullShortUrl())
                .eq (LinkDeviceStatsDO::getDelFlag,0)
                .set (LinkDeviceStatsDO::getDelFlag,1)
                .set (LinkDeviceStatsDO::getDelTime, System.currentTimeMillis ());
        linkDeviceStatsMapper.update (linkDeviceStatsDO,deviceStatsDoLambdaUpdateWrapper);
        
        LinkLocaleStatsDO linkLocaleStatsDO = new LinkLocaleStatsDO ();
        LambdaUpdateWrapper<LinkLocaleStatsDO> localeStatsDoLambdaUpdateWrapper = new LambdaUpdateWrapper<LinkLocaleStatsDO> ()
                .eq (LinkLocaleStatsDO::getFullShortUrl, requestParam.getFullShortUrl())
                .eq (LinkLocaleStatsDO::getDelFlag,0)
                .set (LinkLocaleStatsDO::getDelFlag,1)
                .set (LinkLocaleStatsDO::getDelTime, System.currentTimeMillis ());
        linkLocaleStatsMapper.update (linkLocaleStatsDO,localeStatsDoLambdaUpdateWrapper);
        
        LinkNetworkStatsDO linkNetworkStatsDO = new LinkNetworkStatsDO ();
        LambdaUpdateWrapper<LinkNetworkStatsDO> networkStatsDoLambdaUpdateWrapper = new LambdaUpdateWrapper<LinkNetworkStatsDO> ()
                .eq (LinkNetworkStatsDO::getFullShortUrl, requestParam.getFullShortUrl())
                .eq (LinkNetworkStatsDO::getDelFlag,0)
                .set (LinkNetworkStatsDO::getDelFlag,1)
                .set (LinkNetworkStatsDO::getDelTime, System.currentTimeMillis ());
        linkNetworkStatsMapper.update (linkNetworkStatsDO,networkStatsDoLambdaUpdateWrapper);
        
        LinkOsStatsDO linkOsStatsDO = new LinkOsStatsDO ();
        LambdaUpdateWrapper<LinkOsStatsDO> osStatsDoLambdaUpdateWrapper = new LambdaUpdateWrapper<LinkOsStatsDO> ()
                .eq (LinkOsStatsDO::getFullShortUrl, requestParam.getFullShortUrl())
                .eq (LinkOsStatsDO::getDelFlag,0)
                .set (LinkOsStatsDO::getDelFlag,1)
                .set (LinkOsStatsDO::getDelTime, System.currentTimeMillis ());
        linkOsStatsMapper.update (linkOsStatsDO,osStatsDoLambdaUpdateWrapper);
        
        LinkStatsTodayDO linkStatsTodayDO = new LinkStatsTodayDO ();
        LambdaUpdateWrapper<LinkStatsTodayDO> statsTodayDoLambdaUpdateWrapper = new LambdaUpdateWrapper<LinkStatsTodayDO> ()
                .eq (LinkStatsTodayDO::getFullShortUrl, requestParam.getFullShortUrl())
                .eq (LinkStatsTodayDO::getDelFlag,0)
                .set (LinkStatsTodayDO::getDelFlag,1)
                .set (LinkStatsTodayDO::getDelTime, System.currentTimeMillis ());
        linkStatsTodayMapper.update (linkStatsTodayDO,statsTodayDoLambdaUpdateWrapper);
    }
}
