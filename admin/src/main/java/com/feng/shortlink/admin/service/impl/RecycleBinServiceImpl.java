package com.feng.shortlink.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.feng.shortlink.admin.common.biz.user.UserContext;
import com.feng.shortlink.admin.common.convention.exception.ServiceException;
import com.feng.shortlink.admin.common.convention.result.Result;
import com.feng.shortlink.admin.dao.entity.GroupDO;
import com.feng.shortlink.admin.dao.mapper.GroupMapper;
import com.feng.shortlink.admin.remote.ShortLinkService;
import com.feng.shortlink.admin.remote.dto.request.ShortLinkRecycleBinPageReqDTO;
import com.feng.shortlink.admin.remote.dto.response.ShortLinkPageRespDTO;
import com.feng.shortlink.admin.service.RecycleBinService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author FENGXIN
 * @date 2024/10/3
 * @project feng-shortlink
 * @description
 **/
@Slf4j
@RequiredArgsConstructor
@Service
public class RecycleBinServiceImpl implements RecycleBinService {
    private final GroupMapper groupMapper;
    private final ShortLinkService shortLinkService = new ShortLinkService () {};
    @Override
    public Result<IPage<ShortLinkPageRespDTO>> pageRecycleBinShortLink (ShortLinkRecycleBinPageReqDTO requestParam) {
        LambdaQueryWrapper<GroupDO> queryWrapper = new LambdaQueryWrapper<GroupDO>()
                .eq (GroupDO::getDelFlag , 0)
                .eq (GroupDO::getUsername, UserContext.getUserName ());
        List<GroupDO> groupDOList = groupMapper.selectList (queryWrapper);
        if(groupDOList.isEmpty()){
            throw new ServiceException ("用户无分组信息");
        }
        requestParam.setGidList (groupDOList.stream().map (GroupDO::getGid).collect(Collectors.toList()));
        log.info ("Gid List: {}" , requestParam.getGidList ());
        return shortLinkService.pageRecycleBinShortLink (requestParam);
    }
}
