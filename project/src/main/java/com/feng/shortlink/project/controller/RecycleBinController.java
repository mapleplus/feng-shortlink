package com.feng.shortlink.project.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.feng.shortlink.project.common.convention.result.Result;
import com.feng.shortlink.project.common.convention.result.Results;
import com.feng.shortlink.project.dto.request.RecycleBinSaveReqDTO;
import com.feng.shortlink.project.dto.request.ShortLinkPageReqDTO;
import com.feng.shortlink.project.dto.response.ShortLinkPageRespDTO;
import com.feng.shortlink.project.service.RecycleBinService;
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
    private final RecycleBinService recycleBinService;
    
    /**
     * 保存短链接到回收站
     *
     * @param requestParam 请求参数
     * @return {@code Result<Void> }
     */
    @PostMapping("/api/fenglink/v1/shortlink/recycle-bin")
    public Result<Void> saveRecycleBin(@RequestBody RecycleBinSaveReqDTO requestParam) {
        recycleBinService.saveRecycleBin(requestParam);
        return Results.success();
    }
    
    /**
     * 分页查询回收站短链接
     *
     * @param requestParam 请求参数
     * @return {@code Result<IPage<ShortLinkPageRespDTO>> }
     */
    @GetMapping("/api/fenglink/v1/shortlink/recycle-bin")
    public Result<IPage<ShortLinkPageRespDTO>> pageShortLink (ShortLinkPageReqDTO requestParam) {
        return Results.success (recycleBinService.pageRecycleBinShortLink(requestParam));
    }
}
