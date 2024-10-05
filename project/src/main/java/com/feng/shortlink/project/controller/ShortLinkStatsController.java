package com.feng.shortlink.project.controller;

import com.feng.shortlink.project.common.convention.result.Result;
import com.feng.shortlink.project.common.convention.result.Results;
import com.feng.shortlink.project.dto.request.ShortLinkStatsReqDTO;
import com.feng.shortlink.project.dto.response.ShortLinkStatsRespDTO;
import com.feng.shortlink.project.service.ShortLinkStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    
    @GetMapping("/api/short-link/v1/stats")
    public Result<ShortLinkStatsRespDTO> getShortLinkStats(@RequestBody ShortLinkStatsReqDTO requestParam) {
        return Results.success (shortLinkStatsService.getShortLinkStats (requestParam));
    }
}
