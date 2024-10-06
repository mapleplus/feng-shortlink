package com.feng.shortlink.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.feng.shortlink.project.dto.request.ShortLinkPageStatsReqDTO;
import com.feng.shortlink.project.dto.request.ShortLinkStatsGroupReqDTO;
import com.feng.shortlink.project.dto.request.ShortLinkStatsReqDTO;
import com.feng.shortlink.project.dto.response.ShortLinkPageStatsRespDTO;
import com.feng.shortlink.project.dto.response.ShortLinkStatsGroupRespDTO;
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
     * @param requestParam 单条短链接监控数据入参
     * @return 单条短链接监控数据
     */
    ShortLinkStatsRespDTO getShortLinkStats(ShortLinkStatsReqDTO requestParam);
    
    /**
     * 分页查询短链接统计
     *
    * @param requestParam 分页短链接监控数据入参
    * @return 分页短链接监控数据
     */
    IPage<ShortLinkPageStatsRespDTO> pageShortLinkStats (ShortLinkPageStatsReqDTO requestParam);
    
    /**
     * 分组查询短链接统计
     *
     * @param requestParam 分组短链接监控数据入参
     * @return 分组短链接监控数据
     */
    ShortLinkStatsGroupRespDTO groupShortLinkStats (ShortLinkStatsGroupReqDTO requestParam);
}
