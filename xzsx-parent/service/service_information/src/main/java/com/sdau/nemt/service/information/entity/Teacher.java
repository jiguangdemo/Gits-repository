package com.sdau.nemt.service.information.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sdau.nemt.service.base.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 讲师
 * </p>
 *
 * @author
 * @since 2023-08-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("information_teacher")
@ApiModel(value="Teacher对象", description="讲师")
public class Teacher extends BaseEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "咨询师姓名")
    private String name;

    @ApiModelProperty(value = "咨询师性别")
    private Integer sex;

    @ApiModelProperty(value = "咨询师简介")
    private String intro;

    @ApiModelProperty(value = "咨询师资历,一句话说明")
    private String career;

    @ApiModelProperty(value = "方向")
    private Integer direction;

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

    @ApiModelProperty(value = "逻辑删除 1（true）已删除， 0（false）未删除")
    @TableLogic
    private Integer isDeleted;


}
