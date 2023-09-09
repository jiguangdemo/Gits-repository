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
 * 具体专业
 * </p>
 *
 * @author
 * @since 2023-08-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("volunteer_specialized")
@ApiModel(value="Specialized对象", description="具体专业")
public class Specialized extends BaseEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "专业代码")
    private String code;

    @ApiModelProperty(value = "专业名称")
    private String name;

    @ApiModelProperty(value = "隶属门类id")
    private String classifyId;

    @ApiModelProperty(value = "学制  ")
    private String academic;

    @ApiModelProperty(value = "授予学位   如：工学学位")
    private String degrees;

    @ApiModelProperty(value = "男女比例  例如  40：60   如未知  则'--'")
    private Double maleFemale;

    @ApiModelProperty(value = "逻辑删除 1（true）已删除， 0（false）未删除")
    @TableLogic
    private Integer isDeleted;


}
