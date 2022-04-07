package com.huangxj.flowable.wrapper;

import org.springframework.stereotype.Component;
import com.huangxj.flowable.entity.Form;
import com.huangxj.flowable.vo.FormVo;
import com.huangxj.common.core.wrapper.BaseEntityWrapper;
import com.huangxj.common.core.utils.BeanConverter;

/**
 * 自定义表单包装类,返回视图层所需的字段
 *
 * @author huangxj
 * @since 2022-03-16
 */
@Component
public class FormWrapper extends BaseEntityWrapper<Form, FormVo>  {

	@Override
	public FormVo entityVO(Form form) {
		FormVo formVo =  BeanConverter.convert(form, FormVo.class);

		return formVo;
	}

}
