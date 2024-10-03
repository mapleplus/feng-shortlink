package com.feng.shortlink.admin.controller;

import com.feng.shortlink.admin.common.convention.result.Result;
import com.feng.shortlink.admin.remote.ShortLinkRemoteService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author FENGXIN
 * @date 2024/10/2
 * @project feng-shortlink
 * @description 获取网站标题控制层
 **/
@RestController
public class TitleController {
    ShortLinkRemoteService shortLinkRemoteService;
    
    /**
     * 获取标题
     *
     * @param url 网站地址
     * @return 网站标题
     */
    @GetMapping("/api/fenglink/v1/admin/title")
    public Result<String> getTitle(@RequestParam("url") String url) {
        shortLinkRemoteService = new ShortLinkRemoteService () {
        };
        return shortLinkRemoteService.getTitle(url);
    }
}
