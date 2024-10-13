package com.feng.shortlink.project.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.feng.shortlink.project.common.convention.result.Result;
import com.feng.shortlink.project.common.convention.result.Results;
import com.feng.shortlink.project.dto.request.ShortLinkPageStatsGroupReqDTO;
import com.feng.shortlink.project.dto.request.ShortLinkPageStatsReqDTO;
import com.feng.shortlink.project.dto.request.ShortLinkStatsGroupReqDTO;
import com.feng.shortlink.project.dto.request.ShortLinkStatsReqDTO;
import com.feng.shortlink.project.dto.response.ShortLinkPageStatsGroupRespDTO;
import com.feng.shortlink.project.dto.response.ShortLinkPageStatsRespDTO;
import com.feng.shortlink.project.dto.response.ShortLinkStatsGroupRespDTO;
import com.feng.shortlink.project.dto.response.ShortLinkStatsRespDTO;
import com.feng.shortlink.project.service.ShortLinkStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author FENGXIN
 * @date 2024/10/5
 * @project feng-shortlink
 * @description 短链接监控控制层
 **/
@RestController
@RequiredArgsConstructor
public class ShortLinkStatsController {
    
    private final ShortLinkStatsService shortLinkStatsService;
    
    /**
     * 获取单条短链接监控统计数据
     */
    @GetMapping("/api/fenglink/v1/stats")
    public Result<ShortLinkStatsRespDTO> getShortLinkStats(ShortLinkStatsReqDTO requestParam) {
        return Results.success (shortLinkStatsService.getShortLinkStats (requestParam));
    }
    
    /**
     * 分页短链接监控统计
     */
    @GetMapping("/api/fenglink/v1/stats/page")
    public Result<IPage<ShortLinkPageStatsRespDTO>> pageShortLinkStats(ShortLinkPageStatsReqDTO requestParam) {
        IPage<ShortLinkPageStatsRespDTO> shortLinkPageStatsRespDTOIPage = shortLinkStatsService.pageShortLinkStats (requestParam);
        return Results.success (shortLinkPageStatsRespDTOIPage);
    }
    
    /**
     * 获取分组短链接监控统计数据
     */
    @GetMapping("/api/fenglink/v1/stats/group")
    public Result<ShortLinkStatsGroupRespDTO> groupShortLinkStats(ShortLinkStatsGroupReqDTO requestParam) {
        return Results.success (shortLinkStatsService.groupShortLinkStats (requestParam));
    }
    
    /**
     * 分组分页短链接监控统计
     */
    @GetMapping("/api/fenglink/v1/stats/page/group")
    public Result<IPage<ShortLinkPageStatsGroupRespDTO>> pageGroupShortLinkStats(ShortLinkPageStatsGroupReqDTO requestParam) {
        return Results.success (shortLinkStatsService.pageGroupShortLinkStats (requestParam));
    }
}
