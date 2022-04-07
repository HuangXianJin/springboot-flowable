package com.huangxj.common.core.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


public interface BaseService<T> extends IService<T> {

    /**
     * 查询全部
     * @param param
     * @return
     */
    List<T> list(Object param);

    /**
     * 分页查询
     * @param page
     * @param param
     * @return
     */
    Page<T> page(Page page, Object param);
}
