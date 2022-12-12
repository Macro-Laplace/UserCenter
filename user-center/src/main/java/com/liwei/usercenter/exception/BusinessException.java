package com.liwei.usercenter.exception;

import com.liwei.usercenter.common.ErrorCode;
import lombok.Data;

/**
 * @author Macro-Laplace
 * @version 1.0
 * @date 2022/12/11 13:21
 * @description: TODO
 */
@Data
public class BusinessException extends RuntimeException{

    private final int code;
    private final String description;

    public BusinessException(String message, int code, String description) {
        super(message);
        this.code = code;
        this.description = description;
    }

    public BusinessException(ErrorCode error) {
        super(error.getMessage());
        this.code = error.getCode();
        this.description = error.getDescription();
    }

    public BusinessException(ErrorCode error,String description) {
        super(error.getMessage());
        this.code = error.getCode();
        this.description = description;
    }



}
