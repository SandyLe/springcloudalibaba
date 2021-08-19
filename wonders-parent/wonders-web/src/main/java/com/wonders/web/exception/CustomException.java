package com.wonders.web.exception;


import com.wonders.web.result.ResultCode;

/**
 * @ClassName：CustomException
 * @Description：自定义异常
 * @Author：wuxx
 * @Date：2020/3/11 12:14
 */
public class CustomException extends RuntimeException {
    private static final long serialVersionUID = 4564124491192825748L;

    private int code;

    public CustomException() {
        super();
    }

    public CustomException(int code, String message) {
        super(message);
        this.setCode(code);
    }

    public CustomException(ResultCode resultCode){
        super(resultCode.getMsg());
        this.setCode(resultCode.getCode());
    }
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}