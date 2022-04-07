package com.huangxj.flowable.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huangxj.common.core.constant.ErrorCode;
import com.huangxj.common.core.model.PageParam;
import com.huangxj.common.core.model.Result;
import com.huangxj.common.core.utils.BeanConverter;
import com.huangxj.flowable.entity.ModelInfo;
import com.huangxj.flowable.param.ModelInfoParam;
import com.huangxj.flowable.service.ModelInfoService;
import com.huangxj.flowable.vo.ModelInfoVo;
import com.huangxj.flowable.wrapper.ModelInfoWrapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * Bpm 流程定义表
 * 前端控制器
 *
 * @author huangxj
 * @date 2022-03-11
 */
@Api(value = "Bpm 流程定义表 ", tags = "Bpm 流程定义表")
@RestController
@RequestMapping("flowable/modelInfo")
@AllArgsConstructor
public class ModelInfoController{

    private ModelInfoService service;

    private ModelInfoWrapper wrapper;

    @GetMapping("detail/{id}")
    @ApiOperation(value = "详情", notes = "传入Id")
    public Result detail(@PathVariable Integer id) {
        ModelInfo detail = service.getById(id);
        return Result.success().data(wrapper.entityVO(detail));
    }

    @GetMapping("/getProcessDefinitionList")
    @ApiOperation(value = "获得流程定义列表")
    public Result getProcessDefinitionList(@RequestParam("modelKey") String modelKey) {
        return Result.success().data(service.getProcessDefinitionListBy(modelKey));
    }

    @GetMapping("/getBpmnXml")
    @ApiOperation(value = "获得流程定义的XML")
    public Result getProcessDefinitionBpmnXML(@RequestParam("definitionId") String definitionId) {
        String bpmnXML = service.getProcessDefinitionBpmnXML(definitionId);
        return Result.success().data(bpmnXML);
    }

    @PostMapping("/deploy")
    @ApiOperation(value = "获得流程定义的 BPMN XML")
    public Result deploy(@RequestParam("id") Integer modelInfoId) {
        ModelInfo modelInfo = service.deployModel(modelInfoId);
        return Result.success().data(modelInfo.getDefinitionId());
    }

    @ApiOperation(value = "列表", notes = "列表")
    @GetMapping("list")
    public Result list(ModelInfoParam param) {
        List<ModelInfo> list = service.list(param);
        return Result.success().data(wrapper.listVO(list));
    }


    @ApiOperation("分页/列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", value = "分页大小", dataType = "int"),
            @ApiImplicitParam(name = "current", value = "页码", dataType = "int"),
            @ApiImplicitParam(name = "listMode", value = "集合模式，不进行分页直接返回所有结果集", dataType = "boolean")
    })
    @GetMapping("page")
    public Result page(PageParam pageParam, boolean listMode, ModelInfoParam param) {
        Page page = pageParam.getPage();
        if (listMode) {
            page.setSize(-1);
        }
        Page<ModelInfo> ret = service.page(page, param);
        return Result.success().data(wrapper.pageVO(ret));
    }

    @ApiOperation("创建")
    @PostMapping("create")
    public Result create(@RequestBody ModelInfoVo vo) {
        ModelInfo obj = BeanConverter.convert(vo, ModelInfo.class, "id");
        service.save(obj);
        return Result.success();
    }

    @ApiOperation("更新")
    @PutMapping("update")
    public Result update(@RequestBody ModelInfoVo vo) {
        if (null == vo.getId()) {
            return Result.fail().code(ErrorCode.PRIMARY_KEY_IS_NULL.getCode());
        }
        ModelInfo obj = service.getById(vo.getId());
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


}