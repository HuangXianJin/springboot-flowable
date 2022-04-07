package com.huangxj.flowable.mapper;

import com.huangxj.flowable.entity.CustomProcessInstance;
import com.huangxj.common.core.mapper.MyBaseMapper;
import org.apache.ibatis.annotations.Mapper;
/**
 * 流程实例 Mapper 接口
 * @author huangxj
 * @date 2022-03-22
 */
@Mapper
public interface CustomProcessInstanceMapper extends MyBaseMapper<CustomProcessInstance> {

}
