package com.feng.shortlink.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.feng.shortlink.admin.common.convention.result.Result;
import com.feng.shortlink.admin.common.convention.result.Results;
import com.feng.shortlink.admin.remote.ShortLinkRemoteService;
import com.feng.shortlink.admin.remote.dto.request.*;
import com.feng.shortlink.admin.remote.dto.response.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author FENGXIN
 * @date 2024/9/30
 * @project feng-shortlink
 * @description 短链接控制层
 **/
@RestController
public class ShortLinkController {
    ShortLinkRemoteService shortLinkRemoteService = new ShortLinkRemoteService () {};
    
    /**
     * 新增短链接
     */
    @PostMapping("/api/fenglink/v1/admin/shortlink")
    public Result<ShortLinkSaveRespDTO> saveShortLink (@RequestBody ShortLinkSaveReqDTO requestParam){
        return shortLinkRemoteService.saveShortLink(requestParam);
    }
    
    /**
     * 处理修改短链接的请求。
     */
    @PostMapping("/api/fenglink/v1/admin/shortlink/update")
    public Result<Void> updateShortLink (@RequestBody ShortLinkUpdateReqDTO requestParam) {
        shortLinkRemoteService.updateShortLink (requestParam);
        return Results.success ();
    }
    
    /**
     * 分页查询短链接
     */
    @GetMapping("/api/fenglink/v1/admin/shortlink")
    public Result<IPage<ShortLinkPageRespDTO>> pageShortLink(@RequestBody ShortLinkPageReqDTO requestParam) {
        return shortLinkRemoteService.pageShortLink(requestParam);
    }
    
    /**
     * 获取单条短链接监控统计数据
     */
    @GetMapping("/api/short-link/v1/admin/stats")
    public Result<ShortLinkStatsRespDTO> getShortLinkStats(@RequestBody ShortLinkStatsReqDTO requestParam) {
        return shortLinkRemoteService.getShortLinkStats (requestParam);
    }
    
    /**
     * 分页短链接监控统计
     */
    @GetMapping("/api/short-link/v1/admin/stats/page")
    public Result<IPage<ShortLinkPageStatsRespDTO>> pageShortLinkStats(@RequestBody ShortLinkPageStatsReqDTO requestParam) {
        return shortLinkRemoteService.pageShortLinkStats (requestParam);
    }
    
    /**
     * 获取分组短链接监控统计数据
     */
    @GetMapping("/api/short-link/v1/admin/stats/group")
    public Result<ShortLinkStatsRespDTO> groupShortLinkStats(@RequestBody ShortLinkStatsGroupReqDTO requestParam) {
        return shortLinkRemoteService.groupShortLinkStats (requestParam);
    }
    
    /**
     * 分组分页短链接监控统计
     */
    @GetMapping("/api/short-link/v1/admin/stats/page/group")
    public Result<IPage<ShortLinkPageStatsGroupRespDTO>> pageGroupShortLinkStats(@RequestBody ShortLinkPageStatsGroupReqDTO requestParam) {
        return shortLinkRemoteService.pageGroupShortLinkStats (requestParam);
    }
}
