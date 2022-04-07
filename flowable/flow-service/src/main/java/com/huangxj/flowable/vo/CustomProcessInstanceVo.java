package com.huangxj.flowable.vo;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

/**
 * 流程实例视图实体类
 *
 * @author huangxj
 * @since 2022-03-22
 */
@Data
@ApiModel(value = "CustomProcessInstanceVO对象", description = "流程实例")
public class CustomProcessInstanceVo implements Serializable {
    @ApiModelProperty(value = "id")
    private Integer id;
    @ApiModelProperty(value = "租户编号")
    private String tenantCode;
    @ApiModelProperty(value = "发起流程的用户编号")
    private Integer startUser;
    @ApiModelProperty(value = "发起流程的用户名字")
    private String startUserName;
    @ApiModelProperty(value = "流程实例的名字")
    private String name;
    @ApiModelProperty(value = "流程模型id")
    private Integer modelInfoId;
    @ApiModelProperty(value = "流程实例的编号")
    private String processInstanceId;
    @ApiModelProperty(value = "流程定义的编号")
    private String processDefinitionId;
    @ApiModelProperty(value = "流程实例的状态")
    private String status;
    @ApiModelProperty(value = "流程实例的结果")
    private String result;
    @ApiModelProperty(value = "结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date endTime;
    @ApiModelProperty(value = "表单值")
    private String formVariables;
    @ApiModelProperty(value = "结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;
    @ApiModelProperty(value = "表单的配置")
    private String conf;
    @ApiModelProperty(value = "表单项的数组")
    private String fields;

    public Long getDurationInMillis() {
        if (null != createTime && null != endTime) {
            return endTime.getTime() - createTime.getTime();
        } else {
            return null;
        }
    }
}
