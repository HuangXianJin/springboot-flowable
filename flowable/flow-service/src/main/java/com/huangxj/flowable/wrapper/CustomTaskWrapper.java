package com.huangxj.flowable.wrapper;

import com.huangxj.flowable.entity.CustomProcessInstance;
import com.huangxj.flowable.service.CustomProcessInstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.huangxj.flowable.entity.CustomTask;
import com.huangxj.flowable.vo.CustomTaskVo;
import com.huangxj.common.core.wrapper.BaseEntityWrapper;
import com.huangxj.common.core.utils.BeanConverter;

/**
 * 工作流的流程任务的拓展表包装类,返回视图层所需的字段
 *
 * @author huangxj
 * @since 2022-03-22
 */
@Component
public class CustomTaskWrapper extends BaseEntityWrapper<CustomTask, CustomTaskVo>  {

	@Autowired
	private CustomProcessInstanceService customProcessInstanceService;


	@Override
	public CustomTaskVo entityVO(CustomTask customTask) {
		CustomTaskVo customTaskVo =  BeanConverter.convert(customTask, CustomTaskVo.class);

		CustomProcessInstance instance = customProcessInstanceService.getCustomProcessInstanceByProcessInstanceId(customTaskVo.getProcessInstanceId());
		if(null != instance){
			customTaskVo.setProcessName(instance.getName());
			customTaskVo.setStartUser(instance.getStartUser());
			customTaskVo.setStartUserName(instance.getStartUserName());
		}
		return customTaskVo;
	}

}
