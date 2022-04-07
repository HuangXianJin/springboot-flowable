package com.huangxj.flowable.service;

import com.huangxj.flowable.entity.CustomProcessInstance;
import com.huangxj.common.core.service.BaseService;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.transaction.annotation.Transactional;

/**
 * 流程实例 服务类
 *
 * @author huangxj
 * @date 2022-03-22
 */
public interface CustomProcessInstanceService extends BaseService<CustomProcessInstance> {

    /**
     * 创建流程
     * @param customProcessInstance
     * @return
     */
    CustomProcessInstance createProcessInstance(CustomProcessInstance customProcessInstance);

    CustomProcessInstance getCustomProcessInstanceByProcessInstanceId(String processInstanceId);

    ProcessInstance getProcessInstance(String id);

    @Transactional(rollbackFor = Exception.class)
    void updateProcessInstanceReject(String processInstanceId, String comment);

    void updateProcessInstanceComplete(ProcessInstance instance);

    void cancelProcessInstance(String startUser, String id);
}
