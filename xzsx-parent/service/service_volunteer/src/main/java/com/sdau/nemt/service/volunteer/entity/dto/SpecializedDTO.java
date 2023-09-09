package com.sdau.nemt.service.volunteer.entity.dto;

import com.sdau.nemt.service.volunteer.entity.Specialized;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author
 * @since 2023-08-11
 * @Description:
 */
@Data
@ApiModel("本科/专科专业展示")
public class SpecializedDTO {
    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "院校开设专业的主键id")
    private String id;

    @ApiModelProperty(value = "专业门类 如：本科中哲学、工学   专科中：农林牧渔大类、资源环境与安全大类")
    private String phylum;

    @ApiModelProperty(value = "专业信息")
    private List<Specialized> list;

}
