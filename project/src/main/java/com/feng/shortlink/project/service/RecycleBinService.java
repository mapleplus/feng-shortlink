package com.feng.shortlink.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.feng.shortlink.project.dao.entity.ShortLinkDO;
import com.feng.shortlink.project.dto.request.RecycleBinSaveReqDTO;
import com.feng.shortlink.project.dto.request.ShortLinkRecycleBinPageReqDTO;
import com.feng.shortlink.project.dto.response.ShortLinkPageRespDTO;

/**
 * @author FENGXIN
 * @date 2024/10/3
 * @project feng-shortlink
 * @description
 **/
public interface RecycleBinService extends IService<ShortLinkDO> {
    
    /**
     * 保存短链接到回收站
     *
     * @param requestParam 请求参数
     */
    void saveRecycleBin (RecycleBinSaveReqDTO requestParam);
    
    /**
     * 分页查询回收站短链接
     *
     * @param requestParam 请求参数
     * @return {@code IPage<ShortLinkPageRespDTO> }
     */
    IPage<ShortLinkPageRespDTO> pageRecycleBinShortLink (ShortLinkRecycleBinPageReqDTO requestParam);
}
