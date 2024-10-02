package com.feng.shortlink.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author FENGXIN
 * @date 2024/10/2
 * @project feng-shortlink
 * @description 短链接不存在跳转页面
 **/
@Controller
public class ShortLinkNotFoundController {
    /**
     * 短链接不存在跳转页面
     */
    @RequestMapping("/page/notfound")
    public String notfound() {
        return "notfound";
    }
}
