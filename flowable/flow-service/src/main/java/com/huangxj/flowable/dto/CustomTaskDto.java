package com.huangxj.flowable.dto;

import com.huangxj.flowable.entity.CustomTask;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;

/**
 * 工作流的流程任务的拓展表数据传输对象实体类
 *
 * @author huangxj
 * @since 2022-03-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CustomTaskDto extends CustomTask implements Serializable {

}
