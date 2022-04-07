package com.huangxj.flowable.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum BpmProcessInstanceStatusEnum {

    RUNNING("进行中", "进行中"),
    FINISH("已完成", "已完成");

    /**
     * 状态
     */
    private final String status;
    /**
     * 描述
     */
    private final String desc;

}
