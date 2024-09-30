package com.feng.shortlink.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.feng.shortlink.admin.common.convention.result.Result;
import com.feng.shortlink.admin.remote.dto.ShortLinkService;
import com.feng.shortlink.admin.remote.dto.request.ShortLinkPageReqDTO;
import com.feng.shortlink.admin.remote.dto.request.ShortLinkSaveReqDTO;
import com.feng.shortlink.admin.remote.dto.response.ShortLinkPageRespDTO;
import com.feng.shortlink.admin.remote.dto.response.ShortLinkSaveRespDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author FENGXIN
 * @date 2024/9/30
 * @project feng-shortlink
 * @description
 **/
@RestController
public class ShortLinkController {
    ShortLinkService shortLinkService;
    
    /**
     * 新增短链接
     *
     * @param requestParam 请求参数
     * @return {@code Result<ShortLinkSaveRespDTO> }
     */
    @PostMapping("/api/fenglink/v1/admin/shortlink")
    public Result<ShortLinkSaveRespDTO> saveShortLink (@RequestBody ShortLinkSaveReqDTO requestParam){
        shortLinkService = new ShortLinkService () {};
        return shortLinkService.saveShortLink(requestParam);
    }
    /**
     * 页面短链接
     *
     * @param requestParam 请求参数
     * @return {@code Result<IPage<ShortLinkPageRespDTO>> }
     */
    @GetMapping("/api/fenglink/v1/admin/shortlink")
    public Result<IPage<ShortLinkPageRespDTO>> pageShortLink(@RequestBody ShortLinkPageReqDTO requestParam) {
        shortLinkService = new ShortLinkService () {};
        return shortLinkService.pageShortLink(requestParam);
    }
}
