package com.feng.shortlink.admin.common.convention.exception;

import com.feng.shortlink.admin.common.convention.errorcode.BaseErrorCode;
import com.feng.shortlink.admin.common.convention.errorcode.IErrorCode;

import java.util.Optional;

/**
 * @author FENGXIN
 * @date 2024/9/26
 * @project feng-shortlink
 * @description 服务端异常
 **/
public class ServiceException extends AbstractException {
    
    public ServiceException(String message) {
        this(message, null, BaseErrorCode.SERVICE_ERROR);
    }
    
    public ServiceException(IErrorCode errorCode) {
        this(null, errorCode);
    }
    
    public ServiceException(String message, IErrorCode errorCode) {
        this(message, null, errorCode);
    }
    
    public ServiceException(String message, Throwable throwable, IErrorCode errorCode) {
        super(Optional.ofNullable(message).orElse(errorCode.message()), throwable, errorCode);
    }
    
    @Override
    public String toString() {
        return "ServiceException{" +
                "code='" + errorCode + "'," +
                "message='" + errorMessage + "'" +
                '}';
    }
}
