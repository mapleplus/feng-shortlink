package com.feng.shortlink.admin.common.convention.exception;

import com.feng.shortlink.admin.common.convention.errorcode.BaseErrorCode;
import com.feng.shortlink.admin.common.convention.errorcode.IErrorCode;

/**
 * @author FENGXIN
 * @date 2024/9/26
 * @project feng-shortlink
 * @description 客户端异常
 **/
public class ClientException extends AbstractException {
    
    public ClientException(IErrorCode errorCode) {
        this(null, null, errorCode);
    }
    
    public ClientException(String message) {
        this(message, null, BaseErrorCode.CLIENT_ERROR);
    }
    
    public ClientException(String message, IErrorCode errorCode) {
        this(message, null, errorCode);
    }
    
    public ClientException(String message, Throwable throwable, IErrorCode errorCode) {
        super(message, throwable, errorCode);
    }
    
    @Override
    public String toString() {
        return "ClientException{" +
                "code='" + errorCode + "'," +
                "message='" + errorMessage + "'" +
                '}';
    }
}
