package com.huangxj.common.core.exception;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.huangxj.common.core.constant.ErrorCode;
import com.huangxj.common.core.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * 统一异常处理器
 *
 * @author LYD
 * @date 2017/7/3
 */
@ControllerAdvice
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {


    /**
     * 错误信息配置
     */
    @JSONField(serialize = false, deserialize = false)
    @JsonIgnore
    private static ResourceBundle resourceBundle = ResourceBundle.getBundle("error");

    /**
     * 提示信息国际化
     *
     * @param messageCode
     * @param defaultMessage
     * @return
     */
    @JSONField(serialize = false, deserialize = false)
    @JsonIgnore
    private static String i18n(String messageCode, String defaultMessage) {
        return resourceBundle.containsKey(messageCode) ? resourceBundle.getString(messageCode) : defaultMessage;
    }


    /**
     * 自定义异常
     *
     * @param ex
     * @param request
     * @param response
     * @return
     */
    @ExceptionHandler({ServiceException.class})
    public static Result openException(Exception ex, HttpServletRequest request, HttpServletResponse response) {
        Result result = resolveException(ex, request.getRequestURI());
        return result;
    }


    /**
     * 其他异常
     *
     * @param ex
     * @param request
     * @param response
     * @return
     */
    @ExceptionHandler({Exception.class})
    public static Result exception(Exception ex, HttpServletRequest request, HttpServletResponse response) {
        Result result = resolveException(ex, request.getRequestURI());
        return result;
    }

    /**
     * Oauth认证异常
     *
     * @param ex
     * @return
     */
    public static Result resolveOauthException(Exception ex, String path) {
        ErrorCode code = ErrorCode.BAD_CREDENTIALS;
        String error = Optional.ofNullable(ex.getMessage()).orElse("");
        if (error.contains("User is disabled")) {
            code = ErrorCode.ACCOUNT_DISABLED;
        }
        if (error.contains("Invalid access token")) {
            code = ErrorCode.INVALID_TOKEN;
        }
        return buildBody(ex, ex.getLocalizedMessage(), code, path);
    }

    /**
     * 静态解析异常。可以直接调用
     *
     * @param ex
     * @return
     */
    public static Result resolveException(Exception ex, String path) {
        ErrorCode code = null;

        String className = ex.getClass().getSimpleName();

        if (className.contains("LoginTypeNotSupportException")) {
            code = ErrorCode.LOGIN_TYPE_NOT_SUPPORT;
        }

        String message = ex.getLocalizedMessage();
        Throwable cause = ex.getCause();

        while (cause != null) {
            message = cause.getLocalizedMessage();
            cause = cause.getCause();
        }

        return buildBody(ex, message, code, path);
    }


    /**
     * 构建返回结果对象
     *
     * @param exception
     * @return
     */
    private static Result buildBody(Exception exception, String message, ErrorCode resultCode, String path) {

        if (resultCode != null) {
            message = i18n(resultCode.getMessage(), message);
        }

        if (resultCode == null) {
            resultCode = ErrorCode.FAIL;
        }

        int code = resultCode.getCode();
        if (exception instanceof ServiceException) {
            ServiceException se = (ServiceException) exception;
            message = se.getLocalizedMessage();
            code = se.getCode();
        }
        Result result = Result.fail().code(code).message(message).path(path);
        log.error("==> error:{} exception: {}", result, exception);
        return result;
    }


}
