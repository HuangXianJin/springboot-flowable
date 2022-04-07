package com.huangxj.common.core.exception;


import com.huangxj.common.core.constant.ErrorCode;

/**
 * 基础错误异常
 *
 * @author admin
 */
public class ServiceException extends RuntimeException {

    private static final long serialVersionUID = 3655050728585279326L;

    private int code = ErrorCode.ERROR.getCode();

    public ServiceException() {

    }

    public ServiceException(String msg) {
        super(msg);
    }

    public ServiceException(int code, String msg) {
        super(msg);
        this.code = code;
    }

    public ServiceException(int code, String msg, Throwable cause) {
        super(msg, cause);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }


}
