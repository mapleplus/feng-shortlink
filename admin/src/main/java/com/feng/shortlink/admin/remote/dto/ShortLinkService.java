package com.feng.shortlink.admin.remote.dto;


import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.feng.shortlink.admin.common.convention.result.Result;
import com.feng.shortlink.admin.remote.dto.request.ShortLinkPageReqDTO;
import com.feng.shortlink.admin.remote.dto.request.ShortLinkSaveReqDTO;
import com.feng.shortlink.admin.remote.dto.response.ShortLinkPageRespDTO;
import com.feng.shortlink.admin.remote.dto.response.ShortLinkSaveRespDTO;

import java.util.HashMap;
import java.util.Map;

/**
 * @author FENGXIN
 * @date 2024/9/30
 * @project feng-shortlink
 * @description
 **/
public interface ShortLinkService {
    
    /**
     * 新增短链接
     *
     * @param requestParam 请求参数
     * @return {@code Result<ShortLinkSaveRespDTO> }
     */
    default Result<ShortLinkSaveRespDTO> saveShortLink (ShortLinkSaveReqDTO requestParam){
        String response = HttpUtil.post ("http://127.0.0.1:8001/api/fenglink/v1/shortlink" , JSON.toJSONString (requestParam));
        /* 自动确定要转换的类型 Result中含有泛型需要说明转换*/
        return JSON.parseObject (response ,new TypeReference<> () {
        });
    }
    
    
    /**
     * 分页查询短链接
     *
     * @param requestParam 请求参数
     * @return {@code Result<IPage<ShortLinkPageRespDTO>> }
     */
    default Result<IPage<ShortLinkPageRespDTO>> pageShortLink(ShortLinkPageReqDTO requestParam){
        Map<String,Object> requestMap = new HashMap<> ();
        requestMap.put("gid", requestParam.getGid());
        requestMap.put("current", requestParam.getCurrent());
        requestMap.put("size", requestParam.getSize());
        String responsePage = HttpUtil.get ("http://127.0.0.1:8001/api/fenglink/v1/shortlink" , requestMap);
        return JSON.parseObject(responsePage, new TypeReference<> (){});
    }
    
    
}
