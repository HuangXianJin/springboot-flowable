package com.huangxj.flowable.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 工作流的流程任务的拓展表查询参数
 *
 * @author huangxj
 * @since 2022-03-22
 */
@Data
public class CustomTaskParam {
    private String filter;
    private String name;

    private String assigneeUser;

    private Boolean finished;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date beginCreateTime;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endCreateTime;
}
