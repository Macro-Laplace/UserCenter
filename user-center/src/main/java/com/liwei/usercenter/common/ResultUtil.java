package com.liwei.usercenter.common;

/**
 * @author Macro-Laplace
 * @version 1.0
 * @date 2022/12/11 12:29
 * @description: TODO
 */
public class ResultUtil {

    /**
     * 成功
     * @param data
     * @param <T>
     * @return
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(0,data,"OK");
    }

    /**
     * 失败,没有data
     * @param error
     * @return
     */
    public static  BaseResponse error(ErrorCode error) {
        return new BaseResponse(error);
    }

    public static  BaseResponse error(ErrorCode error,String message,String description) {
        return new BaseResponse(error,null,message,description);
    }

    public static  BaseResponse error(int code,String message,String description) {
        return new BaseResponse(code,null,message,description);
    }
}
