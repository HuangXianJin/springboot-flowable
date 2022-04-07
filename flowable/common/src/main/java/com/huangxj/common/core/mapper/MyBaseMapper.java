package com.huangxj.common.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * @ClassName: BaseMapper
 * @Description: TODO
 * @Author: huangxj
 * @Create: 2018/11/16
 * @Version: 1.0
 **/
public interface MyBaseMapper<T> extends BaseMapper<T> {

    /**
     * 查询接口
     * @param page
     * @param param
     * @return
     */
    Page select(Page page, @Param("queryParam") Object param);
}
