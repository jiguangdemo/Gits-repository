package com.sdau.nemt.service.volunteer.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author
 * @since 2023-08-11
 * @Description:
 */
@Data
@ApiModel("模拟填报中的专业条件")
public class MockSpecializedQueryVO {

    @ApiModelProperty(value = "专业门类 如：本科中哲学、工学   专科中：农林牧渔大类、资源环境与安全大类")
    private String phylum;

    @ApiModelProperty(value = "专业类型 0：本科专业    1：专科专业")
    private Integer type;

    @ApiModelProperty(value = "专业名称")
    private String specialized;

    @ApiModelProperty(value = "冲、稳、保、  例如  0：全部志愿，1：冲 ，2：稳 ， 3：保")
    private Integer pick;
}
