package com.huangxj.common.core.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huangxj.common.core.mapper.MyBaseMapper;
import com.huangxj.common.core.service.BaseService;

import java.util.List;


/**
 * @ClassName BaseServiceImpl
 * @Author: huangxj
 * @Create: 2018/11/27
 **/
public abstract class BaseServiceImpl<M extends MyBaseMapper<T>, T> extends ServiceImpl<M, T> implements BaseService<T> {

    @Override
    public List<T> list(Object param) {
        return baseMapper.select(new Page(1, -1), param).getRecords();
    }

    @Override
    public Page<T> page(Page page, Object param) {
        return baseMapper.select(page, param);
    }

}
