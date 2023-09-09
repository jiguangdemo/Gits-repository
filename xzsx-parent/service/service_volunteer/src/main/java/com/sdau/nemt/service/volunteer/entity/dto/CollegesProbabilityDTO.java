package com.sdau.nemt.service.volunteer.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author
 * @since 2023-08-11
 * @Description:
 */
@Data
@ApiModel(value = "院校概率等信息")
public class CollegesProbabilityDTO {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "院校主键")
    private String Id;

    @ApiModelProperty(value = "院校报考代码")
    private String code;

    @ApiModelProperty(value = "院校名称")
    private String name;

    @ApiModelProperty(value = "校徽")
    private String badge;

    @ApiModelProperty(value = "学校类型 0：普通本科     1：专科")
    private Integer type;

    @ApiModelProperty(value = "院校类别  如：综合、农林、理工")
    private String kind;

    @ApiModelProperty(value = "办学性质 0：公办   1：私办")
    private Integer category;

    @ApiModelProperty(value = "院校录取概率")
    private String probability;


}
