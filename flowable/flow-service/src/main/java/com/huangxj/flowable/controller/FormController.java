package com.huangxj.flowable.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.huangxj.common.core.constant.ErrorCode;
import com.huangxj.common.core.model.PageParam;
import com.huangxj.common.core.model.Result;
import com.huangxj.common.core.utils.BeanConverter;
import com.huangxj.common.core.utils.WebUtils;
import com.huangxj.flowable.entity.Form;
import com.huangxj.flowable.param.FormParam;
import com.huangxj.flowable.service.FormService;
import com.huangxj.flowable.vo.FormVo;
import com.huangxj.flowable.wrapper.FormWrapper;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.apache.commons.lang3.StringUtils;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

/**
 * 自定义表单 前端控制器
 *
 * @author huangxj
 * @date 2022-03-16
 */
@Api(value = "自定义表单", tags = "自定义表单")
@RestController
@RequestMapping("flowable/form")
@AllArgsConstructor
public class FormController {

    private FormService service;

    private FormWrapper wrapper;

    @GetMapping("detail/{id}")
    @ApiOperation(value = "详情", notes = "传入Id")
    public Result detail(@PathVariable Integer id) {
        Form detail = service.getById(id);
        return Result.success().data(wrapper.entityVO(detail));
    }

    @ApiOperation(value = "列表", notes = "列表")
    @GetMapping("list")
    public Result list(FormParam param) {
        List< Form> list =  service.list(param);
        return Result.success().data(wrapper.listVO(list));
    }

    @ApiOperation("分页/列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", value = "分页大小", dataType = "int"),
            @ApiImplicitParam(name = "current", value = "页码", dataType = "int"),
            @ApiImplicitParam(name = "listMode", value = "集合模式，不进行分页直接返回所有结果集", dataType = "boolean")
    })
    @GetMapping("page")
    public Result page(PageParam pageParam, boolean listMode, FormParam param) {
        Page page = pageParam.getPage();
        if (listMode) {
            page.setSize(-1);
        }
        Page<Form> ret = service.page(page, param);
        return Result.success().data(wrapper.pageVO(ret));
    }

    @ApiOperation("创建")
    @PostMapping("create")
    public Result create(@RequestBody FormVo vo) {
        Form obj = BeanConverter.convert(vo, Form.class, "id");
        service.save(obj);
        return Result.success();
    }

    @ApiOperation("更新")
    @PutMapping("update")
    public Result update(@RequestBody FormVo vo) {
        if (null == vo.getId()) {
            return Result.fail().code(ErrorCode.PRIMARY_KEY_IS_NULL.getCode());
        }
        Form obj = service.getById(vo.getId());
        if (null == obj) {
            return Result.fail().code(ErrorCode.DATA_NOT_EXIST.getCode());
        }
        obj = BeanConverter.convert(vo, obj);
        service.updateById(obj);
        return Result.success();
    }

    @ApiOperation("删除,多个id用','分割")
    @DeleteMapping("delete/{id}")
    public Result delete(@PathVariable String id)  {
        String[] ids = id.split(",");
        service.removeByIds(Arrays.asList(ids));
        return Result.success();
    }


}