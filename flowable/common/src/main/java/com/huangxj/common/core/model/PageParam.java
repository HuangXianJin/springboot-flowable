package com.huangxj.common.core.model;


import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * @Description: 分页数据
 * @Author: huangxj
 * @Create: 2018/11/2
 * @Version: 1.0
 **/
public class PageParam {

    private long size;
    private long current;
    private String[] ascs;
    private String[] descs;

    public PageParam() {
        this.current = 1;
        this.size = 10;
    }

    public PageParam(Integer pageSize) {
        this.current = 1;
        this.size = pageSize;
    }


    public Page getPage() {
        Page page = new Page(this.current, this.size);
        if (ascs != null && ascs.length > 0) {
            page.addOrder(OrderItem.ascs(ascs));
        }
        if (descs != null && descs.length > 0) {
            page.addOrder(OrderItem.descs(descs));
        }
        return page;
    }


    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getCurrent() {
        return current;
    }

    public void setCurrent(long current) {
        this.current = current;
    }

    public String[] getAscs() {
        return ascs;
    }

    public void setAscs(String[] ascs) {
        this.ascs = ascs;
    }

    public String[] getDescs() {
        return descs;
    }

    public void setDescs(String[] descs) {
        this.descs = descs;
    }

}
