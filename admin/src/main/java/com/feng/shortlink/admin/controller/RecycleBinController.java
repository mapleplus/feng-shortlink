package com.feng.shortlink.admin.controller;

import com.feng.shortlink.admin.common.convention.result.Result;
import com.feng.shortlink.admin.common.convention.result.Results;
import com.feng.shortlink.admin.remote.ShortLinkService;
import com.feng.shortlink.admin.remote.dto.request.RecycleBinSaveReqDTO;
import lombok.RequiredArgsConstructor;
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
    
    @PostMapping("/api/fenglink/v1/admin/shortlink/recyclebin/save")
    public Result<Void> saveRecycleBin(@RequestBody RecycleBinSaveReqDTO requestParam) {
        shortLinkService.saveRecycleBin(requestParam);
        return Results.success ();
    }
}
