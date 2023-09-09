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
@ApiModel("专业录取概率")
public class SpecializedProbabilityDTO {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键")
    private String Id;

    @ApiModelProperty(value = "专业代码")
    private String code;

    @ApiModelProperty(value = "专业名称")
    private String specialized;

    @ApiModelProperty(value = "专业类型 0：本科专业    1：专科专业")
    private Integer type;

    @ApiModelProperty(value = "选科要求  如：物/地/史(3选1)   字符串类型")
    private String subjectRequest;

    @ApiModelProperty(value = "上一年最低分  如今年为2022年   则该字段代表2021年的专业录取最低分")
    private String lastYearLowestScore;

    @ApiModelProperty(value = "上一年最低位次  如今年为2022年   则该字段代表2021年的专业录取最低位次")
    private String lastYearLowestBit;

    @ApiModelProperty(value = "上一年平均分  如今年为2022年   则该字段代表2021年的专业录取平均分")
    private String lastYearAverageScore;

    @ApiModelProperty(value = "上一年平均位次  如今年为2022年   则该字段代表2021年的专业录取平均位次")
    private String lastYearAverageBit;

    @ApiModelProperty(value = "前年最低分  如今年为2022年   则该字段代表2020年的专业录取最低分")
    private String twoYearsLowestScore;

    @ApiModelProperty(value = "前年最低位次  如今年为2022年   则该字段代表2020年的专业录取最低位次")
    private String twoYearsLowestBit;

    @ApiModelProperty(value = "前年平均分  如今年为2022年   则该字段代表2020年的专业录取平均分")
    private String twoYearsAverageScore;

    @ApiModelProperty(value = "前年平均位次  如今年为2022年   则该字段代表2020年的专业录取平均位次")
    private String twoYearsAverageBit;

    @ApiModelProperty(value = "录取概率")
    private String probability;

    @ApiModelProperty(value = "志愿位置  0代表无   数字代表位置")
    private Integer sort;

    @ApiModelProperty(value = "是否收藏")
    private Integer collection;

}
