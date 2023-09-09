package com.sdau.nemt.service.volunteer.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author
 * @since 2023-08-11
 * @Description:
 */
@ApiModel(value = "前端专业查询条件")
@Data
public class SpecializedQueryVO {

    @ApiModelProperty(value = "专业代码")
    private String code;

    @ApiModelProperty(value = "隶属门类id")
    private String classifyId;

    @ApiModelProperty(value = "专业名称")
    private String name;
}
