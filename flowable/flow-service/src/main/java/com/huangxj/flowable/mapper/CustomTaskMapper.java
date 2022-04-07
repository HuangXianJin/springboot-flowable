package com.huangxj.flowable.mapper;

import com.huangxj.flowable.entity.CustomTask;
import com.huangxj.common.core.mapper.MyBaseMapper;
import org.apache.ibatis.annotations.Mapper;
/**
 * 工作流的流程任务的拓展表 Mapper 接口
 * @author huangxj
 * @date 2022-03-22
 */
@Mapper
public interface CustomTaskMapper extends MyBaseMapper<CustomTask> {

}
