package com.sdau.nemt.service.volunteer.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author
 * @since 2023-08-11
 * @Description:
 */
@Data
@ApiModel("模拟志愿搜索条件")
public class VolunteerQueryVO {

    @ApiModelProperty(value = "院校名称")
    private String collegesName;

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

    @ApiModelProperty(value = "省，比如 北京、山东")
    private String province;

    @ApiModelProperty(value = "市，如  北京市，泰安市")
    private String city;

//    @ApiModelProperty(value = "隶属门类id")
//    private String classifyId;

    @ApiModelProperty(value = "专业名称")
    private String specializedName;

    @ApiModelProperty(value = "冲、稳、保   0:冲，1：稳，2保")
    private Integer strategy;

}
