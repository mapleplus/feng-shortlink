package com.feng.shortlink.project.common.convention.exception;


import com.feng.shortlink.project.common.convention.errorcode.BaseErrorCode;
import com.feng.shortlink.project.common.convention.errorcode.IErrorCode;

/**
 * @author FENGXIN
 * @date 2024/9/26
 * @project feng-shortlink
 * @description 远程调用异常
 **/
public class RemoteException extends AbstractException {
    
    public RemoteException(String message) {
        this(message, null, BaseErrorCode.REMOTE_ERROR);
    }
    
    public RemoteException(String message, IErrorCode errorCode) {
        this(message, null, errorCode);
    }
    
    public RemoteException(String message, Throwable throwable, IErrorCode errorCode) {
        super(message, throwable, errorCode);
    }
    
    @Override
    public String toString() {
        return "RemoteException{" +
                "code='" + errorCode + "'," +
                "message='" + errorMessage + "'" +
                '}';
    }
}
