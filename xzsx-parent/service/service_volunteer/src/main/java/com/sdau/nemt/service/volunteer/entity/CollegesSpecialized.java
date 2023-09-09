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
 * 院校专业
 * </p>
 *
 * @author
 * @since 2023-08-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("volunteer_colleges_specialized")
@ApiModel(value="CollegesSpecialized对象", description="院校专业")
public class CollegesSpecialized extends BaseEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "院校id")
    private String collegesId;

    @ApiModelProperty(value = "专业代码")
    private String code;

    @ApiModelProperty(value = "专业类型 0：本科专业    1：专科专业")
    private Integer type;

    @ApiModelProperty(value = "专业名称")
    private String specialized;

    @ApiModelProperty(value = "国家特色建设专业 0：非国家特色建设   1：国家特色专业 ")
    private Integer specialType;

    @ApiModelProperty(value = "双一流建设专业0：非双一流建设  1：双一流建设专业")
    private Integer doubleFirstClass;

    @ApiModelProperty(value = "本专业面向山东招生计划数量")
    private String plan;

    @ApiModelProperty(value = "学制  如：四  、 八")
    private String academic;

    @ApiModelProperty(value = "学费")
    private String tuition;

    @ApiModelProperty(value = "选科要求  如：物/地/史(3选1)   字符串类型")
    private String subjectRequest;

    @ApiModelProperty(value = "逻辑删除 1（true）已删除， 0（false）未删除")
    @TableLogic
    private Integer isDeleted;


}
