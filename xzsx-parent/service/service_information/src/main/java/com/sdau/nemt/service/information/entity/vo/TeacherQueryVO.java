package com.sdau.nemt.service.information.entity.vo;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Date: 2023-08-10 22:05
 * @Author:
 * @Description:
 */
@Data
@Accessors(chain = true)
@ApiModel(value="TeacherQueryVO对象", description="讲师")
public class TeacherQueryVO {

    private static final long serialVersionUID=1L;

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

    @ApiModelProperty(value = "咨询师qq账号")
    private String qq;

    @ApiModelProperty(value = "咨询师微信")
    private String wechat;

    @ApiModelProperty(value = "咨询师手机号")
    private String phone;

    @ApiModelProperty(value = "讲师头像")
    private String avatar;

    @ApiModelProperty(value = "排序")
    private Integer sort;

}
