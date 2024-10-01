package com.feng.shortlink.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.feng.shortlink.admin.dao.entity.GroupDO;
import com.feng.shortlink.admin.dto.request.ShortLinkGroupSortDTO;
import com.feng.shortlink.admin.dto.request.ShortLinkGroupUpdateDTO;
import com.feng.shortlink.admin.dto.response.GroupRespDTO;

import java.util.List;

/**
 * @author FENGXIN
 * @date 2024/9/28
 * @project feng-shortlink
 * @description
 **/
public interface GroupService extends IService<GroupDO> {
    /**
     * 根据组ID保存分组信息
     *
     * @param requestParam 包含分组详细信息的数据传输对象
     */
    void saveGroupByGid (String responseParam);
    void saveGroupByGid (String username,String responseParam);
    /**
     * 获取所有分组信息
     *
     * @return 所有分组的响应DTO列表
     */
    List<GroupRespDTO> getGroup ();
    
    /**
     * 更新分组信息
     *
     * @param requestParam 包含更新分组详细信息的数据传输对象
     */
    void updateGroup (ShortLinkGroupUpdateDTO requestParam);
    
    /**
     * 根据组ID删除分组信息
     *
     * @param gid 要删除的组ID
     */
    void deleteGroup (String gid);
    
    /**
     * 对分组进行排序
     *
     * @param requestParam 包含排序分组详细信息的请求参数列表
     */
    void sortGroup (List<ShortLinkGroupSortDTO> requestParam);
}
