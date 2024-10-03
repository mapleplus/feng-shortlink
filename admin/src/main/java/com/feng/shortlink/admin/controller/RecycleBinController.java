package com.feng.shortlink.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.feng.shortlink.admin.common.convention.result.Result;
import com.feng.shortlink.admin.common.convention.result.Results;
import com.feng.shortlink.admin.remote.ShortLinkRemoteService;
import com.feng.shortlink.admin.remote.dto.request.ShortLinkRecycleBinPageReqDTO;
import com.feng.shortlink.admin.remote.dto.request.ShortLinkRecycleBinRecoverReqDTO;
import com.feng.shortlink.admin.remote.dto.request.ShortLinkRecycleBinSaveReqDTO;
import com.feng.shortlink.admin.remote.dto.response.ShortLinkPageRespDTO;
import com.feng.shortlink.admin.service.impl.RecycleBinServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author FENGXIN
 * @date 2024/10/3
 * @project feng-shortlink
 * @description 回收站分组控制层
 **/
@RestController
@RequiredArgsConstructor
public class RecycleBinController {
    private final ShortLinkRemoteService shortLinkRemoteService = new ShortLinkRemoteService () {};
    private final RecycleBinServiceImpl recycleBinService;
    
    /**
     * 保存短链接到回收站
     */
    @PostMapping("/api/fenglink/v1/admin/shortlink/recycle-bin")
    public Result<Void> saveRecycleBin(@RequestBody ShortLinkRecycleBinSaveReqDTO requestParam) {
        shortLinkRemoteService.saveRecycleBin(requestParam);
        return Results.success ();
    }
    
    /**
     * 分页查询回收站短链接
     */
    @GetMapping("/api/fenglink/v1/admin/shortlink/recycle-bin")
    public Result<IPage<ShortLinkPageRespDTO>> pageShortLink(@RequestBody ShortLinkRecycleBinPageReqDTO requestParam) {
        return recycleBinService.pageRecycleBinShortLink(requestParam);
    }
    
    /**
     * 恢复短链接
     */
    @PostMapping("/api/fenglink/v1/admin/shortlink/recycle-bin/recover")
    public Result<Void> recoverShortLink(@RequestBody ShortLinkRecycleBinRecoverReqDTO requestParam) {
        shortLinkRemoteService.recoverRecycleBin(requestParam);
        return Results.success();
    }
}
