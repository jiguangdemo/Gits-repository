package com.sdau.nemt.service.volunteer.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author
 * @since 2023-08-11
 * @Description:
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
@ApiModel(value="GraduateDTO对象", description="毕业落实率")
public class GraduateDTO {
    @ApiModelProperty("院校名称")
    private String name;

    @ApiModelProperty("该项评分")
    private Double value;
}
