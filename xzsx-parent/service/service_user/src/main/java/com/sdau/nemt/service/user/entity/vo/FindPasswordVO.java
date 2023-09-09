package com.sdau.nemt.service.user.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Date: 2023-08-11
 * @Author:
 * @Description:
 */
@Data
@ApiModel("修改密码前端提交信息")
public class FindPasswordVO {
    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("第一次密码")
    private String onePassword;

    @ApiModelProperty("第二次密码")
    private String twoPassword;
}
