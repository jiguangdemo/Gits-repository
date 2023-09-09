package com.sdau.nemt.service.volunteer.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author
 * @since 2023-08-11
 * @Description:
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
@ApiModel(value="CityMapDTO对象", description="市高校信息详细信息")
public class CityMapDTO {
    @ApiModelProperty("城市名称")
    private String name;
    @ApiModelProperty("高校数量")
    private Integer value;
    @ApiModelProperty("高校名称")
    private List<CityCollegesDTO> cityCollegesDTOList;
}
