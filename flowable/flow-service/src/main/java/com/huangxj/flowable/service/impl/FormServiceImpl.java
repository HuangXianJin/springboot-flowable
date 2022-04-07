package com.huangxj.flowable.service.impl;

import com.huangxj.flowable.entity.Form;
import com.huangxj.flowable.mapper.FormMapper;
import com.huangxj.flowable.service.FormService;
import com.huangxj.common.core.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 自定义表单 服务实现类
 *
 * @author huangxj
 * @date 2022-03-16
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class FormServiceImpl extends BaseServiceImpl<FormMapper, Form> implements FormService {

}
