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
@ApiModel("模拟志愿推荐信息")
public class MockDTO {
    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "院校开设专业的主键id")
    private String id;

    @ApiModelProperty(value = "院校名称")
    private String name;

    @ApiModelProperty(value = "院校类别  如：综合、农林、理工")
    private String kind;

    @ApiModelProperty(value = "办学性质 0：公办   1：私办")
    private Integer category;

    @ApiModelProperty(value = "专业代码")
    private String code;

    @ApiModelProperty(value = "专业名称")
    private String specialized;

    @ApiModelProperty(value = "本专业面向山东招生计划数量")
    private String plan;

    @ApiModelProperty(value = "学制  如：四  、 八")
    private String academic;

    @ApiModelProperty(value = "学费")
    private String tuition;

    @ApiModelProperty(value = "选科要求  如：物/地/史(3选1)   字符串类型")
    private String subjectRequest;

    @ApiModelProperty(value = "上一年最低分  如今年为2022年   则该字段代表2021年的专业录取最低分")
    private String lastYearLowestScore;

    @ApiModelProperty(value = "上一年最低位次  如今年为2022年   则该字段代表2021年的专业录取最低位次")
    private String lastYearLowestBit;

    @ApiModelProperty(value = "排序字段")
    private Integer sort;

}
