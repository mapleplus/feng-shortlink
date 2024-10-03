package com.feng.shortlink.project.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.feng.shortlink.project.common.convention.result.Result;
import com.feng.shortlink.project.common.convention.result.Results;
import com.feng.shortlink.project.dto.request.ShortLinkRecycleBinRecoverReqDTO;
import com.feng.shortlink.project.dto.request.ShortLinkRecycleBinSaveReqDTO;
import com.feng.shortlink.project.dto.request.ShortLinkRecycleBinPageReqDTO;
import com.feng.shortlink.project.dto.response.ShortLinkPageRespDTO;
import com.feng.shortlink.project.service.RecycleBinService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author FENGXIN
 * @date 2024/10/3
 * @project feng-shortlink
 * @description
 **/
@RestController
@RequiredArgsConstructor
public class RecycleBinController {
    private final RecycleBinService recycleBinService;
    
    /**
     * 保存短链接到回收站
     */
    @PostMapping("/api/fenglink/v1/shortlink/recycle-bin")
    public Result<Void> saveRecycleBin(@RequestBody ShortLinkRecycleBinSaveReqDTO requestParam) {
        recycleBinService.saveRecycleBin(requestParam);
        return Results.success();
    }
    
    /**
     * 分页查询回收站短链接
     */
    @GetMapping("/api/fenglink/v1/shortlink/recycle-bin")
    public Result<IPage<ShortLinkPageRespDTO>> pageRecycleBinShortLink (ShortLinkRecycleBinPageReqDTO requestParam) {
        return Results.success (recycleBinService.pageRecycleBinShortLink(requestParam));
    }
    
    /**
     * 恢复短链接
     */
    @PostMapping("/api/fenglink/v1/shortlink/recycle-bin/recover")
    public Result<Void> recoverShortLink(@RequestBody ShortLinkRecycleBinRecoverReqDTO requestParam) {
        recycleBinService.recoverRecycleBin(requestParam);
        return Results.success();
    }
}
