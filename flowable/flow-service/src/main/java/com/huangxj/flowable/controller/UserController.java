package com.huangxj.flowable.controller;

import com.huangxj.common.core.model.Result;
import com.huangxj.flowable.mock.MockData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * TODO
 *
 * @author huangxj
 * @version 1.0
 * @date 2022/4/7 15:08
 */
@Api(value = "用户接口 ")
@RestController
@RequestMapping("user")
public class UserController {

    @GetMapping("list")
    @ApiOperation(value = "用户列表")
    public Result userList() {
        return Result.success().data(MockData.getUserList());
    }

}
