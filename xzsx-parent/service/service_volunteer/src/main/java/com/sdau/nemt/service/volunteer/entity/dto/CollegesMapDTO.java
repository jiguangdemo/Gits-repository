package com.sdau.nemt.service.volunteer.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author
 * @since 2023-08-11
 * @Description:
 */
@AllArgsConstructor
@Data
@Accessors(chain = true)
@ApiModel(value="CollegesMapDTO对象", description="院校地图展示信息")
public class CollegesMapDTO {
    @ApiModelProperty("省份")
    private String province;
    @ApiModelProperty("高校总数")
    private Integer total;
//    @ApiModelProperty("本科数量")
//    private Integer benKe;
//    @ApiModelProperty("专科数量")
//    private Integer zhuanKe;
//    @ApiModelProperty("985数量")
//    private Integer worldClassNumber;
//    @ApiModelProperty("211数量")
//    private Integer innovationNumber;

}
