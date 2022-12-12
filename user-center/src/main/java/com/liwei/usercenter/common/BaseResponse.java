package com.liwei.usercenter.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Macro-Laplace
 * @version 1.0
 * @date 2022/12/11 12:20
 * @description: 通用返回类
 */
@Data
public class BaseResponse<T> implements Serializable {
    private static final long serialVersionUID = -2352677024950242420L;
    private int code;
    private T data;
    private  String message;
    private  String description;


    public BaseResponse(int code, T data, String massage,String description) {
        this.code = code;
        this.data = data;
        this.message = massage;
        this.description = description;
    }

    public BaseResponse(int code, T data,String message) {
        this(code, data, message,"");
    }

    public BaseResponse(int code, T data) {
        this(code, data, "","");
    }

    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(),null,errorCode.getMessage(),errorCode.getDescription());
    }

    public BaseResponse(ErrorCode errorCode,String message,String description) {
        this(errorCode.getCode(),null,message,description);
    }
    public BaseResponse(ErrorCode errorCode,T data,String message,String description) {
        this(errorCode.getCode(),data,message,description);
    }

    public BaseResponse(ErrorCode errorCode,String description) {
        this(errorCode.getCode(),null,errorCode.getMessage(),description);
    }

}
