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
 * 流程实例
 *
 * @author huangxj
 * @date 2022-03-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@TableName("flow_custom_process_instance")
@ApiModel(value="CustomProcessInstance对象", description="流程实例")
public class CustomProcessInstance implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "租户编号")
    private String tenantCode;

    @ApiModelProperty(value = "发起流程的用户")
    private String startUser;

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
    private Date endTime;

    @ApiModelProperty(value = "表单值")
    private String formVariables;

    @ApiModelProperty(value = "表单的配置")
    private String conf;

    @ApiModelProperty(value = "表单项的数组")
    private String fields;

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


}
