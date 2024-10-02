package com.feng.shortlink.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.feng.shortlink.project.dao.entity.ShortLinkDO;
import com.feng.shortlink.project.dto.request.ShortLinkPageReqDTO;
import com.feng.shortlink.project.dto.request.ShortLinkSaveReqDTO;
import com.feng.shortlink.project.dto.request.ShortLinkUpdateReqDTO;
import com.feng.shortlink.project.dto.response.ShortLinkGroupQueryRespDTO;
import com.feng.shortlink.project.dto.response.ShortLinkPageRespDTO;
import com.feng.shortlink.project.dto.response.ShortLinkSaveRespDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * @author FENGXIN
 * @date 2024/9/29
 * @project feng-shortlink
 * @description 短链接业务接口
 **/
public interface ShortLinkService extends IService<ShortLinkDO> {
    /**
     * 根据提供的请求参数保存短链接。
     *
     * @param requestParam 包含要保存的短链接详细信息的请求参数
     * @return 包含保存的短链接详细信息的响应
     */
    ShortLinkSaveRespDTO saveShortLink (ShortLinkSaveReqDTO requestParam);
    
    /**
     * 分页查询短链接
     *
     * @param requestParam 请求参数
     * @return {@code IPage<ShortLinkPageRespDTO> }
     */
    IPage<ShortLinkPageRespDTO> pageShortLink (ShortLinkPageReqDTO requestParam);
    
    /**
     * 列出短链接组
     *
     * @param requestParam 请求参数
     * @return {@code List<ShortLinkGroupQueryRespDTO> }
     */
    List<ShortLinkGroupQueryRespDTO> listShortLinkGroup (List<String> requestParam);
    
    /**
     * 更新短链接
     *
     * @param requestParam 请求参数
     */
    void updateShortLink (ShortLinkUpdateReqDTO requestParam);
    
    /**
     * 跳转链接
     *
     * @param shortLink 短链接
     * @param request 请求
     * @param response  响应
     */
    void restoreLink (String shortLink , HttpServletRequest request , HttpServletResponse response);
}
