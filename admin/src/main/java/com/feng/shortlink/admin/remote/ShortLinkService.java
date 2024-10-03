package com.feng.shortlink.admin.remote;


import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.feng.shortlink.admin.common.convention.result.Result;
import com.feng.shortlink.admin.remote.dto.request.*;
import com.feng.shortlink.admin.remote.dto.response.ShortLinkGroupQueryRespDTO;
import com.feng.shortlink.admin.remote.dto.response.ShortLinkPageRespDTO;
import com.feng.shortlink.admin.remote.dto.response.ShortLinkSaveRespDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author FENGXIN
 */
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
        return JSON.parseObject (response ,new TypeReference<> () {});
    }
    
    /**
     * 处理修改短链接的请求。
     *
     * @param requestParam 包含有关要修改的短链接详细信息的请求参数
     */
    default void updateShortLink ( ShortLinkUpdateReqDTO requestParam) {
        HttpUtil.post ("http://127.0.0.1:8001/api/fenglink/v1/shortlink/update" , JSON.toJSONString (requestParam));
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
    
    /**
     * 查询短链接组中短链接的数量
     *
     * @param requestParam 请求参数
     * @return {@code Result<List<ShortLinkGroupQueryRespDTO>> }
     */
    default Result<List<ShortLinkGroupQueryRespDTO>> listShortLinkGroup( List<String> requestParam){
        Map<String,Object> requestMap = new HashMap<> ();
        requestMap.put("requestParam", requestParam);
        String responsePage = HttpUtil.get ("http://127.0.0.1:8001/api/fenglink/v1/shortlink/group" , requestMap);
        return JSON.parseObject(responsePage, new TypeReference<> (){});
    }
    
    /**
     * 获取标题
     *
     * @param url 网址
     * @return {@code Result<String> 标题}
     */
    default Result<String> getTitle (String url) {
        String response = HttpUtil.get ("http://127.0.0.1:8001/api/fenglink/v1/title?url=" + url);
        /* 自动确定要转换的类型 Result中含有泛型需要说明转换*/
        return JSON.parseObject (response ,new TypeReference<> () {});
    }
    
    /**
     * 保存短链接到回收站
     *
     * @param requestParam 请求参数
     */
    default void saveRecycleBin (RecycleBinSaveReqDTO requestParam) {
        HttpUtil.post ("http://127.0.0.1:8001/api/fenglink/v1/shortlink/recycle-bin" , JSON.toJSONString (requestParam));
    }
    
    /**
     * 分页查询回收站短链接
     *
     * @param requestParam 请求参数
     * @return {@code Result<IPage<ShortLinkPageRespDTO>> }
     */
    default Result<IPage<ShortLinkPageRespDTO>> pageRecycleBinShortLink(ShortLinkRecycleBinPageReqDTO requestParam){
        Map<String,Object> requestMap = new HashMap<> ();
        requestMap.put("gidList", requestParam.getGidList ());
        requestMap.put("current", requestParam.getCurrent());
        requestMap.put("size", requestParam.getSize());
        System.out.println (JSON.toJSONString (requestMap));
        String responsePage = HttpUtil.get ("http://127.0.0.1:8001/api/fenglink/v1/shortlink/recycle-bin" , requestMap);
        return JSON.parseObject(responsePage, new TypeReference<> (){});
    }
    
}
