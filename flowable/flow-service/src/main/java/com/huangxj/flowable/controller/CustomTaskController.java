package com.huangxj.flowable.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huangxj.common.core.constant.ErrorCode;
import com.huangxj.common.core.model.PageParam;
import com.huangxj.common.core.model.Result;
import com.huangxj.common.core.utils.BeanConverter;
import com.huangxj.flowable.entity.CustomTask;
import com.huangxj.flowable.mock.MockData;
import com.huangxj.flowable.param.CustomTaskParam;
import com.huangxj.flowable.service.CustomTaskService;
import com.huangxj.flowable.vo.CustomTaskVo;
import com.huangxj.flowable.wrapper.CustomTaskWrapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 工作流的流程任务的拓展表 前端控制器
 *
 * @author huangxj
 * @date 2022-03-22
 */
@Api(value = "工作流的流程任务的拓展表", tags = "工作流的流程任务的拓展表")
@RestController
@RequestMapping("flowable/customTask")
@AllArgsConstructor
public class CustomTaskController  {

    private CustomTaskService service;

    private CustomTaskWrapper wrapper;

    @GetMapping("detail/{id}")
    @ApiOperation(value = "详情", notes = "传入Id")
    public Result detail(@PathVariable Integer id) {
        CustomTask detail = service.getById(id);
        return Result.success().data(wrapper.entityVO(detail));
    }

    @GetMapping("getTaskListByProcessInstanceId")
    @ApiOperation(value = "详情", notes = "processInstanceId")
    public Result getTaskListByProcessInstanceId(String processInstanceId) {
        List list = service.getTaskListByProcessInstanceId(processInstanceId);
        return Result.success().data(list);
    }

    @GetMapping("getActivityListByProcessInstanceId")
    @ApiOperation(value = "activityList", notes = "processInstanceId")
    public Result getActivityListByProcessInstanceId(String processInstanceId) {
        List list = service.getActivityListByProcessInstanceId(processInstanceId);
        return Result.success().data(list);
    }

    @ApiOperation("待办任务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", value = "分页大小", dataType = "int"),
            @ApiImplicitParam(name = "current", value = "页码", dataType = "int"),
    })
    @GetMapping("getTodoTaskPage")
    public Result getTodoTaskPage(PageParam pageParam, CustomTaskParam param) {
        Page<CustomTask> ret = service.getTodoTaskPage(pageParam, param);
        return Result.success().data(wrapper.pageVO(ret));
    }

    @ApiOperation("已办任务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", value = "分页大小", dataType = "int"),
            @ApiImplicitParam(name = "current", value = "页码", dataType = "int"),
    })
    @GetMapping("getDoneTaskPage")
    public Result getDoneTaskPage(PageParam pageParam, CustomTaskParam param) {
        Page<CustomTask> ret = service.getDoneTaskPage(pageParam, param);
        return Result.success().data(wrapper.pageVO(ret));
    }

    @ApiOperation("创建")
    @PostMapping("create")
    public Result create(@RequestBody CustomTaskVo vo) {
        CustomTask obj = BeanConverter.convert(vo, CustomTask.class, "id");
        service.save(obj);
        return Result.success();
    }

    @ApiOperation("更新")
    @PutMapping("update")
    public Result update(@RequestBody CustomTaskVo vo) {
        if (null == vo.getId()) {
            return Result.fail().code(ErrorCode.PRIMARY_KEY_IS_NULL.getCode());
        }
        CustomTask obj = service.getById(vo.getId());
        if (null == obj) {
            return Result.fail().code(ErrorCode.DATA_NOT_EXIST.getCode());
        }
        obj = BeanConverter.convert(vo, obj);
        service.updateById(obj);
        return Result.success();
    }

    @ApiOperation("删除,多个id用','分割")
    @DeleteMapping("delete/{id}")
    public Result delete(@PathVariable String id) {
        String[] ids = id.split(",");
        service.removeByIds(Arrays.asList(ids));
        return Result.success();
    }

    @PutMapping("/approve")
    @ApiOperation("通过任务")
    public Result approveTask(@RequestBody CustomTaskVo reqVO) {
        service.approveTask(MockData.getCurrentUser().getUserId(), BeanConverter.convert(reqVO, CustomTask.class));
        return Result.success();
    }

    @PutMapping("/reject")
    @ApiOperation("不通过任务")
    public Result rejectTask(@RequestBody CustomTaskVo reqVO) {
        service.rejectTask(MockData.getCurrentUser().getUserId(),  BeanConverter.convert(reqVO, CustomTask.class));
        return Result.success();
    }

    @PutMapping("/updateAssignee")
    @ApiOperation(value = "更新任务的负责人", notes = "用于【流程详情】的【转派】按钮")
    public Result updateTaskAssignee(@RequestBody CustomTaskVo reqVO) {
        service.updateTaskAssignee(MockData.getCurrentUser().getUserId(),  BeanConverter.convert(reqVO, CustomTask.class));
        return Result.success();
    }


}