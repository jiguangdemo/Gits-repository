package com.sdau.nemt.service.user.entity.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Date: 2023-08-11
 * @Author:
 * @Description:
 */
@Data
public class GradesForm {
    @ApiModelProperty(value = "省份   如山东省、北京市、新疆维吾尔自治区")
    private String province;

    @ApiModelProperty(value = "市    如泰安市、青岛市")
    private String city;

    @ApiModelProperty(value = "县/区    如泰山区   曹县  ")
    private String county;

    @ApiModelProperty(value = "高中学校名字")
    private String seniorName;

    @ApiModelProperty(value = "选科一  1：物理   2：化学3：生物4：政治5：历史6：地理")
    private Integer subjectsOne;

    @ApiModelProperty(value = "选科一  1：物理  2：化学3：生物4：政治5：历史6：地理")
    private Integer subjectsTwo;

    @ApiModelProperty(value = "选科一  1：物理2：化学3：生物4：政治5：历史6：地理")
    private Integer subjectsThree;

    @ApiModelProperty(value = "高考成绩")
    private String scores;

    @ApiModelProperty(value = "高考位次")
    private String bits;
}
