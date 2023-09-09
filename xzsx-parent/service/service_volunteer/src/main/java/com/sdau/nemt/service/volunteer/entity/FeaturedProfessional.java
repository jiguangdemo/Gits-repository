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
 * 一流专业和特色专业
 * </p>
 *
 * @author
 * @since 2023-08-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("volunteer_featured_professional")
@ApiModel(value="FeaturedProfessional对象", description="一流专业和特色专业")
public class FeaturedProfessional extends BaseEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "院校id")
    private String collegesId;

    @ApiModelProperty(value = "双一流专业")
    private String doubleFirstClassProfessional;

    @ApiModelProperty(value = "国家特色专业")
    private String featuredProfessional;

    @ApiModelProperty(value = "逻辑删除")
    @TableLogic
    private Integer isDeleted;


}
