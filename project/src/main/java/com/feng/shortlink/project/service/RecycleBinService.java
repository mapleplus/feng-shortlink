package com.feng.shortlink.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.feng.shortlink.project.dao.entity.ShortLinkDO;
import com.feng.shortlink.project.dto.request.ShortLinkRecycleBinRecoverReqDTO;
import com.feng.shortlink.project.dto.request.ShortLinkRecycleBinRemoveReqDTO;
import com.feng.shortlink.project.dto.request.ShortLinkRecycleBinSaveReqDTO;
import com.feng.shortlink.project.dto.request.ShortLinkRecycleBinPageReqDTO;
import com.feng.shortlink.project.dto.response.ShortLinkPageRespDTO;

/**
 * @author FENGXIN
 * @date 2024/10/3
 * @project feng-shortlink
 * @description 回收站业务接口
 **/
public interface RecycleBinService extends IService<ShortLinkDO> {
    
    /**
     * 保存短链接到回收站
     */
    void saveRecycleBin (ShortLinkRecycleBinSaveReqDTO requestParam);
    
    /**
     * 分页查询回收站短链接
     */
    IPage<ShortLinkPageRespDTO> pageRecycleBinShortLink (ShortLinkRecycleBinPageReqDTO requestParam);
    
    /**
     * 恢复短链接
     */
    void recoverRecycleBin (ShortLinkRecycleBinRecoverReqDTO requestParam);
    
    /**
     * 移除短链接
     */
    void removeRecycleBin (ShortLinkRecycleBinRemoveReqDTO requestParam);
}
