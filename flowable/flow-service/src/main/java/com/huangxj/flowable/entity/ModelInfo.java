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
 * Bpm 流程定义表

 *
 * @author huangxj
 * @date 2022-03-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@TableName("flow_model_info")
@ApiModel(value="ModelInfo对象", description="Bpm 流程定义表 ")
public class ModelInfo implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
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

    @ApiModelProperty(value = "创建人")
    @TableField(fill = FieldFill.INSERT)
    private Integer createId;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "更新人")
    @TableField(fill = FieldFill.UPDATE)
    private Integer modifyId;

    @ApiModelProperty(value = "修改时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date modifyTime;

    @ApiModelProperty(value = "最后发布时间")
    private Date deployTime;

}
