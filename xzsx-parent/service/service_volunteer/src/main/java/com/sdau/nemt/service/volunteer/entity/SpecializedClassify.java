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
 * 专业类别 门类
 * </p>
 *
 * @author
 * @since 2023-08-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("volunteer_specialized_classify")
@ApiModel(value="SpecializedClassify对象", description="专业类别 门类")
public class SpecializedClassify extends BaseEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "专业层次  0：本科   1：专科（高职）")
    private Integer level;

    @ApiModelProperty(value = "专业门类 如：本科中哲学、工学   专科中：农林牧渔大类、资源环境与安全大类")
    private String phylum;

    @ApiModelProperty(value = "逻辑删除 1（true）已删除， 0（false）未删除")
    @TableLogic
    private Integer isDeleted;
}
