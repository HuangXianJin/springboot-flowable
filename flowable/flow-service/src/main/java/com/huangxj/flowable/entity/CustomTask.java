package com.huangxj.flowable.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 工作流的流程任务的拓展表
 *
 * @author huangxj
 * @date 2022-03-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@TableName("flow_custom_task")
@ApiModel(value="CustomTask对象", description="工作流的流程任务的拓展表")
public class CustomTask implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "编号")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "任务的审批人")
    private String assigneeUser;

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
    private Date endTime;

    @ApiModelProperty(value = "流程实例的编号")
    private String processInstanceId;

    @ApiModelProperty(value = "流程定义的编号")
    private String processDefinitionId;

    @ApiModelProperty(value = "租户编号")
    private String tenantCode;

    @ApiModelProperty(value = "创建人")
    @TableField(fill = FieldFill.INSERT)
    private Integer createId;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "修改人")
    @TableField(fill = FieldFill.UPDATE)
    private Integer modifyId;

    @ApiModelProperty(value = "修改时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date modifyTime;

    private Date taskCreateTime;
}
