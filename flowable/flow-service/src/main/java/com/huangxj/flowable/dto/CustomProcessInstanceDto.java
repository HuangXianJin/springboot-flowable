package com.huangxj.flowable.dto;

import com.huangxj.flowable.entity.CustomProcessInstance;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;

/**
 * 流程实例数据传输对象实体类
 *
 * @author huangxj
 * @since 2022-03-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CustomProcessInstanceDto extends CustomProcessInstance implements Serializable {

}
