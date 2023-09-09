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
 * 院校专业往年情况
 * </p>
 *
 * @author
 * @since 2023-08-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("volunteer_colleges_specialized_usual")
@ApiModel(value="CollegesSpecializedUsual对象", description="院校专业往年情况")
public class CollegesSpecializedUsual extends BaseEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "对应开设专业主键")
    private String specializedId;

    @ApiModelProperty(value = "上一年最低分  如今年为2023年   则该字段代表2022年的专业录取最低分")
    private String lastYearLowestScore;

    @ApiModelProperty(value = "上一年最低位次  如今年为2023年   则该字段代表2022年的专业录取最低位次")
    private String lastYearLowestBit;

    @ApiModelProperty(value = "上一年平均分  如今年为2023年   则该字段代表2022年的专业录取平均分")
    private String lastYearAverageScore;

    @ApiModelProperty(value = "上一年平均位次  如今年为2023年   则该字段代表2022年的专业录取平均位次")
    private String lastYearAverageBit;

    @ApiModelProperty(value = "前年最低分  如今年为2023年   则该字段代表2021年的专业录取最低分")
    private String twoYearsLowestScore;

    @ApiModelProperty(value = "前年最低位次  如今年为2023年   则该字段代表2021年的专业录取最低位次")
    private String twoYearsLowestBit;

    @ApiModelProperty(value = "前年平均分  如今年为2023年   则该字段代表2021年的专业录取平均分")
    private String twoYearsAverageScore;

    @ApiModelProperty(value = "前年平均位次  如今年为2023年   则该字段代表2021年的专业录取平均位次")
    private String twoYearsAverageBit;

    @ApiModelProperty(value = "逻辑删除 1（true）已删除， 0（false）未删除")
    @TableLogic
    private Integer isDeleted;


}
