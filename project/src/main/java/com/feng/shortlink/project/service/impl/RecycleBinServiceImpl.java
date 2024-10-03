package com.feng.shortlink.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feng.shortlink.project.dao.entity.ShortLinkDO;
import com.feng.shortlink.project.dao.mapper.ShortLinkMapper;
import com.feng.shortlink.project.dto.request.RecycleBinSaveReqDTO;
import com.feng.shortlink.project.dto.request.ShortLinkPageReqDTO;
import com.feng.shortlink.project.dto.response.ShortLinkPageRespDTO;
import com.feng.shortlink.project.service.RecycleBinService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import static com.feng.shortlink.project.common.constant.RedisCacheConstant.SHORTLINK_GOTO_KEY;

/**
 * @author FENGXIN
 * @date 2024/10/3
 * @project feng-shortlink
 * @description
 **/
@Service
@RequiredArgsConstructor
public class RecycleBinServiceImpl extends ServiceImpl<ShortLinkMapper, ShortLinkDO> implements RecycleBinService {
    private final StringRedisTemplate stringRedisTemplate;
    
    @Override
    public void saveRecycleBin (RecycleBinSaveReqDTO requestParam) {
        LambdaUpdateWrapper<ShortLinkDO> lambdaUpdateWrapper = new LambdaUpdateWrapper<ShortLinkDO>()
                .eq (ShortLinkDO::getGid, requestParam.getGid())
                .eq (ShortLinkDO::getFullShortUrl, requestParam.getFullShortUrl())
                .eq (ShortLinkDO::getEnableStatus,0)
                .eq(ShortLinkDO::getDelFlag,0);
        ShortLinkDO shortLinkDO = ShortLinkDO.builder ().enableStatus (1).build ();
        baseMapper.update (shortLinkDO, lambdaUpdateWrapper);
        // 删除缓存
        stringRedisTemplate.delete (String.format (SHORTLINK_GOTO_KEY , requestParam.getFullShortUrl ()));
    }
    
    @Override
    public IPage<ShortLinkPageRespDTO> pageRecycleBinShortLink (ShortLinkPageReqDTO requestParam) {
        LambdaQueryWrapper<ShortLinkDO> lambdaQueryWrapper = new LambdaQueryWrapper<ShortLinkDO> ()
                .eq (ShortLinkDO::getDelFlag , 0)
                .eq (ShortLinkDO::getGid , requestParam.getGid ())
                .eq (ShortLinkDO::getEnableStatus,1);
        ShortLinkPageReqDTO selectPage = baseMapper.selectPage (requestParam , lambdaQueryWrapper);
        return selectPage.convert (each -> BeanUtil.copyProperties (each,ShortLinkPageRespDTO.class));
    }
}