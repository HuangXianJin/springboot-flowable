package com.huangxj.flowable.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum BpmProcessInstanceResultEnum {

    PROCESS("处理中", "处理中"),
    APPROVE("通过", "通过"),
    REJECT("未通过", "未通过"),
    CANCEL("已取消", "已取消");

    /**
     * 结果
     */
    private final String result;
    /**
     * 描述
     */
    private final String desc;

}
