package com.feng.shortlink.project.util;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;

import java.util.Date;
import java.util.Optional;

import static com.feng.shortlink.project.common.constant.ShortLinkConstant.SHORT_LINK_VALID_TIME;

/**
 * @author FENGXIN
 * @date 2024/10/2
 * @project feng-shortlink
 * @description
 **/
public class ShortLinkUtil {
    public static Long getShortLinkValidTime(Date validTime) {
        return Optional.ofNullable(validTime)
                .map(each -> DateUtil.between (new Date (),validTime, DateUnit.MS))
                .orElse(SHORT_LINK_VALID_TIME);
    }
}
