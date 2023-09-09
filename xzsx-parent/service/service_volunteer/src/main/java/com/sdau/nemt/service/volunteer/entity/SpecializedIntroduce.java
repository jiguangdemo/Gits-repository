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
 * 专业介绍
 * </p>
 *
 * @author
 * @since 2023-08-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("volunteer_specialized_introduce")
@ApiModel(value="SpecializedIntroduce对象", description="专业介绍")
public class SpecializedIntroduce extends BaseEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "对应主键id")
    private String specializedId;

    @ApiModelProperty(value = "专业介绍")
    private String introduce;

    @ApiModelProperty(value = "考研方向  如  哲学：马克思主义哲学、中国哲学、外国哲学、哲学")
    private String graduateSchool;

    @ApiModelProperty(value = "开设课程")
    private String course;

    @ApiModelProperty(value = "发展前景简介")
    private String developmentProspects;

    @ApiModelProperty(value = "逻辑删除 1（true）已删除， 0（false）未删除")
    @TableLogic
    private Integer isDeleted;


}
