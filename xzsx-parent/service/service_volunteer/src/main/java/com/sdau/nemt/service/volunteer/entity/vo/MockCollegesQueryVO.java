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
@ApiModel("模拟填报中的院校条件")
public class MockCollegesQueryVO {

    @ApiModelProperty(value = "院校名称")
    private String name;

    @ApiModelProperty(value = "省，比如 北京、山东")
    private String province;

    @ApiModelProperty(value = "市，如  北京市，泰安市")
    private String city;

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


    @ApiModelProperty(value = "冲、稳、保、  例如  0：全部志愿，1：冲 ，2：稳 ， 3：保")
    private Integer pick;

}
