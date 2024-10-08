package com.feng.shortlink.project.handler;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.feng.shortlink.project.common.convention.result.Result;
import com.feng.shortlink.project.dto.request.ShortLinkSaveReqDTO;
import com.feng.shortlink.project.dto.response.ShortLinkSaveRespDTO;

/**
 * @author FENGXIN
 * @date 2024/10/8
 * @project feng-shortlink
 * @description 如果触发风控，设置降级策略
 **/
public class CustomBlockHandler {
    public static Result<ShortLinkSaveRespDTO> createShortLinkBlockHandlerMethod(ShortLinkSaveReqDTO requestParam, BlockException exception) {
        return new Result<ShortLinkSaveRespDTO>().setCode("B100000").setMessage("当前访问网站人数过多，请稍后再试...");
    }
}
