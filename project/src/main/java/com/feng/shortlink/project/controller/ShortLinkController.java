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
     * Handles the request to save a new short link.
     *
     * @param requestParam the request parameters that include details about the short link to be saved
     * @return the response containing the details of the saved short link
     */
    @PostMapping("/api/fenglink/v1/shortlink")
    public Result<ShortLinkSaveRespDTO> saveShortLink(@RequestBody ShortLinkSaveReqDTO requestParam) {
        return Results.success(shortLinkService.saveShortLink(requestParam));
    }
}
