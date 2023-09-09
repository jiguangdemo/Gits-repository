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
 * 院校介绍
 * </p>
 *
 * @author
 * @since 2023-08-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("volunteer_colleges_introduce")
@ApiModel(value="CollegesIntroduce对象", description="院校介绍")
public class CollegesIntroduce extends BaseEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "院校主键id")
    private String collegesId;

    @ApiModelProperty(value = "院校介绍")
    private String introduce;

    @ApiModelProperty(value = "院校图片")
    private String images;

    @ApiModelProperty(value = "逻辑删除 1（true）已删除， 0（false）未删除")
    @TableLogic
    private Integer isDeleted;


}
