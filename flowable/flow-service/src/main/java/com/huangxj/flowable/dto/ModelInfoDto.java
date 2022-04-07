package com.huangxj.flowable.dto;

import com.huangxj.flowable.entity.ModelInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;

/**
 * Bpm 流程定义表
数据传输对象实体类
 *
 * @author huangxj
 * @since 2022-03-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ModelInfoDto extends ModelInfo implements Serializable {

}
