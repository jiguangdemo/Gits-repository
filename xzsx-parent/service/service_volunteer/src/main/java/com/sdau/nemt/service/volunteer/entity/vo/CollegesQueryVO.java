package com.sdau.nemt.service.volunteer.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author
 * @since 2023-08-11
 * @Description:
 */
@ApiModel(value = "前端院校查询条件")
@Data
public class CollegesQueryVO {

    @ApiModelProperty(value = "院校名称")
    private String name;

    @ApiModelProperty(value = "学校类型 0：普通本科     1：专科")
    private Integer type;

    @ApiModelProperty(value = "院校类别  如：综合、农林、理工")
    private String kind;

    @ApiModelProperty(value = "办学性质 0：公办   1：私办")
    private Integer category;

    @ApiModelProperty(value = "985:    0:yes   1:no")
    private Integer worldClass;

    @ApiModelProperty(value = "211:    0:yes    1:no")
    private Integer innovation;

    @ApiModelProperty(value = "双一流： 0：yes  1：no")
    private Integer doubleFirstClass;

    @ApiModelProperty(value = "强基计划     0：yes    1：no")
    private Integer strongFoundation;

    @ApiModelProperty(value = "省")
    private String province;
}
