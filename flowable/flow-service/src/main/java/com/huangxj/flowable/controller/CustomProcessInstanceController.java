package com.huangxj.flowable.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huangxj.common.core.model.PageParam;
import com.huangxj.common.core.model.Result;
import com.huangxj.common.core.utils.BeanConverter;
import com.huangxj.flowable.entity.CustomProcessInstance;
import com.huangxj.flowable.mock.MockData;
import com.huangxj.flowable.param.CustomProcessInstanceParam;
import com.huangxj.flowable.service.CustomProcessInstanceService;
import com.huangxj.flowable.vo.CustomProcessInstanceVo;
import com.huangxj.flowable.wrapper.CustomProcessInstanceWrapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 流程实例 前端控制器
 *
 * @author huangxj
 * @date 2022-03-22
 */
@Api(value = "流程实例", tags = "流程实例")
@RestController
@RequestMapping("flowable/customProcessInstance")
@AllArgsConstructor
public class CustomProcessInstanceController  {

    private CustomProcessInstanceService service;

    private CustomProcessInstanceWrapper wrapper;


    @GetMapping("detail/{id}")
    @ApiOperation(value = "详情", notes = "传入Id")
    public Result detail(@PathVariable Integer id) {
        CustomProcessInstance detail = service.getById(id);
        return Result.success().data(wrapper.entityVO(detail));
    }


    @ApiOperation(value = "列表", notes = "列表")
    @GetMapping("list")
    public Result list(CustomProcessInstanceParam param) {
        List<CustomProcessInstance> list = service.list(param);
        return Result.success().data(wrapper.listVO(list));
    }

    @ApiOperation("分页/列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", value = "分页大小", dataType = "int"),
            @ApiImplicitParam(name = "current", value = "页码", dataType = "int"),
            @ApiImplicitParam(name = "listMode", value = "集合模式，不进行分页直接返回所有结果集", dataType = "boolean")
    })
    @GetMapping("page")
    public Result page(PageParam pageParam, boolean listMode, CustomProcessInstanceParam param) {
        Page page = pageParam.getPage();
        if (listMode) {
            page.setSize(-1);
        }
        Page<CustomProcessInstance> ret = service.page(page, param);
        return Result.success().data(wrapper.pageVO(ret));
    }

    @ApiOperation("创建")
    @PostMapping("create")
    public Result create(@RequestBody CustomProcessInstanceVo vo) {
        CustomProcessInstance obj = BeanConverter.convert(vo, CustomProcessInstance.class, "id");
        service.createProcessInstance(obj);
        return Result.success();
    }

    @GetMapping("getByProcessInstanceId")
    @ApiOperation(value = "详情", notes = "processInstanceId")
    public Result getByProcessInstanceId(String processInstanceId) {
        CustomProcessInstance detail = service.getCustomProcessInstanceByProcessInstanceId(processInstanceId);
        return Result.success().data(wrapper.entityVO(detail));
    }

    @ApiOperation("取消流程")
    @PostMapping("cancel")
    public Result cancel(@RequestBody CustomProcessInstanceVo vo) {
        CustomProcessInstance obj = BeanConverter.convert(vo, CustomProcessInstance.class, "id");
        service.cancelProcessInstance(MockData.getCurrentUser().getUserId(), obj.getProcessInstanceId());
        return Result.success();
    }

    @ApiOperation("删除,多个id用','分割")
    @DeleteMapping("delete/{id}")
    public Result delete(@PathVariable String id) {
        String[] ids = id.split(",");

        service.removeByIds(Arrays.asList(ids));
        return Result.success();
    }
}