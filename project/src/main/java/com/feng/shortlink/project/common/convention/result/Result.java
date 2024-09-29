package com.feng.shortlink.project.common.convention.result;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author FENGXIN
 * @date 2024/9/26
 * @project feng-shortlink
 * @description 全局统一返回实体
 **/
@Data
/*
@Accessors(chain = true)：Lombok 注解，用于生成链式 setter 方法
Result<String> result = new Result<>()
    .setCode("0")
    .setMessage("Success")
    .setData("Data");
 */
@Accessors(chain = true)
public class Result<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 5679018624309023727L;
    
    /**
     * 正确返回码
     */
    public static final String SUCCESS_CODE = "0";
    
    /**
     * 返回码
     */
    private String code;
    
    /**
     * 返回消息
     */
    private String message;
    
    /**
     * 响应数据
     */
    private T data;
    
    /**
     * 请求ID
     */
    private String requestId;
    
    public boolean isSuccess() {
        return SUCCESS_CODE.equals(code);
    }
}
