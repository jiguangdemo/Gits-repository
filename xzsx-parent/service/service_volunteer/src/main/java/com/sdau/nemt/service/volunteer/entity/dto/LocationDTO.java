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
@ApiModel("志愿位置情况")
public class LocationDTO {
    @ApiModelProperty("位置")
    private Integer sort;

    @ApiModelProperty("是否存在志愿")
    private Integer exist;
}
