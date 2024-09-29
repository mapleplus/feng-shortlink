package com.feng.shortlink.project.controller;

import com.feng.shortlink.project.common.convention.result.Result;
import com.feng.shortlink.project.common.convention.result.Results;
import com.feng.shortlink.project.dto.request.ShortLinkSaveReqDTO;
import com.feng.shortlink.project.dto.response.ShortLinkSaveRespDTO;
import com.feng.shortlink.project.service.ShortLinkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
     * 处理保存新短链接的请求。
     *
     * @param requestParam 包含有关要保存的短链接详细信息的请求参数
     * @return 包含已保存短链接详细信息的响应
     */
    @PostMapping("/api/fenglink/v1/shortlink")
    public Result<ShortLinkSaveRespDTO> saveShortLink (@RequestBody ShortLinkSaveReqDTO requestParam) {
        return Results.success (shortLinkService.saveShortLink (requestParam));
    }
}
