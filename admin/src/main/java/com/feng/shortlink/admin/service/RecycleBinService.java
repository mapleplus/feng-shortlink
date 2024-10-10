package com.feng.shortlink.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.feng.shortlink.admin.common.convention.result.Result;
import com.feng.shortlink.admin.remote.dto.request.ShortLinkRecycleBinPageReqDTO;
import com.feng.shortlink.admin.remote.dto.response.ShortLinkPageRespDTO;

/**
 * @author FENGXIN
 * @date 2024/10/3
 * @project feng-shortlink
 * @description 回收站查询接口
 **/
public interface RecycleBinService {
    Result<Page<ShortLinkPageRespDTO>> pageRecycleBinShortLink (ShortLinkRecycleBinPageReqDTO requestParam);
}
