package com.sdau.nemt.service.volunteer.entity;

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
 * 2020年分数对应最低位次，用于判断学生位次
 * </p>
 *
 * @author
 * @since 2023-08-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value="AATable对象", description="2023年分数对应最低位次，用于判断学生位次")
public class AATable extends BaseEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "分数段")
    private String scores;

    @ApiModelProperty(value = "位次")
    private String bit;

    @ApiModelProperty(value = "物理位次")
    private String physicsbit;

    @ApiModelProperty(value = "化学位次")
    private String chemistrybit;

    @ApiModelProperty(value = "生物位次")
    private String biologicalbit;

    @ApiModelProperty(value = "政治位次")
    private String politicsbit;

    @ApiModelProperty(value = "历史次")
    private String historybit;

    @ApiModelProperty(value = "地理位次")
    private String geographybit;

    @ApiModelProperty(value = "逻辑删除 1（true）已删除， 0（false）未删除")
    @TableLogic
    private Integer isDeleted;


}
