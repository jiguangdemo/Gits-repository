package com.sdau.nemt.service.volunteer.entity.dto;

import com.sdau.nemt.service.volunteer.entity.Colleges;
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
@ApiModel(value="ProvinceMapDTO对象", description="省份详细信息")
public class ProvinceMapDTO {
//    @ApiModelProperty("省份")
//    private String province;
    @ApiModelProperty("高校总数")
    private Integer total;
    @ApiModelProperty("本科数量")
    private Integer benKe;
    @ApiModelProperty("本科院校名称列表")
    private List<Colleges> benkeList;
    @ApiModelProperty("专科数量")
    private Integer zhuanKe;
    @ApiModelProperty("专科院校名称列表")
    private List<Colleges> zhuanKeList;
    @ApiModelProperty("985数量")
    private Integer worldClassNumber;
    @ApiModelProperty("985院校列表")
    private List<Colleges> worldClassList;
    @ApiModelProperty("211数量")
    private Integer innovationNumber;
    @ApiModelProperty("211院校列表")
    private List<Colleges> innovationNumberList;
    @ApiModelProperty("院校以及城市")
    private List<CityMapDTO> cityMapDTOList;

}
