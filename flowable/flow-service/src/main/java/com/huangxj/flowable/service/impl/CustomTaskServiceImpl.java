package com.huangxj.flowable.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huangxj.common.core.exception.ServiceException;
import com.huangxj.common.core.model.PageParam;
import com.huangxj.common.core.utils.BeanConverter;
import com.huangxj.flowable.entity.CustomProcessInstance;
import com.huangxj.flowable.entity.CustomTask;
import com.huangxj.flowable.enums.BpmProcessInstanceResultEnum;
import com.huangxj.flowable.mapper.CustomTaskMapper;
import com.huangxj.flowable.mock.MockData;
import com.huangxj.flowable.param.CustomTaskParam;
import com.huangxj.flowable.service.CustomProcessInstanceService;
import com.huangxj.flowable.service.CustomTaskService;
import com.huangxj.common.core.service.impl.BaseServiceImpl;
import com.huangxj.flowable.vo.BpmActivityVO;
import com.huangxj.flowable.vo.CustomTaskVo;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 工作流的流程任务的拓展表 服务实现类
 *
 * @author huangxj
 * @date 2022-03-22
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CustomTaskServiceImpl extends BaseServiceImpl<CustomTaskMapper, CustomTask> implements CustomTaskService {


    @Autowired
    private CustomProcessInstanceService customProcessInstanceService;
    @Resource
    private TaskService taskService;
    @Resource
    private HistoryService historyService;

    @Resource
    private RuntimeService runtimeService;

    @Override
    public Page<CustomTask> getTodoTaskPage(PageParam pageParam, CustomTaskParam param) {
        Page page = pageParam.getPage();
        param.setFinished(false);
        param.setAssigneeUser(MockData.getCurrentUser().getUserId());
        this.page(page, param);
        return page;
    }


    @Override
    public Page<CustomTask> getDoneTaskPage(PageParam pageParam, CustomTaskParam param) {
        Page page = pageParam.getPage();
        param.setFinished(true);
        param.setAssigneeUser(MockData.getCurrentUser().getUserId());
        this.page(page, param);
        return page;
    }

    @Override
    public void cancelTasksByProcessInstanceId(String processInstanceId) {
        List<CustomTask> customTaskList = getCustomListPyProcessInstanceId(processInstanceId);
        for (CustomTask task : customTaskList) {
            task.setEndTime(new Date());
            task.setResult(BpmProcessInstanceResultEnum.CANCEL.getResult());
            task.setComment("");
        }
        this.updateBatchById(customTaskList);
    }

    @Override
    public List<CustomTaskVo> getTaskListByProcessInstanceId(String processInstanceId) {
        // 获得任务列表
        List<CustomTaskVo> customTaskVoList = new ArrayList<>();
        List<CustomTask> customTaskList = getCustomListPyProcessInstanceId(processInstanceId);
        CustomProcessInstance processInstance = customProcessInstanceService.getCustomProcessInstanceByProcessInstanceId(processInstanceId);
        if (null == processInstance) {
            return customTaskVoList;
        }
        for (CustomTask task : customTaskList) {
            CustomTaskVo customTaskVo = new CustomTaskVo();
            customTaskVo = BeanConverter.convert(task, customTaskVo);
            customTaskVo.setProcessName(processInstance.getName());
            customTaskVo.setStartUser(processInstance.getStartUser());
            customTaskVo.setStartUserName(processInstance.getStartUserName());
            customTaskVoList.add(customTaskVo);
        }
        return customTaskVoList;
    }

    public List<CustomTask> getCustomListPyProcessInstanceId(String processInstanceId) {
        return this.lambdaQuery().eq(CustomTask::getProcessInstanceId, processInstanceId).orderByDesc(CustomTask::getTaskCreateTime).list();
    }

    @Override
    public List<BpmActivityVO> getActivityListByProcessInstanceId(String processInstanceId) {
        List<HistoricActivityInstance> activityList = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId).list();
        return activityList.stream().map(e -> {
            BpmActivityVO bpmActivityRespVO = new BpmActivityVO();
            bpmActivityRespVO.setAssignee(e.getAssignee());
            bpmActivityRespVO.setKey(e.getActivityId());
            bpmActivityRespVO.setType(e.getActivityType());
            bpmActivityRespVO.setStartTime(e.getStartTime());
            bpmActivityRespVO.setEndTime(e.getEndTime());
            bpmActivityRespVO.setTaskId(e.getTaskId());
            return bpmActivityRespVO;
        }).collect(Collectors.toList());
    }

    @Override
    public void createTask(Task task) {
        CustomTask customTask = new CustomTask();
        customTask.setResult(BpmProcessInstanceResultEnum.PROCESS.getResult());
        customTask.setTaskId(task.getId());
        customTask.setAssigneeUser(task.getAssignee());
        customTask.setTaskId(task.getId());
        customTask.setTaskCreateTime(task.getCreateTime());
        customTask.setProcessInstanceId(task.getProcessInstanceId());
        customTask.setName(task.getName());
        customTask.setProcessDefinitionId(task.getProcessDefinitionId());
        this.save(customTask);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void approveTask(String assignee, CustomTask customTask) {
        // 校验任务存在
        Task task = checkTask(assignee, customTask.getTaskId());
        // 校验流程实例存在
        ProcessInstance instance = runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
        if (instance == null) {
            throw new ServiceException("流程实例不存在");
        }
        // 完成任务，审批通过
        taskService.complete(task.getId(), instance.getProcessVariables());
        customTask.setResult(BpmProcessInstanceResultEnum.APPROVE.getResult());
        customTask.setEndTime(new Date());
        this.updateById(customTask);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void rejectTask(String assignee, CustomTask customTask) {
        // 校验任务存在
        Task task = checkTask(assignee, customTask.getTaskId());
        // 校验流程实例存在
        ProcessInstance instance = customProcessInstanceService.getProcessInstance(task.getProcessInstanceId());
        if (instance == null) {
            throw new ServiceException("流程实例不存在");
        }
        // 更新流程实例为不通过
        customProcessInstanceService.updateProcessInstanceReject(instance.getProcessInstanceId(), customTask.getComment());
        customTask.setResult(BpmProcessInstanceResultEnum.REJECT.getResult());
        customTask.setEndTime(new Date());
        this.updateById(customTask);
    }

    @Override
    public void updateTaskAssignee(String assignee, CustomTask customTask) {
        // 校验任务存在
        Task task = checkTask(assignee, customTask.getTaskId());

        // 更新负责人
        taskService.setAssignee(customTask.getTaskId(), customTask.getAssigneeUser());
        CustomTask old = this.getById(customTask.getId());
        old.setAssigneeUser(customTask.getAssigneeUser());
        old.setAssigneeUserName(customTask.getAssigneeUserName());
        this.updateById(old);
    }

    /**
     * 校验任务是否存在， 并且是否是分配给自己的任务
     *
     * @param assignee 审批人
     * @param taskId   task id
     */
    private Task checkTask(String assignee, String taskId) {
        Task task = getTask(taskId);
        if (task == null) {
            throw new ServiceException("审批任务失败，原因：该任务不处于未审批状态");
        }
        if (!Objects.equals(task.getAssignee(), assignee)) {
            throw new ServiceException("审批任务失败，原因：该任务的审批人不是你");
        }
        return task;
    }

    private Task getTask(String id) {
        return taskService.createTaskQuery().taskId(id).singleResult();
    }


}
