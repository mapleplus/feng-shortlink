package com.feng.shortlink.admin.remote;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.feng.shortlink.admin.common.convention.result.Result;
import com.feng.shortlink.admin.remote.dto.request.*;
import com.feng.shortlink.admin.remote.dto.response.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author FENGXIN
 * @date 2024/10/10
 * @project feng-shortlink
 * @description 微服务中台调用
 **/
@FeignClient("feng-shortlink-project")
public interface ShortLinkActualRemoteService {
    
    /**
     * 新增短链接
     */
    @PostMapping("/api/fenglink/v1/shortlink")
    Result<ShortLinkSaveRespDTO> saveShortLink (@RequestBody ShortLinkSaveReqDTO requestParam);
    
    /**
     * 处理修改短链接的请求。
     *
     * @param requestParam 包含有关要修改的短链接详细信息的请求参数
     */
    @PostMapping("/api/fenglink/v1/shortlink/update")
    void updateShortLink ( ShortLinkUpdateReqDTO requestParam);
    
    /**
     * 分页查询短链接
     */
    @GetMapping("/api/fenglink/v1/shortlink")
    Result<Page<ShortLinkPageRespDTO>> pageShortLink(@RequestParam("gid") String gid
            , @RequestParam(value = "orderTag",required = false)String orderTag
            );
    
    /**
     * 查询短链接组中短链接的数量
     */
    @GetMapping("/api/fenglink/v1/shortlink/group")
    Result<List<ShortLinkGroupQueryRespDTO>> listShortLinkGroup(@RequestParam("requestParam") List<String> requestParam);
    
     /**
     * 获取标题
     */
     @GetMapping("/api/fenglink/v1/title")
     Result<String> getTitle (@RequestParam("url") String url);
    
     /**
     * 保存短链接到回收站
     */
     @PostMapping("/api/fenglink/v1/shortlink/recycle-bin")
     void saveRecycleBin (ShortLinkRecycleBinSaveReqDTO requestParam);

     /**
     * 分页查询回收站短链接
     */
     @GetMapping("/api/fenglink/v1/shortlink/recycle-bin")
     Result<Page<ShortLinkPageRespDTO>> pageRecycleBinShortLink(@RequestParam("gidList") List<String> gidList
             ,@RequestParam("current") Long current
             ,@RequestParam("size") Long size);

     /**
     * 恢复短链接
     */
     @PostMapping("/api/fenglink/v1/shortlink/recycle-bin/recover")
     void recoverRecycleBin (ShortLinkRecycleBinRecoverReqDTO requestParam);

     /**
     * 移除短链接
     */
     @PostMapping("/api/fenglink/v1/shortlink/recycle-bin/remove")
     void removeRecycleBin (ShortLinkRecycleBinRemoveReqDTO requestParam);

     /**
     * 获取单条短链接监控统计数据
     */
     @GetMapping("/api/fenglink/v1/stats")
     Result<ShortLinkStatsRespDTO> getShortLinkStats (
               @RequestParam("fullShortUrl") String fullShortUrl
             , @RequestParam("gid") String gid
             , @RequestParam("enableStatus") Integer enableStatus
             , @RequestParam("startDate") String startDate
             , @RequestParam("endDate") String endDate);

     /**
     * 分页短链接监控统计
     */
     @GetMapping("/api/fenglink/v1/stats/page")
     Result<Page<ShortLinkPageStatsRespDTO>> pageShortLinkStats (
               @RequestParam("fullShortUrl") String fullShortUrl
             , @RequestParam("gid") String gid
             , @RequestParam("startDate") String startDate
             , @RequestParam("endDate") String endDate
             , @RequestParam("enableStatus") Integer enableStatus
             , @RequestParam("current") Long current
             , @RequestParam("size") Long size
     );

     /**
     * 获取分组短链接监控统计数据
     */
     @GetMapping("/api/fenglink/v1/stats/group")
     Result<ShortLinkStatsRespDTO> groupShortLinkStats (
               @RequestParam("gid") String gid
             , @RequestParam("startDate") String startDate
             , @RequestParam("endDate") String endDate);

     /**
     * 分组分页短链接监控统计
     */
     @GetMapping("/api/fenglink/v1/stats/page/group")
     Result<Page<ShortLinkPageStatsGroupRespDTO>> pageGroupShortLinkStats (
             @RequestParam("gid") String gid
             , @RequestParam("startDate") String startDate
             , @RequestParam("endDate") String endDate
             , @RequestParam("enableStatus") Integer enableStatus
             , @RequestParam("current") Long current
             , @RequestParam("size") Long size
     );
}
