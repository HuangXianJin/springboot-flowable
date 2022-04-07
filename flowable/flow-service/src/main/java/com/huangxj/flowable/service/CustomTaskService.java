package com.huangxj.flowable.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huangxj.common.core.model.PageParam;
import com.huangxj.flowable.entity.CustomTask;
import com.huangxj.common.core.service.BaseService;
import com.huangxj.flowable.param.CustomTaskParam;
import com.huangxj.flowable.vo.BpmActivityVO;
import com.huangxj.flowable.vo.CustomTaskVo;
import org.flowable.task.api.Task;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 工作流的流程任务的拓展表 服务类
 *
 * @author huangxj
 * @date 2022-03-22
 */
public interface CustomTaskService extends BaseService<CustomTask> {

    Page<CustomTask> getTodoTaskPage(PageParam pageParam, CustomTaskParam param);

    Page<CustomTask> getDoneTaskPage(PageParam pageParam, CustomTaskParam param);

    void cancelTasksByProcessInstanceId(String processInstanceId);

    List<CustomTaskVo> getTaskListByProcessInstanceId(String processInstanceId);

    List<BpmActivityVO> getActivityListByProcessInstanceId(String processInstanceId);

    void createTask(Task entity);

    @Transactional(rollbackFor = Exception.class)
    void approveTask(String assignee, CustomTask customTask);

    @Transactional(rollbackFor = Exception.class)
    void rejectTask(String assignee, CustomTask customTask);

    void updateTaskAssignee(String assignee, CustomTask customTask);
}
