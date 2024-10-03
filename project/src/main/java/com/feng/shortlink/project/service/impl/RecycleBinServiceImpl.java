package com.feng.shortlink.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feng.shortlink.project.dao.entity.ShortLinkDO;
import com.feng.shortlink.project.dao.mapper.ShortLinkMapper;
import com.feng.shortlink.project.dto.request.ShortLinkRecycleBinRecoverReqDTO;
import com.feng.shortlink.project.dto.request.ShortLinkRecycleBinRemoveReqDTO;
import com.feng.shortlink.project.dto.request.ShortLinkRecycleBinSaveReqDTO;
import com.feng.shortlink.project.dto.request.ShortLinkRecycleBinPageReqDTO;
import com.feng.shortlink.project.dto.response.ShortLinkPageRespDTO;
import com.feng.shortlink.project.service.RecycleBinService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

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
    
    @Override
    public void saveRecycleBin (ShortLinkRecycleBinSaveReqDTO requestParam) {
        LambdaUpdateWrapper<ShortLinkDO> lambdaUpdateWrapper = new LambdaUpdateWrapper<ShortLinkDO>()
                .eq (ShortLinkDO::getGid, requestParam.getGid())
                .eq (ShortLinkDO::getFullShortUrl, requestParam.getFullShortUrl())
                .eq (ShortLinkDO::getEnableStatus,0)
                .eq(ShortLinkDO::getDelFlag,0);
        baseMapper.update (ShortLinkDO.builder ().enableStatus (1).build (), lambdaUpdateWrapper);
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
    public void removeRecycleBin (ShortLinkRecycleBinRemoveReqDTO requestParam) {
        LambdaUpdateWrapper<ShortLinkDO> lambdaUpdateWrapper = new LambdaUpdateWrapper<ShortLinkDO> ()
                .eq (ShortLinkDO::getGid, requestParam.getGid())
                .eq (ShortLinkDO::getFullShortUrl, requestParam.getFullShortUrl())
                .eq (ShortLinkDO::getEnableStatus,1)
                .eq (ShortLinkDO::getDelFlag,0);
        baseMapper.delete (lambdaUpdateWrapper);
    }
}
