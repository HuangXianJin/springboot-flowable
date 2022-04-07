package com.huangxj.common.core.exception;

import com.huangxj.common.core.constant.ErrorCode;

/**
 * 提示消息异常
 *
 * @author admin
 */
public class AlertException extends ServiceException {
    private static final long serialVersionUID = 4908906410210213271L;

    public AlertException() {
    }

    public AlertException(String msg) {
        super(ErrorCode.ALERT.getCode(), msg);
    }


}
