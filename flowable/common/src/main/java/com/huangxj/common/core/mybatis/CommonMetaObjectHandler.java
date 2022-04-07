package com.huangxj.common.core.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @ClassName CommonMetaObjectHandler
 * @Description 公共字段填充
 * @Author: huangxj
 * @Create: 2019-09-26 9:35
 * @Version V1.0
 **/
@Component
public class CommonMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
//        this.setFieldValByName("createId", AuthHelper.getUserId(), metaObject);
        this.setFieldValByName("createTime", new Date(), metaObject);
//        this.setFieldValByName("modifyId", AuthHelper.getUserId(), metaObject);
        this.setFieldValByName("modifyTime", new Date(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
//        this.setFieldValByName("modifyId", AuthHelper.getUserId(), metaObject);
        this.setFieldValByName("modifyTime", new Date(), metaObject);
    }
}
