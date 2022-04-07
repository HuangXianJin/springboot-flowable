package com.huangxj.flowable.vo;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 工作流的流程任务的拓展表视图实体类
 *
 * @author huangxj
 * @since 2022-03-22
 */
@Data
@ApiModel(value = "CustomTaskVO对象", description = "工作流的流程任务的拓展表")
public class CustomTaskVo implements Serializable {
    @ApiModelProperty(value = "编号")
    private Integer id;
    @ApiModelProperty(value = "任务的审批人")
    private String assigneeUser;
    @ApiModelProperty
    private String assigneeUserName;
    @ApiModelProperty(value = "任务的名字")
    private String name;
    @ApiModelProperty(value = "任务的编号")
    private String taskId;
    @ApiModelProperty(value = "任务的结果")
    private String result;
    @ApiModelProperty(value = "审批建议")
    private String comment;
    @ApiModelProperty(value = "任务的结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;
    @ApiModelProperty(value = "流程实例的编号")
    private String processInstanceId;
    @ApiModelProperty(value = "流程定义的编号")
    private String processDefinitionId;
    @ApiModelProperty(value = "租户编号")
    private String tenantCode;
    @ApiModelProperty
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date taskCreateTime;
    @ApiModelProperty(value = "发起流程的用户编号")
    private String startUser;
    @ApiModelProperty(value = "发起流程的用户名字")
    private String startUserName;
    @ApiModelProperty(value = "流程名字")
    private String processName;

    private Long durationInMillis;

    public Long getDurationInMillis() {
        if (null != taskCreateTime && null != endTime) {
            return endTime.getTime() - taskCreateTime.getTime();
        } else {
            return null;
        }
    }

}
