package com.huangxj.flowable.wrapper;

import org.springframework.stereotype.Component;
import com.huangxj.flowable.entity.ModelInfo;
import com.huangxj.flowable.vo.ModelInfoVo;
import com.huangxj.common.core.wrapper.BaseEntityWrapper;
import com.huangxj.common.core.utils.BeanConverter;

/**
 * Bpm 流程定义表
包装类,返回视图层所需的字段
 *
 * @author huangxj
 * @since 2022-03-11
 */
@Component
public class ModelInfoWrapper extends BaseEntityWrapper<ModelInfo, ModelInfoVo>  {

	@Override
	public ModelInfoVo entityVO(ModelInfo modelInfo) {
		ModelInfoVo modelInfoVo =  BeanConverter.convert(modelInfo, ModelInfoVo.class);

		return modelInfoVo;
	}

}
