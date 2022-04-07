package com.huangxj.flowable.vo;

import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

/**
 * 自定义表单视图实体类
 *
 * @author huangxj
 * @since 2022-03-16
 */
@Data
@ApiModel(value = "FormVO对象", description = "自定义表单")
public class FormVo implements Serializable {
    @ApiModelProperty(value = "id")
    private Integer id;
    @ApiModelProperty(value = "租户编号")
    private String tenantCode;
    @ApiModelProperty(value = "表单的配置")
    private String conf;
    @ApiModelProperty(value = "表单项的数组")
    private String fields;
    @ApiModelProperty(value = "描述")
    private String description;

}
