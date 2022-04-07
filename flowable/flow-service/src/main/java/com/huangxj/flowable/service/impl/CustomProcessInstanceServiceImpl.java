package com.huangxj.flowable.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.huangxj.common.core.exception.ServiceException;
import com.huangxj.common.core.utils.StringUtils;
import com.huangxj.flowable.entity.CustomProcessInstance;
import com.huangxj.flowable.entity.Form;
import com.huangxj.flowable.entity.ModelInfo;
import com.huangxj.flowable.enums.BpmProcessInstanceDeleteReasonEnum;
import com.huangxj.flowable.enums.BpmProcessInstanceResultEnum;
import com.huangxj.flowable.enums.BpmProcessInstanceStatusEnum;
import com.huangxj.flowable.mapper.CustomProcessInstanceMapper;
import com.huangxj.flowable.mock.MockData;
import com.huangxj.flowable.mock.User;
import com.huangxj.flowable.service.CustomProcessInstanceService;
import com.huangxj.common.core.service.impl.BaseServiceImpl;
import com.huangxj.flowable.service.CustomTaskService;
import com.huangxj.flowable.service.FormService;
import com.huangxj.flowable.service.ModelInfoService;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;

/**
 * 流程实例 服务实现类
 *
 * @author huangxj
 * @date 2022-03-22
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CustomProcessInstanceServiceImpl extends BaseServiceImpl<CustomProcessInstanceMapper, CustomProcessInstance> implements CustomProcessInstanceService {


    @Autowired
    RuntimeService runtimeService;

    @Autowired
    RepositoryService repositoryService;

    @Autowired
    ModelInfoService modelInfoService;

    @Autowired
    FormService formService;

    @Autowired
    HistoryService historyService;

    @Autowired
    CustomTaskService customTaskService;


    @Override
    public CustomProcessInstance createProcessInstance(CustomProcessInstance customProcessInstance) {

        String processDefinitionId = customProcessInstance.getProcessDefinitionId();
        if (StringUtils.isEmpty(processDefinitionId)) {
            throw new ServiceException("流程定义不存在，请检查流程是否发布");
        }
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();

        ModelInfo modelInfo = modelInfoService.getById(customProcessInstance.getModelInfoId());
        if (null == processDefinition || null == modelInfo) {
            throw new ServiceException("流程定义不存在，请检查流程是否发布");
        }
        if ("自定义表单".equals(modelInfo.getFormType())) {
            Form form = formService.getById(modelInfo.getFormId());
            if (null == form) {
                throw new ServiceException("流程未配置表单");
            }
            customProcessInstance.setConf(form.getConf());
            customProcessInstance.setFields(form.getFields());
        }

        Map jsonObject = JSON.parseObject(customProcessInstance.getFormVariables());

        customProcessInstance.setStatus(BpmProcessInstanceStatusEnum.RUNNING.getStatus());
        customProcessInstance.setResult(BpmProcessInstanceResultEnum.PROCESS.getResult());


        User user = MockData.getCurrentUser();

        customProcessInstance.setStartUser("" + user.getUserId());
        customProcessInstance.setStartUserName(user.getUserName());

        customProcessInstance.setName(processDefinition.getName());
        ProcessInstance instance = runtimeService.startProcessInstanceById(processDefinitionId, jsonObject);
        customProcessInstance.setProcessInstanceId(instance.getProcessInstanceId());
        this.save(customProcessInstance);
        return customProcessInstance;
    }

    @Override
    public boolean removeByIds(Collection<? extends Serializable> idList) {
        List<CustomProcessInstance> customProcessInstances = this.listByIds(idList);
        for (CustomProcessInstance customProcessInstance : customProcessInstances) {
            runtimeService.deleteProcessInstance(customProcessInstance.getProcessInstanceId(), null);
        }
        return super.removeByIds(idList);
    }

    @Override
    public CustomProcessInstance getCustomProcessInstanceByProcessInstanceId(String processInstanceId) {
        CustomProcessInstance customProcessInstance = this.lambdaQuery().eq(CustomProcessInstance::getProcessInstanceId, processInstanceId).one();
        return customProcessInstance;
    }

    @Override
    public ProcessInstance getProcessInstance(String id) {
        return runtimeService.createProcessInstanceQuery().processInstanceId(id).singleResult();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateProcessInstanceReject(String processInstanceId, String comment) {
        // 需要主动查询，因为 instance 只有 id 属性
//        ProcessInstance processInstance = getProcessInstance(processInstanceId);

        // 删除流程实例，以实现驳回任务时，取消整个审批流程
        deleteProcessInstance(processInstanceId, StrUtil.format(BpmProcessInstanceDeleteReasonEnum.REJECT_TASK.format(comment)));

        // 更新 status + result
        // 注意，不能和上面的逻辑更换位置。因为 deleteProcessInstance 会触发流程的取消，进而调用 updateProcessInstanceExtCancel 方法，
        // 设置 result 为 BpmProcessInstanceStatusEnum.CANCEL，显然和 result 不一定是一致的

        CustomProcessInstance customProcessInstance = getCustomProcessInstanceByProcessInstanceId(processInstanceId);
        customProcessInstance.setStatus(BpmProcessInstanceStatusEnum.FINISH.getStatus());
        customProcessInstance.setResult(BpmProcessInstanceResultEnum.REJECT.getResult());
        customProcessInstance.setEndTime(new Date());
        this.updateById(customProcessInstance);
    }

    private void deleteProcessInstance(String instanceId, String reason) {
        runtimeService.deleteProcessInstance(instanceId, reason);
    }

    @Override
    public void updateProcessInstanceComplete(ProcessInstance instance) {
        // 需要主动查询，因为 instance 只有 id 属性
        // 另外，此时如果去查询 ProcessInstance 的话，字段是不全的，所以去查询了 HistoricProcessInstance
        HistoricProcessInstance processInstance = getHistoricProcessInstance(instance.getId());
        // 更新拓展表
        CustomProcessInstance customProcessInstance = getCustomProcessInstanceByProcessInstanceId(instance.getProcessInstanceId());
        customProcessInstance.setEndTime(new Date());
        customProcessInstance.setStatus(BpmProcessInstanceStatusEnum.FINISH.getStatus());
        customProcessInstance.setResult(BpmProcessInstanceResultEnum.APPROVE.getResult());

        this.updateById(customProcessInstance);

    }

    public HistoricProcessInstance getHistoricProcessInstance(String id) {
        return historyService.createHistoricProcessInstanceQuery().processInstanceId(id).singleResult();
    }

    @Override
    @Transactional
    public void cancelProcessInstance(String startUser, String processInstanceId) {
        // 校验流程实例存在
        CustomProcessInstance instance = getCustomProcessInstanceByProcessInstanceId(processInstanceId);

        if (instance == null) {
            throw new ServiceException("流程实例不存在");
        }

        // 只能取消自己的
        if (!Objects.equals(instance.getStartUser(), startUser)) {
            throw new ServiceException("无法取消他人的流程");
        }

        instance.setEndTime(new Date());
        instance.setStatus(BpmProcessInstanceStatusEnum.FINISH.getStatus());
        instance.setResult(BpmProcessInstanceResultEnum.CANCEL.getResult());
        this.updateById(instance);
        customTaskService.cancelTasksByProcessInstanceId(instance.getProcessInstanceId());
        // 通过删除流程实例，实现流程实例的取消,
        // 删除流程实例，正则执行任务ACT_RU_TASK. 任务会被删除。通过历史表查询
        deleteProcessInstance(instance.getProcessInstanceId(),
                BpmProcessInstanceDeleteReasonEnum.CANCEL_TASK.format());


    }
}
