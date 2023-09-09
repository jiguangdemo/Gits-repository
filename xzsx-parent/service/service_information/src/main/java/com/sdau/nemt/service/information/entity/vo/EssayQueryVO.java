package com.sdau.nemt.service.information.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Date: 2023-08-10 11:57
 * @Author:
 * @Description:
 */
@ApiModel("文章查询视图层条件")
@Data
public class EssayQueryVO {

    @ApiModelProperty(value = "文章标题")
    private String title;

    @ApiModelProperty(value = "文章类型,对应类型主键")
    private String typeId;

    @ApiModelProperty(value = "作者")
    private String author;
}
