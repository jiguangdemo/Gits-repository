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
 * 院校基本信息
 * </p>
 *
 * @author
 * @since 2023-08-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("volunteer_colleges")
@ApiModel(value="Colleges对象", description="院校基本信息")
public class Colleges extends BaseEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "院校报考代码")
    private String code;

    @ApiModelProperty(value = "校徽")
    private String badge;

    @ApiModelProperty(value = "院校名称")
    private String name;

    @ApiModelProperty(value = "学校类型 0：普通本科     1：专科")
    private Integer type;

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

    @ApiModelProperty(value = "强基计划     0：yes    1：no")
    private Integer strongFoundation;

    @ApiModelProperty(value = "省")
    private String province;

    @ApiModelProperty(value = "市")
    private String city;

    @ApiModelProperty(value = "逻辑删除 1（true）已删除， 0（false）未删除")
    @TableLogic
    private Integer isDeleted;


}
