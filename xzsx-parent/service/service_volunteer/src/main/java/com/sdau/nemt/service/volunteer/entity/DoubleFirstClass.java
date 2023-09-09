package com.sdau.nemt.service.volunteer.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.sdau.nemt.service.base.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 双一流专业
 * </p>
 *
 * @author
 * @since 2023-08-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("volunteer_double_first_class")
@ApiModel(value="DoubleFirstClass对象", description="双一流专业")
public class DoubleFirstClass extends BaseEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "院校主键")
    private String collegesId;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "逻辑删除")
    @TableLogic
    private Integer isDeleted;


}
