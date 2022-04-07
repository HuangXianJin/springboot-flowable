package com.huangxj.flowable.vo;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

/**
 * Bpm 流程定义表
视图实体类
 *
 * @author huangxj
 * @since 2022-03-11
 */
@Data
@ApiModel(value = "ModelInfoVO对象", description = "Bpm 流程定义表 ")
public class ModelInfoVo implements Serializable {
    @ApiModelProperty(value = "id")
    private Integer id;
    @ApiModelProperty(value = "租户编号")
    private String tenantCode;
    @ApiModelProperty(value = "流程模型的编号")
    private String definitionId;
    @ApiModelProperty(value = "流程模型的key")
    private String modelKey;
    @ApiModelProperty(value = "流程模型的名称")
    private String name;
    @ApiModelProperty(value = "描述")
    private String description;
    @ApiModelProperty(value = "表单类型")
    private String formType;
    @ApiModelProperty(value = "表单编号")
    private Integer formId;
    @ApiModelProperty(value = "自定义表单路径")
    private String formPath;
    @ApiModelProperty(value = "xml")
    private String xml;
    @ApiModelProperty(value = "中断状态")
    private Boolean isSuspended;
    @ApiModelProperty(value = "版本", required = true, example = "1")
    private Integer version;
    @ApiModelProperty(value = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date modifyTime;
    @ApiModelProperty(value = "最后发布时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date deployTime;
}
