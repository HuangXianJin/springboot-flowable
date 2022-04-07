package com.huangxj.flowable.dto;

import com.huangxj.flowable.entity.Form;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;

/**
 * 自定义表单数据传输对象实体类
 *
 * @author huangxj
 * @since 2022-03-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FormDto extends Form implements Serializable {

}
