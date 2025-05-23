package com.feng.shortlink.project.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.feng.shortlink.project.common.convention.result.Result;
import com.feng.shortlink.project.common.convention.result.Results;
import com.feng.shortlink.project.dto.request.ShortLinkPageReqDTO;
import com.feng.shortlink.project.dto.request.ShortLinkSaveReqDTO;
import com.feng.shortlink.project.dto.request.ShortLinkUpdateReqDTO;
import com.feng.shortlink.project.dto.response.ShortLinkGroupQueryRespDTO;
import com.feng.shortlink.project.dto.response.ShortLinkPageRespDTO;
import com.feng.shortlink.project.dto.response.ShortLinkSaveRespDTO;
import com.feng.shortlink.project.handler.CustomBlockHandler;
import com.feng.shortlink.project.service.ShortLinkService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author FENGXIN
 * @date 2024/9/29
 * @project feng-shortlink
 * @description 短链接接口层
 **/
@RestController
@Slf4j
@RequiredArgsConstructor
public class ShortLinkController {
    private final ShortLinkService shortLinkService;
    
    /**
     * 处理创建新短链接的请求。
     *
     * @param requestParam 包含有关要保存的短链接详细信息的请求参数
     * @return 包含已保存短链接详细信息的响应
     */
    @PostMapping("/api/fenglink/v1/shortlink")
    @SentinelResource(
            value = "create_short-link",
            blockHandler = "createShortLinkBlockHandlerMethod",
            blockHandlerClass = CustomBlockHandler.class
    )
    public Result<ShortLinkSaveRespDTO> saveShortLink (@RequestBody ShortLinkSaveReqDTO requestParam) {
        return Results.success (shortLinkService.saveShortLink (requestParam));
    }
    
    /**
     * 处理修改短链接的请求。
     *
     * @param requestParam 包含有关要修改的短链接详细信息的请求参数
     */
    @PostMapping("/api/fenglink/v1/shortlink/update")
    public void updateShortLink (@RequestBody ShortLinkUpdateReqDTO requestParam) {
        shortLinkService.updateShortLink (requestParam);
    }
    
    /**
     * 分页查询短链接
     *
     * @param requestParam 请求参数
     * @return {@code Result<IPage<ShortLinkPageRespDTO>> }
     */
    @GetMapping("/api/fenglink/v1/shortlink")
    public Result<IPage<ShortLinkPageRespDTO>> pageShortLink ( ShortLinkPageReqDTO requestParam) {
        return Results.success (shortLinkService.pageShortLink(requestParam));
    }
    
    /**
     * 跳转链接
     *
     * @param shortLink 短链接
     * @param request 请求
     * @param response  响应
     */
    @GetMapping("/{short-link}")
    public void restoreLink (@PathVariable("short-link") String shortLink, HttpServletRequest request, HttpServletResponse response) {
        shortLinkService.restoreLink(shortLink,request,response);
    }
    /**
     * 查询短链接组中短链接的数量
     *
     * @param requestParam 请求参数
     * @return {@code Result<List<ShortLinkGroupQueryRespDTO>> }
     */
    @GetMapping("/api/fenglink/v1/shortlink/group")
    public Result<List<ShortLinkGroupQueryRespDTO>> listShortLinkGroup (@RequestParam("requestParam") List<String> requestParam) {
        return Results.success (shortLinkService.listShortLinkGroup(requestParam));
    }
}
