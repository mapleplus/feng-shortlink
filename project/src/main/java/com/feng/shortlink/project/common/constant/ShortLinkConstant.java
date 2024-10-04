package com.feng.shortlink.project.common.constant;

import lombok.Data;

/**
 * @author FENGXIN
 * @date 2024/10/2
 * @project feng-shortlink
 * @description 短链接常量
 **/
@Data
public class ShortLinkConstant {
    
    /**
     * 短链接有效时间
     */
    public static final Long SHORT_LINK_VALID_TIME = 2592000000L;
    public static final String SHORT_LINK_LOCALE_STATS_URL = "https://restapi.amap.com/v3/ip";
}
