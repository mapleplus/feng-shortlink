package com.feng.shortlink.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.feng.shortlink.admin.common.convention.result.Result;
import com.feng.shortlink.admin.remote.dto.request.ShortLinkRecycleBinPageReqDTO;
import com.feng.shortlink.admin.remote.dto.response.ShortLinkPageRespDTO;

/**
 * @author FENGXIN
 * @date 2024/10/3
 * @project feng-shortlink
 * @description
 **/
public interface RecycleBinService {
    Result<IPage<ShortLinkPageRespDTO>> pageRecycleBinShortLink (ShortLinkRecycleBinPageReqDTO requestParam);
}
