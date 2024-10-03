package com.feng.shortlink.admin.controller;

import com.feng.shortlink.admin.common.convention.result.Result;
import com.feng.shortlink.admin.common.convention.result.Results;
import com.feng.shortlink.admin.dto.request.ShortLinkGroupSortDTO;
import com.feng.shortlink.admin.dto.request.ShortLinkGroupUpdateDTO;
import com.feng.shortlink.admin.dto.response.GroupRespDTO;
import com.feng.shortlink.admin.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
     * 处理基于给定组ID保存组信息的POST请求。
     *
     * @param requestParam 包含组信息的ShortLinkGroupSaveDTO对象
     * @return 指示操作成功的Result对象
     */
    @PostMapping("/api/fenglink/v1/admin/group")
    public Result<Void> saveGroupByGid (@RequestParam("name") String requestParam) {
        // 调用 groupService 保存组信息
        groupService.saveGroupByGid (requestParam);
        // 返回成功结果
        return Results.success ();
    }
    
    /**
     * 处理HTTP GET请求以检索组列表。
     *
     * @return 包含GroupRespDTO对象列表的Result对象
     */
    @GetMapping("/api/fenglink/v1/admin/group")
    public Result<List<GroupRespDTO>> getGroup () {
        return Results.success (groupService.getGroup ());
    }
    
    /**
     * 更新短链接组的详细信息。
     *
     * @param requestParam 包含要更新的组的详细信息的数据传输对象
     * @return 指示操作成功或失败的Result对象
     */
    @PutMapping("/api/fenglink/v1/admin/group")
    public Result<Void> updateGroup (@RequestBody ShortLinkGroupUpdateDTO requestParam) {
        groupService.updateGroup (requestParam);
        return Results.success ();
    }
    
    /**
     * 删除以组ID（gid）标识的组。
     *
     * @param gid 要删除的组ID
     * @return 指示删除操作成功或失败的结果
     */
    @DeleteMapping("/api/fenglink/v1/admin/group")
    public Result<Void> deleteGroup (@RequestParam String gid) {
        groupService.deleteGroup (gid);
        return Results.success ();
    }
    
    /**
     * 排序一组ShortLinkGroupSortDTO对象的端点。
     *
     * @param requestParam 包含组排序信息的ShortLinkGroupSortDTO列表
     * @return 指示操作成功的Result<Void>
     */
    @PostMapping("/api/fenglink/v1/admin/group/sort")
    public Result<Void> sortGroup (@RequestBody List<ShortLinkGroupSortDTO> requestParam) {
        groupService.sortGroup (requestParam);
        return Results.success ();
    }
}
