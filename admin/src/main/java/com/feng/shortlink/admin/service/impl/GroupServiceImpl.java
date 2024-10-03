package com.feng.shortlink.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feng.shortlink.admin.common.biz.user.UserContext;
import com.feng.shortlink.admin.common.convention.result.Result;
import com.feng.shortlink.admin.dao.entity.GroupDO;
import com.feng.shortlink.admin.dao.mapper.GroupMapper;
import com.feng.shortlink.admin.dto.request.ShortLinkGroupSortDTO;
import com.feng.shortlink.admin.dto.request.ShortLinkGroupUpdateDTO;
import com.feng.shortlink.admin.dto.response.GroupRespDTO;
import com.feng.shortlink.admin.remote.ShortLinkRemoteService;
import com.feng.shortlink.admin.remote.dto.response.ShortLinkGroupQueryRespDTO;
import com.feng.shortlink.admin.service.GroupService;
import com.feng.shortlink.admin.util.RandomIDGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author FENGXIN
 * @date 2024/9/28
 * @project feng-shortlink
 * @description 分组实现
 **/
@Service
@Slf4j
public class GroupServiceImpl extends ServiceImpl<GroupMapper, GroupDO> implements GroupService {
    ShortLinkRemoteService shortLinkRemoteService;

    @Override
    public void saveGroupByGid (String requestParam) {
        saveGroupByGid (UserContext.getUserName (), requestParam);
    }
    
    @Override
    public void saveGroupByGid (String username,String requestParam) {
        // 生成随机gid
        String gid = RandomIDGenerator.generateRandomGid ();
        // 保证gid全局不重复
        while (true) {
            LambdaQueryWrapper<GroupDO> queryWrapper = new LambdaQueryWrapper<GroupDO> ()
                    .eq (GroupDO::getGid , gid)
                    .eq (GroupDO::getUsername , username);
            // gid唯一就退出循环
            if (baseMapper.selectOne (queryWrapper) == null) {
                break;
            }
            gid = RandomIDGenerator.generateRandomGid ();
        }
        GroupDO groupDO = GroupDO.builder ()
                .gid (gid)
                .name (requestParam)
                .username (username)
                .sortOrder (0)
                .delFlag (0)
                .build ();
        baseMapper.insert (groupDO);
        log.info ("save group success, gid = {}" , gid);
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
        shortLinkRemoteService = new ShortLinkRemoteService () {};
        Result<List<ShortLinkGroupQueryRespDTO>> result = shortLinkRemoteService
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
}
