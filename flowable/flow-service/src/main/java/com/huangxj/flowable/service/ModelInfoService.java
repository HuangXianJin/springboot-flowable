package com.huangxj.flowable.service;

import com.huangxj.flowable.entity.ModelInfo;
import com.huangxj.common.core.service.BaseService;
import com.huangxj.flowable.vo.ModelInfoVo;

import java.util.List;

/**
 * Bpm 流程定义表
 服务类
 *
 * @author huangxj
 * @date 2022-03-11
 */
public interface ModelInfoService extends BaseService<ModelInfo> {

    List<ModelInfoVo> getProcessDefinitionListBy(String modelKey);

    ModelInfo deployModel(Integer modelInfoId);

    String getProcessDefinitionBpmnXML(String definitionId);
}
