package com.feng.shortlink.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feng.shortlink.admin.dao.entity.GroupDO;
import com.feng.shortlink.admin.dao.mapper.GroupMapper;
import com.feng.shortlink.admin.dto.request.SaveShortLinkGroupDTO;
import com.feng.shortlink.admin.dto.response.GroupRespDTO;
import com.feng.shortlink.admin.service.GroupService;
import com.feng.shortlink.admin.util.RandomIDGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author FENGXIN
 * @date 2024/9/28
 * @project feng-shortlink
 * @description
 **/
@Service
@Slf4j
public class GroupServiceImpl extends ServiceImpl<GroupMapper, GroupDO> implements GroupService {
    
    /**
     * Saves a group with a globally unique GID generated randomly. Ensures that
     * the GID is unique by checking against existing records in the database.
     *
     * @param requestParams Parameters required to save the group, encapsulated
     *                      in a SaveShortLinkGroupDTO object.
     */
    @Override
    public void saveGroupByGid (SaveShortLinkGroupDTO requestParams) {
        // 生成随机gid
        String gid = RandomIDGenerator.generateRandomGid ();
        // 保证gid全局不重复
        while (true){
            LambdaQueryWrapper<GroupDO> queryWrapper = new LambdaQueryWrapper<GroupDO> ()
                    .eq (GroupDO::getGid, gid)
                    // TODO 网关获取username
                    .eq (GroupDO::getUsername, null);
            // gid唯一就退出循环
            if (baseMapper.selectOne (queryWrapper) == null) {
                break;
            }
            gid = RandomIDGenerator.generateRandomGid ();
        }
        GroupDO groupDO = GroupDO.builder ()
                .gid (gid)
                .name (requestParams.getName ())
                .build ();
        baseMapper.insert (groupDO);
        log.info ("save group success, gid = {}", gid);
    }
    
    @Override
    public List<GroupRespDTO> getGroup () {
        LambdaQueryWrapper<GroupDO> queryWrapper = Wrappers.lambdaQuery (GroupDO.class)
                // TODO 从上下文获取username
                .eq (GroupDO::getUsername, "哆啦A梦")
                .eq (GroupDO::getDelFlag, 0)
                .orderByAsc (GroupDO::getSortOrder,GroupDO::getUpdateTime);
        return BeanUtil.copyToList (baseMapper.selectList (queryWrapper), GroupRespDTO.class);
    }
}
