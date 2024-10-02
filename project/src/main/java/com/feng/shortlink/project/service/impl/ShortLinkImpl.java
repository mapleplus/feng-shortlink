package com.feng.shortlink.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feng.shortlink.project.common.convention.exception.ClientException;
import com.feng.shortlink.project.common.convention.exception.ServiceException;
import com.feng.shortlink.project.common.enums.ValidDateTypeEnum;
import com.feng.shortlink.project.dao.entity.LinkGotoDO;
import com.feng.shortlink.project.dao.entity.ShortLinkDO;
import com.feng.shortlink.project.dao.mapper.LinkGotoMapper;
import com.feng.shortlink.project.dao.mapper.ShortLinkMapper;
import com.feng.shortlink.project.dto.request.ShortLinkPageReqDTO;
import com.feng.shortlink.project.dto.request.ShortLinkSaveReqDTO;
import com.feng.shortlink.project.dto.request.ShortLinkUpdateReqDTO;
import com.feng.shortlink.project.dto.response.ShortLinkGroupQueryRespDTO;
import com.feng.shortlink.project.dto.response.ShortLinkPageRespDTO;
import com.feng.shortlink.project.dto.response.ShortLinkSaveRespDTO;
import com.feng.shortlink.project.service.ShortLinkService;
import com.feng.shortlink.project.util.HashUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
    private final ShortLinkMapper shortLinkMapper;
    private final LinkGotoMapper linkGotoMapper;
    
    /**
     * 根据给定的请求参数创建短链接。
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
        linkUriCreateCachePenetrationBloomFilter.add (fullLink);
        return ShortLinkSaveRespDTO.builder ()
                .fullShortUrl (savedLinkDO.getFullShortUrl ())
                .gid (savedLinkDO.getGid ())
                .originUrl (savedLinkDO.getOriginUrl ())
                .build ();
    }
    
    /**
     * 更新短链接
     *
     * @param requestParam 请求参数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateShortLink (ShortLinkUpdateReqDTO requestParam) {
        // 查询db里的短链接
        LambdaQueryWrapper<ShortLinkDO> lambdaQueryWrapper = new LambdaQueryWrapper<ShortLinkDO> ()
                .eq (ShortLinkDO::getGid , requestParam.getGid ())
                .eq (ShortLinkDO::getFullShortUrl , requestParam.getFullShortUrl ())
                .eq (ShortLinkDO::getEnableStatus , 0)
                .eq (ShortLinkDO::getDelFlag , 0);
        ShortLinkDO selectOne = baseMapper.selectOne (lambdaQueryWrapper);
        if (selectOne == null) {
            throw new ClientException ("短链接不存在此分组");
        }
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
        if (Objects.equals (selectOne.getGid () , requestParam.getGid ())) {
            // gid一致 说明在同一组 直接新增 gid用谁的都可以
            LambdaUpdateWrapper<ShortLinkDO> lambdaUpdateWrapper = new LambdaUpdateWrapper<ShortLinkDO>()
                    .eq (ShortLinkDO::getGid , requestParam.getGid ())
                    .eq (ShortLinkDO::getFullShortUrl , requestParam.getFullShortUrl ())
                    .eq (ShortLinkDO::getEnableStatus , 0)
                    .eq (ShortLinkDO::getDelFlag , 0)
                    .set (Objects.equals (requestParam.getValidDateType (),ValidDateTypeEnum.PERMANENT.getValue ()),ShortLinkDO::getValidDateType , null );
            baseMapper.update (shortLinkDO,lambdaUpdateWrapper);
        }else {
            // gid 不一致 说明需要换组 需要删除之前的短链接gid用delectOne的 再新增到新组里
            LambdaUpdateWrapper<ShortLinkDO> lambdaUpdateWrapper = new LambdaUpdateWrapper<ShortLinkDO>()
                    .eq (ShortLinkDO::getGid , selectOne.getGid ())
                    .eq (ShortLinkDO::getFullShortUrl , requestParam.getFullShortUrl ())
                    .eq (ShortLinkDO::getEnableStatus , 0)
                    .eq (ShortLinkDO::getDelFlag , 0);
            baseMapper.delete (lambdaUpdateWrapper);
            baseMapper.insert (shortLinkDO);
        }
    }
    
    /**
     * 跳转链接
     *
     * @param shortLink 短链接
     * @param request 请求
     * @param response  响应
     */
    @Override
    public void restoreLink (String shortLink , HttpServletRequest request , HttpServletResponse response) {
        // 获取服务名 如baidu.com
        String serverName = request.getServerName ();
        String fullLink = serverName + "/" + shortLink;
        // 查询路由表中的短链接（短链接做分片键 因为短链接表用gid分片键 不能直接根据完整短链接快速查询结果）
        LambdaQueryWrapper<LinkGotoDO> linkGotoDoLambdaQueryWrapper = new LambdaQueryWrapper<LinkGotoDO> ()
                .eq (LinkGotoDO::getFullShortUrl , fullLink);
        LinkGotoDO linkGotoDO = linkGotoMapper.selectOne (linkGotoDoLambdaQueryWrapper);
        if (linkGotoDO == null) {
            // 严谨 需要进行封控
            return;
        }
        // 使用路由表的gid快速查询短链接表的数据
        LambdaQueryWrapper<ShortLinkDO> shortLinkDoLambdaQueryWrapper = new LambdaQueryWrapper<ShortLinkDO> ()
                .eq (ShortLinkDO::getGid , linkGotoDO.getGid ())
                .eq (ShortLinkDO::getFullShortUrl , fullLink)
                .eq (ShortLinkDO::getEnableStatus , 0)
                .eq (ShortLinkDO::getDelFlag , 0);
        ShortLinkDO shortLinkDO = baseMapper.selectOne (shortLinkDoLambdaQueryWrapper);
        if (shortLinkDO != null) {
            // 返回重定向链接
            try {
                // 重定向
                response.sendRedirect (shortLinkDO.getOriginUrl ());
            } catch (IOException e) {
                throw new ClientException ("短链接重定向失败");
            }
        }
    }
    
    /**
     * 分页查询短链接
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
     * 查询短链接组的短链接数量
     *
     * @param requestParam 请求参数
     * @return {@code List<ShortLinkGroupQueryRespDTO> }
     */
    @Override
    public List<ShortLinkGroupQueryRespDTO> listShortLinkGroup (List<String> requestParam) {
        QueryWrapper<ShortLinkDO> queryWrapper = new QueryWrapper<ShortLinkDO> ()
                .select ("gid as gid","COUNT(*) AS groupCount")
                .eq ("enable_status",0)
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
            if (!linkUriCreateCachePenetrationBloomFilter.contains (requestParam.getDomain () + "/" + shortLink)) {
                return shortLink;
            }
            // 避免重复生成 加上时间毫秒下一次重新生成 不影响实际url
            originUrl += System.currentTimeMillis ();
            generatingCount++;
        }
    }
}
