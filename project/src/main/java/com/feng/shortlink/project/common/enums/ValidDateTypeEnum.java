package com.feng.shortlink.project.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author FENGXIN
 * @date 2024/10/1
 * @project feng-shortlink
 * @description 短链接有效期类型枚举
 **/

@Getter
@RequiredArgsConstructor
public enum ValidDateTypeEnum {
    
    /**
     * 永久有效
     */
    PERMANENT(0),
    /**
     * 用户自定义
     */
    CUSTOMER(1);
    private final int value;
}
