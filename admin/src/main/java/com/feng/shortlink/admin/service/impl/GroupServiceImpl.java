package com.feng.shortlink.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feng.shortlink.admin.common.biz.user.UserContext;
import com.feng.shortlink.admin.common.convention.exception.ClientException;
import com.feng.shortlink.admin.common.convention.exception.ServiceException;
import com.feng.shortlink.admin.common.convention.result.Result;
import com.feng.shortlink.admin.dao.entity.GroupDO;
import com.feng.shortlink.admin.dao.entity.GroupUniqueDO;
import com.feng.shortlink.admin.dao.mapper.GroupMapper;
import com.feng.shortlink.admin.dao.mapper.GroupUniqueMapper;
import com.feng.shortlink.admin.dto.request.ShortLinkGroupSortDTO;
import com.feng.shortlink.admin.dto.request.ShortLinkGroupUpdateDTO;
import com.feng.shortlink.admin.dto.response.GroupRespDTO;
import com.feng.shortlink.admin.remote.ShortLinkActualRemoteService;
import com.feng.shortlink.admin.remote.dto.response.ShortLinkGroupQueryRespDTO;
import com.feng.shortlink.admin.service.GroupService;
import com.feng.shortlink.admin.util.RandomIDGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.feng.shortlink.admin.common.constant.RedisCacheConstant.LOCK_SHORTLINK_GROUP;

/**
 * @author FENGXIN
 * @date 2024/9/28
 * @project feng-shortlink
 * @description 分组实现
 **/
@Service
@Slf4j
@RequiredArgsConstructor
public class GroupServiceImpl extends ServiceImpl<GroupMapper, GroupDO> implements GroupService {
    private final RBloomFilter<String> gidRegisterCachePenetrationBloomFilter;
    private final GroupUniqueMapper groupUniqueMapper;
    private final ShortLinkActualRemoteService shortLinkActualRemoteService;
    private final RedissonClient redissonClient;
    
    @Value ("${short-link.group.max-num}")
    private Integer maxNum;
    
    @Override
    public void saveGroupByGid (String requestParam) {
        saveGroupByGid (UserContext.getUserName (), requestParam);
    }
    
    @Override
    public void saveGroupByGid (String username,String requestParam) {
        RLock lock = redissonClient.getLock (String.format (LOCK_SHORTLINK_GROUP , username));
        lock.lock ();
        try {
            // 查询group数量
            LambdaQueryWrapper<GroupDO> lambdaQueryWrapper = new LambdaQueryWrapper<GroupDO> ()
                    .eq (GroupDO::getUsername , username)
                    .eq (GroupDO::getDelFlag,0);
            List<GroupDO> groupDOList = baseMapper.selectList (lambdaQueryWrapper);
            if (CollUtil.isNotEmpty (groupDOList) && groupDOList.size () == maxNum) {
                throw new ClientException (String.format ("分组数量超过限定范围：%d",maxNum));
            }
            String gid = null;
            int retryCount = 0;
            int maxRetry = 10;
            while (retryCount < maxRetry) {
                gid =saveUniqueGroupGid ();
                retryCount++;
                if (StrUtil.isNotBlank (gid)){
                    GroupDO groupDO = GroupDO.builder ()
                            .gid (gid)
                            .username (username)
                            .sortOrder (0)
                            .delFlag (0)
                            .build ();
                    baseMapper.insert(groupDO);
                    gidRegisterCachePenetrationBloomFilter.add (gid);
                    break;
                }
            }
            if (StrUtil.isBlank (gid)){
                throw new ServiceException ("生成分组gid频繁");
            }
            log.info ("save group success, gid = {}" , gid);
        }  finally {
            lock.unlock ();
        }
    }
    
    @Override
    public List<GroupRespDTO> getGroup () {
        LambdaQueryWrapper<GroupDO> queryWrapper = new LambdaQueryWrapper<GroupDO> ()
                .eq (GroupDO::getUsername , UserContext.getUserName ())
                .eq (GroupDO::getDelFlag , 0)
                .orderByAsc (GroupDO::getSortOrder , GroupDO::getUpdateTime);
        // 设置用户的分组数量
        List<GroupRespDTO> groupRespDTOList = BeanUtil.copyToList (baseMapper.selectList (queryWrapper) , GroupRespDTO.class);
        // 获取分组id 目的获取分组数量
        Result<List<ShortLinkGroupQueryRespDTO>> result = shortLinkActualRemoteService
                .listShortLinkGroup (groupRespDTOList.stream ().map (GroupRespDTO::getGid).collect (Collectors.toList ()));
        // 设置分组数量
        groupRespDTOList.forEach (groupRespDTO -> result.getData ().stream ()
                // gid相等再设置数量
                .filter (each -> Objects.equals(each.getGid(), groupRespDTO.getGid()))
                // 设置每组的数量
                .forEach (each -> groupRespDTO.setGroupCount (each.getGroupCount ())));
        return groupRespDTOList;
    }
    
    @Override
    public void updateGroup (ShortLinkGroupUpdateDTO requestParam) {
        LambdaQueryWrapper<GroupDO> queryWrapper = new LambdaQueryWrapper<GroupDO> ()
                .eq (GroupDO::getUsername , UserContext.getUserName ())
                .eq (GroupDO::getGid , requestParam.getGid ())
                .eq (GroupDO::getDelFlag , 0);
        GroupDO groupDO = GroupDO.builder ()
                .name (requestParam.getName ())
                .build ();
        baseMapper.update (groupDO , queryWrapper);
    }
    
    @Override
    public void deleteGroup (String gid) {
        LambdaQueryWrapper<GroupDO> queryWrapper = new LambdaQueryWrapper<GroupDO> ()
                .eq (GroupDO::getUsername , UserContext.getUserName ())
                .eq (GroupDO::getGid , gid)
                .eq (GroupDO::getDelFlag , 0);
        GroupDO groupDO = GroupDO.builder ()
                .delFlag (1)
                .build ();
        baseMapper.update (groupDO , queryWrapper);
    }
    
    @Override
    public void sortGroup (List<ShortLinkGroupSortDTO> requestParam) {
        requestParam.forEach (each -> {
            LambdaQueryWrapper<GroupDO> queryWrapper = new LambdaQueryWrapper<GroupDO> ()
                    .eq (GroupDO::getUsername , UserContext.getUserName ())
                    .eq (GroupDO::getGid , each.getGid ())
                    .eq (GroupDO::getDelFlag , 0);
            GroupDO groupDO = GroupDO.builder ()
                    .sortOrder (each.getSortOrder ())
                    .build ();
            baseMapper.update (groupDO , queryWrapper);
        });
    }
    
    private String saveUniqueGroupGid () {
        String gid = RandomIDGenerator.generateRandomGid ();
        if(!gidRegisterCachePenetrationBloomFilter.contains (gid)){
            GroupUniqueDO build = GroupUniqueDO.builder ()
                    .gid (gid)
                    .build ();
            try {
                groupUniqueMapper.insert (build);
            }catch (DuplicateKeyException e){
                return null;
            }
        }
        return gid;
    }
}
