package com.sdau.nemt.service.information.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Date: 2023-08-10 13:43
 * @Author:
 * @Description:
 */
@Data
public class TeacherDTO {

    @ApiModelProperty("咨询师id")
    private String id;

    @ApiModelProperty(value = "咨询师姓名")
    private String name;

    @ApiModelProperty(value = "咨询师性别")
    private Integer sex;

    @ApiModelProperty(value = "咨询师简介")
    private String intro;

    @ApiModelProperty(value = "咨询师资历,一句话说明")
    private String career;

    @ApiModelProperty(value = "头衔 1免费咨询师 2会员咨询师")
    private Integer level;

    @ApiModelProperty(value = "讲师头像")
    private String avatar;

}
