package com.feng.shortlink.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feng.shortlink.project.dao.entity.ShortLinkDO;
import com.feng.shortlink.project.dao.mapper.ShortLinkMapper;
import com.feng.shortlink.project.dto.request.ShortLinkSaveReqDTO;
import com.feng.shortlink.project.dto.response.ShortLinkSaveRespDTO;
import com.feng.shortlink.project.service.ShortLinkService;
import com.feng.shortlink.project.util.HashUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author FENGXIN
 * @date 2024/9/29
 * @project feng-shortlink
 * @description 短链接业务实现
 **/
@Slf4j
@Service
public class ShortLinkImpl extends ServiceImpl<ShortLinkMapper, ShortLinkDO> implements ShortLinkService {
    
    
    /**
     * Saves a short link based on the given request parameters.
     *
     * @param requestParam the request parameter containing the original URL, domain, group identifier, and other metadata
     * @return the response data transfer object containing the details of the newly created short link
     */
    @Override
    public ShortLinkSaveRespDTO saveShortLink (ShortLinkSaveReqDTO requestParam) {
        // 生成短链接
        String shortLinkSuffix = generateShortLink (requestParam);
        // 设置插入数据实体
        ShortLinkDO savedLinkDO = BeanUtil.toBean (requestParam , ShortLinkDO.class);
        savedLinkDO.setFullShortUrl (savedLinkDO.getDomain () + "/" + shortLinkSuffix);
        savedLinkDO.setShortUri (shortLinkSuffix);
        savedLinkDO.setClickNum (0);
        savedLinkDO.setEnableStatus (0);
        baseMapper.insert (savedLinkDO);
        return ShortLinkSaveRespDTO.builder ()
                .fullShortUrl (savedLinkDO.getFullShortUrl ())
                .gid (savedLinkDO.getGid ())
                .originUrl (savedLinkDO.getOriginUrl ())
                .build ();
    }
    
    
    /**
     * Converts the original URL from the request parameter into a short link.
     *
     * @param requestParam the request parameter containing the original URL and other metadata
     * @return the generated short link in base62 format
     */
    public String generateShortLink (ShortLinkSaveReqDTO requestParam) {
        return HashUtil.hashToBase62 (requestParam.getOriginUrl ());
    }
}
