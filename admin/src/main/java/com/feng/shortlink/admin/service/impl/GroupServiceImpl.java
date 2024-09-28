package com.feng.shortlink.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feng.shortlink.admin.dao.entity.GroupDO;
import com.feng.shortlink.admin.dao.mapper.GroupMapper;
import com.feng.shortlink.admin.dto.request.SaveShortLinkGroupDTO;
import com.feng.shortlink.admin.service.GroupService;
import com.feng.shortlink.admin.util.RandomIDGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author FENGXIN
 * @date 2024/9/28
 * @project feng-shortlink
 * @description
 **/
@Service
@Slf4j
public class GroupServiceImpl extends ServiceImpl<GroupMapper, GroupDO> implements GroupService {
    
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
}
