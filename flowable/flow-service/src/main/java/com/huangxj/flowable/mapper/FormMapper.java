package com.huangxj.flowable.mapper;

import com.huangxj.flowable.entity.Form;
import com.huangxj.common.core.mapper.MyBaseMapper;
import org.apache.ibatis.annotations.Mapper;
/**
 * 自定义表单 Mapper 接口
 * @author huangxj
 * @date 2022-03-16
 */
@Mapper
public interface FormMapper extends MyBaseMapper<Form> {

}
