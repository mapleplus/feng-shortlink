package com.feng.shortlink.admin.controller;

import com.feng.shortlink.admin.common.convention.result.Result;
import com.feng.shortlink.admin.common.convention.result.Results;
import com.feng.shortlink.admin.dto.request.SaveShortLinkGroupDTO;
import com.feng.shortlink.admin.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author FENGXIN
 * @date 2024/9/28
 * @project feng-shortlink
 * @description
 **/
@RestController
@RequiredArgsConstructor
public class GroupController {
    private final GroupService groupService;
    
    @PostMapping("/api/fenglink/v1/group")
    public Result<Void> saveGroupByGid (@RequestBody SaveShortLinkGroupDTO requestParams) {
        groupService.saveGroupByGid(requestParams);
        return Results.success();
    }
}
