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
@ApiModel("是否是国家特色建设专业")
public class SpecialTypeDTO {
    @ApiModelProperty(value = "主键")
    private String Id;

    @ApiModelProperty(value = "专业名称")
    private String specialized;
}
