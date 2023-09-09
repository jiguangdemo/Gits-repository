package com.sdau.nemt.service.volunteer.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author
 * @since 2023-08-11
 * @Description:
 */
@Data
@ApiModel("专业详情")
public class SpecializedIntroduceDTO {
    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "专业id")
    private String id;

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

    @ApiModelProperty(value = "专业介绍")
    private String introduce;

    @ApiModelProperty(value = "考研方向  如  哲学：马克思主义哲学、中国哲学、外国哲学、哲学")
    private String graduateSchool;

    @ApiModelProperty(value = "开设课程")
    private String course;

    @ApiModelProperty(value = "发展前景简介")
    private String developmentProspects;

    @ApiModelProperty(value = "是否收藏")
    private Integer collection;
}
