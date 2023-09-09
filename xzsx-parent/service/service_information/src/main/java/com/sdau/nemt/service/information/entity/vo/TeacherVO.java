package com.sdau.nemt.service.information.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Date: 2023-08-10 13:38
 * @Author:
 * @Description:
 */
@Data
public class TeacherVO {

    @ApiModelProperty(value = "咨询师姓名")
    private String name;

    @ApiModelProperty(value = "咨询师性别")
    private Integer sex;

    @ApiModelProperty(value = "头衔 1免费咨询师 2会员咨询师")
    private Integer level;
}
