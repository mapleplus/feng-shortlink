package com.feng.shortlink.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.feng.shortlink.admin.dao.entity.GroupDO;
import com.feng.shortlink.admin.dto.request.SaveShortLinkGroupDTO;
import com.feng.shortlink.admin.dto.response.GroupRespDTO;

import java.util.List;

/**
 * @author FENGXIN
 * @date 2024/9/28
 * @project feng-shortlink
 * @description
 **/
public interface GroupService extends IService<GroupDO> {
    void saveGroupByGid (SaveShortLinkGroupDTO requestParams);
    
    List<GroupRespDTO> getGroup ();
}
