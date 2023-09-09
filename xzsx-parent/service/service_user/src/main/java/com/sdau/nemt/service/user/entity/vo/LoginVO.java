package com.sdau.nemt.service.user.entity.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Date: 2023-08-11
 * @Author:
 * @Description:
 */
@Data
@Accessors(chain = true)
@ApiModel(value="登录对象", description="用于封装登录对象")
public class LoginVO {
    @ApiModelProperty(value = "用户账号")
    private String username;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "验证码")
    private String code;
}
