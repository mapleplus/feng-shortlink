package com.feng.shortlink.project.common.convention.errorcode;

/**
 * @author FENGXIN
 * @date 2024/9/26
 * @project feng-shortlink
 * @description 异常码接口
 **/
public interface IErrorCode {
    /**
     * 错误码
     */
    String code();
    
    /**
     * 错误信息
     */
    String message();
}
