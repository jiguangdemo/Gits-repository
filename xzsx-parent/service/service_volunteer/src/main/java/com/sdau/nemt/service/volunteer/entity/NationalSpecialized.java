package com.sdau.nemt.service.volunteer.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.sdau.nemt.service.base.model.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author
 * @since 2023-08-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("volunteer_national_specialized")
@ApiModel(value="NationalSpecialized对象", description="")
public class NationalSpecialized extends BaseEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "院校id")
    private String collegesId;

    @ApiModelProperty(value = "专业名字")
    private String name;

    @ApiModelProperty(value = "逻辑删除")
    @TableField("is_deleted")
    @TableLogic
    private Boolean deleted;


}
