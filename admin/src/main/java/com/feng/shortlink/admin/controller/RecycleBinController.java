package com.feng.shortlink.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.feng.shortlink.admin.common.convention.result.Result;
import com.feng.shortlink.admin.common.convention.result.Results;
import com.feng.shortlink.admin.remote.ShortLinkService;
import com.feng.shortlink.admin.remote.dto.request.RecycleBinSaveReqDTO;
import com.feng.shortlink.admin.remote.dto.request.ShortLinkPageReqDTO;
import com.feng.shortlink.admin.remote.dto.response.ShortLinkPageRespDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author FENGXIN
 * @date 2024/10/3
 * @project feng-shortlink
 * @description
 **/
@RestController
@RequiredArgsConstructor
public class RecycleBinController {
    private final ShortLinkService shortLinkService = new ShortLinkService () {};
    
    /**
     * 保存短链接到回收站
     *
     * @param requestParam 请求参数
     * @return {@code Result<Void> }
     */
    @PostMapping("/api/fenglink/v1/admin/shortlink/recycle-bin")
    public Result<Void> saveRecycleBin(@RequestBody RecycleBinSaveReqDTO requestParam) {
        shortLinkService.saveRecycleBin(requestParam);
        return Results.success ();
    }
    
    /**
     * 分页查询回收站短链接
     *
     * @param requestParam 请求参数
     * @return {@code Result<IPage<ShortLinkPageRespDTO>> }
     */
    @GetMapping("/api/fenglink/v1/admin/shortlink/recycle-bin")
    public Result<IPage<ShortLinkPageRespDTO>> pageShortLink(@RequestBody ShortLinkPageReqDTO requestParam) {
        return shortLinkService.pageShortLink(requestParam);
    }
}
