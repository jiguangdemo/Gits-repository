package com.sdau.nemt.service.user.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Date: 2023-08-11
 * @Author:
 * @Description:
 */
@Data
@Accessors(chain = true)
@ApiModel(value="注册对象", description="用于封装注册对象信息")
public class RegisterVO {

    @ApiModelProperty(value = "验证码")
    private String code;

    @ApiModelProperty(value = "用户昵称")
    private String nickName;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "密码")
    private String rePassword;

    @ApiModelProperty(value = "用户真实姓名")
    private String realName;

    @ApiModelProperty(value = "用户手机号")
    private String phone;

    @ApiModelProperty(value = "用户邮箱")
    private String email;
}
