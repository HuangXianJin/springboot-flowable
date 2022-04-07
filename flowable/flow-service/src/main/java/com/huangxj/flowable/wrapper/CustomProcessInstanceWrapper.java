package com.huangxj.flowable.wrapper;

import org.springframework.stereotype.Component;
import com.huangxj.flowable.entity.CustomProcessInstance;
import com.huangxj.flowable.vo.CustomProcessInstanceVo;
import com.huangxj.common.core.wrapper.BaseEntityWrapper;
import com.huangxj.common.core.utils.BeanConverter;

/**
 * 流程实例包装类,返回视图层所需的字段
 *
 * @author huangxj
 * @since 2022-03-22
 */
@Component
public class CustomProcessInstanceWrapper extends BaseEntityWrapper<CustomProcessInstance, CustomProcessInstanceVo>  {

	@Override
	public CustomProcessInstanceVo entityVO(CustomProcessInstance customProcessInstance) {
		CustomProcessInstanceVo customProcessInstanceVo =  BeanConverter.convert(customProcessInstance, CustomProcessInstanceVo.class);

		return customProcessInstanceVo;
	}

}
