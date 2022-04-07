package com.huangxj.common.core.model;


import com.google.common.collect.Maps;
import com.huangxj.common.core.constant.ErrorCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @author huangxj
 */
@ApiModel(value = "响应结果")
@Data
public class Result<T> implements Serializable {
    private static final long serialVersionUID = -6190689122701100762L;


    /**
     * 是否处理成功
     */
    @ApiModelProperty(value = "是否处理成功")
    private boolean success = true;

    /**
     * 响应编码
     */
    @ApiModelProperty(value = "响应编码:0-请求处理成功")
    private int code = 200;
    /**
     * 提示消息
     */
    @ApiModelProperty(value = "提示消息")
    private String message;

    /**
     * 请求路径
     */
    @ApiModelProperty(value = "请求路径")
    private String path;

    /**
     * 响应数据
     */
    @ApiModelProperty(value = "响应数据")
    private T data;


    /**
     * 附加数据
     */
    @ApiModelProperty(value = "附加数据")
    private Map<String, Object> extra;

    /**
     * 响应时间
     */
    @ApiModelProperty(value = "响应时间")
    private long timestamp = System.currentTimeMillis();

    public Result() {
        super();
    }


    public Result code(int code) {
        this.code = code;
        return this;
    }


    public Result message(String message) {
        this.message = message;
        return this;
    }


    public Result data(T data) {
        this.data = data;
        return this;
    }

    public Result path(String path) {
        this.path = path;
        return this;
    }

    public Result put(String key, Object value) {
        if (this.extra == null) {
            this.extra = Maps.newHashMap();
        }
        this.extra.put(key, value);
        return this;
    }

    public Result success(boolean success) {
        this.success = success;
        return this;
    }


    /**
     * 请求成功
     *
     * @return
     */
    public static Result success() {
        return new Result().code(ErrorCode.OK.getCode()).message("操作成功");
    }


    public static Result fail() {
        return new Result().success(false).code(ErrorCode.FAIL.getCode()).message("操作失败");
    }


    /**
     * 操作失败
     *
     * @return
     */
    public static Result fail(String message) {
        return fail().message(message);
    }

}
