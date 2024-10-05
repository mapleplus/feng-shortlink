package com.feng.shortlink.project.service;

import com.feng.shortlink.project.dto.request.ShortLinkStatsReqDTO;
import com.feng.shortlink.project.dto.response.ShortLinkStatsRespDTO;

/**
 * @author FENGXIN
 * @date 2024/10/5
 * @project feng-shortlink
 * @description 短链接监控接口层
 **/
public interface ShortLinkStatsService {
    /**
     * 获取单个短链接监控数据
     *
     * @param requestParam 获取短链接监控数据入参
     * @return 短链接监控数据
     */
    ShortLinkStatsRespDTO getShortLinkStats(ShortLinkStatsReqDTO requestParam);
}
