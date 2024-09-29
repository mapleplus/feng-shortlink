package com.feng.shortlink.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.feng.shortlink.project.dao.entity.ShortLinkDO;
import com.feng.shortlink.project.dto.request.ShortLinkSaveReqDTO;
import com.feng.shortlink.project.dto.response.ShortLinkSaveRespDTO;

/**
 * @author FENGXIN
 * @date 2024/9/29
 * @project feng-shortlink
 * @description 短链接业务接口
 **/
public interface ShortLinkService extends IService<ShortLinkDO> {
    /**
     * Saves a short link based on provided request parameters.
     *
     * @param requestParam the request parameters that include details about the short link to be saved
     * @return the response containing the details of the saved short link
     */
    ShortLinkSaveRespDTO saveShortLink (ShortLinkSaveReqDTO requestParam);
}
