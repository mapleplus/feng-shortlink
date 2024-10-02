package com.feng.shortlink.project.controller;

import com.feng.shortlink.project.common.convention.result.Result;
import com.feng.shortlink.project.common.convention.result.Results;
import com.feng.shortlink.project.service.TitleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author FENGXIN
 * @date 2024/10/2
 * @project feng-shortlink
 * @description 获取网站标题
 **/
@RestController
@RequiredArgsConstructor
public class TitleController {
    private final TitleService titleService;
    
    /**
     * 获取标题
     *
     * @param url 网站地址
     * @return 网站标题
     */
    @GetMapping("/api/fenglink/v1/title")
    public Result<String> getTitle(@RequestParam("url") String url) {
        return Results.success (titleService.getTitle(url));
    }
}
