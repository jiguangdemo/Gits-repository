package com.sdau.nemt.service.user.entity;

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
 * 用户成绩信息
 * </p>
 *
 * @author
 * @since 2023-08-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("user_grades")
@ApiModel(value="Grades对象", description="用户成绩信息")
public class Grades extends BaseEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "用户id")
    private String userId;

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

    @ApiModelProperty(value = "逻辑删除 1（true）已删除， 0（false）未删除")
    @TableLogic
    private Integer isDeleted;


}
