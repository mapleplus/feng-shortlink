package com.feng.shortlink.admin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.feng.shortlink.admin.common.convention.result.Result;
import com.feng.shortlink.admin.common.convention.result.Results;
import com.feng.shortlink.admin.remote.ShortLinkActualRemoteService;
import com.feng.shortlink.admin.remote.dto.request.*;
import com.feng.shortlink.admin.remote.dto.response.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author FENGXIN
 * @date 2024/9/30
 * @project feng-shortlink
 * @description 短链接控制层
 **/
@RestController
@RequiredArgsConstructor
public class ShortLinkController {
    private final ShortLinkActualRemoteService shortLinkActualRemoteService;
    /**
     * 新增短链接
     */
    @PostMapping("/api/fenglink/v1/admin/shortlink")
    public Result<ShortLinkSaveRespDTO> saveShortLink (@RequestBody ShortLinkSaveReqDTO requestParam){
        return shortLinkActualRemoteService.saveShortLink(requestParam);
    }
    
    /**
     * 处理修改短链接的请求。
     */
    @PostMapping("/api/fenglink/v1/admin/shortlink/update")
    public Result<Void> updateShortLink (@RequestBody ShortLinkUpdateReqDTO requestParam) {
        shortLinkActualRemoteService.updateShortLink (requestParam);
        return Results.success ();
    }
    
    /**
     * 分页查询短链接
     */
    @GetMapping("/api/fenglink/v1/admin/shortlink")
    public Result<Page<ShortLinkPageRespDTO>> pageShortLink( ShortLinkPageReqDTO requestParam) {
        return shortLinkActualRemoteService.pageShortLink(requestParam.getGid (),requestParam.getOrderTag ());
    }
    
    /**
     * 获取单条短链接监控统计数据
     */
    @GetMapping("/api/fenglink/v1/admin/stats")
    public Result<ShortLinkStatsRespDTO> getShortLinkStats( ShortLinkStatsReqDTO requestParam) {
        return shortLinkActualRemoteService.getShortLinkStats (requestParam.getFullShortUrl (),requestParam.getGid (),requestParam.getStartDate (),requestParam.getEndDate ());
    }
    
    /**
     * 分页短链接监控统计
     */
    @GetMapping("/api/fenglink/v1/admin/stats/page")
    public Result<Page<ShortLinkPageStatsRespDTO>> pageShortLinkStats( ShortLinkPageStatsReqDTO requestParam) {
        return shortLinkActualRemoteService.pageShortLinkStats (requestParam.getFullShortUrl (),requestParam.getGid (),requestParam.getStartDate (),requestParam.getEndDate ());
    }
    
    /**
     * 获取分组短链接监控统计数据
     */
    @GetMapping("/api/fenglink/v1/admin/stats/group")
    public Result<ShortLinkStatsRespDTO> groupShortLinkStats( ShortLinkStatsGroupReqDTO requestParam) {
        return shortLinkActualRemoteService.groupShortLinkStats (requestParam.getGid (),requestParam.getStartDate (),requestParam.getEndDate ());
    }
    
    /**
     * 分组分页短链接监控统计
     */
    @GetMapping("/api/fenglink/v1/admin/stats/page/group")
    public Result<Page<ShortLinkPageStatsGroupRespDTO>> pageGroupShortLinkStats( ShortLinkPageStatsGroupReqDTO requestParam) {
        return shortLinkActualRemoteService.pageGroupShortLinkStats (requestParam.getGid (),requestParam.getStartDate (),requestParam.getEndDate ());
    }
}
