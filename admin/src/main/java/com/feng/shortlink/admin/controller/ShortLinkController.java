package com.feng.shortlink.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.feng.shortlink.admin.common.convention.result.Result;
import com.feng.shortlink.admin.common.convention.result.Results;
import com.feng.shortlink.admin.remote.ShortLinkRemoteService;
import com.feng.shortlink.admin.remote.dto.request.ShortLinkPageReqDTO;
import com.feng.shortlink.admin.remote.dto.request.ShortLinkSaveReqDTO;
import com.feng.shortlink.admin.remote.dto.request.ShortLinkStatsReqDTO;
import com.feng.shortlink.admin.remote.dto.request.ShortLinkUpdateReqDTO;
import com.feng.shortlink.admin.remote.dto.response.ShortLinkPageRespDTO;
import com.feng.shortlink.admin.remote.dto.response.ShortLinkSaveRespDTO;
import com.feng.shortlink.admin.remote.dto.response.ShortLinkStatsRespDTO;
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
     *
     * @param requestParam 请求参数
     * @return {@code Result<ShortLinkSaveRespDTO> }
     */
    @PostMapping("/api/fenglink/v1/admin/shortlink")
    public Result<ShortLinkSaveRespDTO> saveShortLink (@RequestBody ShortLinkSaveReqDTO requestParam){
        return shortLinkRemoteService.saveShortLink(requestParam);
    }
    
    /**
     * 处理修改短链接的请求。
     *
     * @param requestParam 包含有关要修改的短链接详细信息的请求参数
     */
    @PostMapping("/api/fenglink/v1/admin/shortlink/update")
    public Result<Void> updateShortLink (@RequestBody ShortLinkUpdateReqDTO requestParam) {
        shortLinkRemoteService.updateShortLink (requestParam);
        return Results.success ();
    }
    
    /**
     * 分页查询短链接
     *
     * @param requestParam 请求参数
     * @return {@code Result<IPage<ShortLinkPageRespDTO>> }
     */
    @GetMapping("/api/fenglink/v1/admin/shortlink")
    public Result<IPage<ShortLinkPageRespDTO>> pageShortLink(@RequestBody ShortLinkPageReqDTO requestParam) {
        return shortLinkRemoteService.pageShortLink(requestParam);
    }
    
    @GetMapping("/api/short-link/v1/admin/stats")
    public Result<ShortLinkStatsRespDTO> getShortLinkStats(@RequestBody ShortLinkStatsReqDTO requestParam) {
        return shortLinkRemoteService.getShortLinkStats (requestParam);
    }
}
