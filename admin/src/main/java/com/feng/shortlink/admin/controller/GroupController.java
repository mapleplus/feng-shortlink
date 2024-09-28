package com.feng.shortlink.admin.controller;

import com.feng.shortlink.admin.common.convention.result.Result;
import com.feng.shortlink.admin.common.convention.result.Results;
import com.feng.shortlink.admin.dto.request.SaveShortLinkGroupDTO;
import com.feng.shortlink.admin.dto.response.GroupRespDTO;
import com.feng.shortlink.admin.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    
    /**
     * Handles the POST request to save group information based on the given group ID.
     *
     * @param requestParams the group information encapsulated in a SaveShortLinkGroupDTO object
     * @return a Result object indicating the success of the operation
     */
    @PostMapping("/api/fenglink/v1/group")
    public Result<Void> saveGroupByGid (@RequestBody SaveShortLinkGroupDTO requestParams) {
        // 调用 groupService 保存组信息
        groupService.saveGroupByGid (requestParams);
        // 返回成功结果
        return Results.success ();
    }
    
    @GetMapping("/api/fenglink/v1/group")
    public Result<List<GroupRespDTO>> getGroup () {
        return Results.success (groupService.getGroup ());
    }
}
