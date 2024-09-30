package com.feng.shortlink.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feng.shortlink.project.common.convention.exception.ServiceException;
import com.feng.shortlink.project.dao.entity.ShortLinkDO;
import com.feng.shortlink.project.dao.mapper.ShortLinkMapper;
import com.feng.shortlink.project.dto.request.ShortLinkPageReqDTO;
import com.feng.shortlink.project.dto.request.ShortLinkSaveReqDTO;
import com.feng.shortlink.project.dto.response.ShortLinkPageRespDTO;
import com.feng.shortlink.project.dto.response.ShortLinkSaveRespDTO;
import com.feng.shortlink.project.service.ShortLinkService;
import com.feng.shortlink.project.util.HashUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

/**
 * @author FENGXIN
 * @date 2024/9/29
 * @project feng-shortlink
 * @description 短链接业务实现
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class ShortLinkImpl extends ServiceImpl<ShortLinkMapper, ShortLinkDO> implements ShortLinkService {
    private final RBloomFilter<String> linkUriCreateCachePenetrationBloomFilter;
    
    /**
     * 根据给定的请求参数保存短链接。
     *
     * @param requestParam 包含原始URL、域名、分组标识符和其他元数据的请求参数
     * @return 包含新创建的短链接详细信息的响应数据传输对象
     */
    @Override
    public ShortLinkSaveRespDTO saveShortLink (ShortLinkSaveReqDTO requestParam) {
        // 生成短链接 一个originUrl可以有多个短链接 只是要求短链接不能重复
        String shortLinkSuffix = generateShortLink (requestParam);
        String fullLink = requestParam.getDomain () + "/" + shortLinkSuffix;
        // 设置插入数据实体
        ShortLinkDO savedLinkDO = ShortLinkDO.builder ()
                .domain (requestParam.getDomain ())
                .shortUri (shortLinkSuffix)
                .fullShortUrl (fullLink)
                .originUrl (requestParam.getOriginUrl ())
                .clickNum (0)
                .gid (requestParam.getGid ())
                .favicon (requestParam.getFavicon ())
                .enableStatus (0)
                .createdType (requestParam.getCreatedType ())
                .validDateType (requestParam.getValidDateType ())
                .validDate (requestParam.getValidDate ())
                .describe (requestParam.getDescribe ())
                .build ();
        try {
            baseMapper.insert (savedLinkDO);
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
        linkUriCreateCachePenetrationBloomFilter.add (fullLink);
        return ShortLinkSaveRespDTO.builder ()
                .fullShortUrl (savedLinkDO.getFullShortUrl ())
                .gid (savedLinkDO.getGid ())
                .originUrl (savedLinkDO.getOriginUrl ())
                .build ();
    }
    
    /**
     * 页面短链接
     *
     * @param requestParam 请求参数
     * @return {@code IPage<ShortLinkPageRespDTO> }
     */
    @Override
    public IPage<ShortLinkPageRespDTO> pageShortLink (ShortLinkPageReqDTO requestParam) {
        LambdaQueryWrapper<ShortLinkDO> lambdaQueryWrapper = new LambdaQueryWrapper<ShortLinkDO> ()
                .eq (ShortLinkDO::getDelFlag , 0)
                .eq (ShortLinkDO::getGid , requestParam.getGid ())
                .eq (ShortLinkDO::getEnableStatus,0);
        ShortLinkPageReqDTO selectPage = baseMapper.selectPage (requestParam , lambdaQueryWrapper);
        return selectPage.convert (each -> BeanUtil.copyProperties (each,ShortLinkPageRespDTO.class));
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
            if (!linkUriCreateCachePenetrationBloomFilter.contains (requestParam.getDomain () + "/" + shortLink)) {
                return shortLink;
            }
            // 避免重复生成 加上时间毫秒下一次重新生成 不影响实际url
            originUrl += System.currentTimeMillis ();
            generatingCount++;
        }
    }
}
