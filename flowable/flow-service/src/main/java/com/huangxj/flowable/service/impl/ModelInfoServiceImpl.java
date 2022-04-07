package com.huangxj.flowable.service.impl;

import com.huangxj.common.core.exception.ServiceException;
import com.huangxj.common.core.utils.StringUtils;
import com.huangxj.flowable.entity.ModelInfo;
import com.huangxj.flowable.mapper.ModelInfoMapper;
import com.huangxj.flowable.service.ModelInfoService;
import com.huangxj.common.core.service.impl.BaseServiceImpl;
import com.huangxj.flowable.vo.ModelInfoVo;
import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.repository.ProcessDefinitionQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Bpm 流程定义表
 * 服务实现类
 *
 * @author huangxj
 * @date 2022-03-11
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ModelInfoServiceImpl extends BaseServiceImpl<ModelInfoMapper, ModelInfo> implements ModelInfoService {

    @Autowired
    RepositoryService repositoryService;

    @Override
    public List<ModelInfoVo> getProcessDefinitionListBy(String modelKey) {
        // 拼接查询条件
        ProcessDefinitionQuery definitionQuery = repositoryService.createProcessDefinitionQuery();
        // 执行查询
        List<ProcessDefinition> processDefinitions = definitionQuery.processDefinitionKey(modelKey).orderByProcessDefinitionVersion().desc().list();
        List<ModelInfoVo> modelInfoVoList = processDefinitions.stream().map(e -> {
            ModelInfoVo modelInfoVo = new ModelInfoVo();
            modelInfoVo.setName(e.getName());
            modelInfoVo.setDefinitionId(e.getId());
            modelInfoVo.setModelKey(e.getKey());
            modelInfoVo.setVersion(e.getVersion());
            modelInfoVo.setIsSuspended(e.isSuspended());
            return modelInfoVo;
        }).collect(Collectors.toList());
        return modelInfoVoList;
    }

    @Override
    public ModelInfo deployModel(Integer modelInfoId) {
        ModelInfo modelInfo = this.getById(modelInfoId);
        if (null == modelInfo) {
            throw new ServiceException("流程模型不存在");
        }
        if (StringUtils.isEmpty(modelInfo.getXml())) {
            throw new ServiceException("流程设计为空");
        }
        Deployment deploy = null;
        try {
            deploy = repositoryService.createDeployment().addBytes(modelInfo.getModelKey() + ".bpmn20.xml", modelInfo.getXml().getBytes("UTF-8"))
                    .deploy();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 设置 ProcessDefinition 的 category 分类
        ProcessDefinition definition = repositoryService.createProcessDefinitionQuery().deploymentId(deploy.getId()).singleResult();
        modelInfo.setDefinitionId(definition.getId());
        updateProcessDefinitionSuspended(definition.getKey(), definition.getId());
        modelInfo.setDeployTime(new Date());

        updateById(modelInfo);
        return modelInfo;
    }

    @Override
    public String getProcessDefinitionBpmnXML(String definitionId) {
        BpmnModel bpmnModel = repositoryService.getBpmnModel(definitionId);
        if (bpmnModel == null) {
            return null;
        }
        BpmnXMLConverter converter = new BpmnXMLConverter();
        return new String(converter.convertToXML(bpmnModel));
    }

    /**
     * 挂起旧流程，不能发起新任务
     *
     * @param modelKey
     * @param id
     */
    private void updateProcessDefinitionSuspended(String modelKey, String id) {
        List<ProcessDefinition> processDefinitionList = repositoryService.createProcessDefinitionQuery().processDefinitionKey(modelKey).list();
        for (ProcessDefinition item : processDefinitionList) {
            if (item.isSuspended()||item.getId().equals(id)) {
                continue;
            } else {
                repositoryService.suspendProcessDefinitionById(item.getId(), false, null);
            }
        }
    }

}
