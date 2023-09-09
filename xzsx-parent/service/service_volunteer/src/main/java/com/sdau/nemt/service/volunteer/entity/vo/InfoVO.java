package com.sdau.nemt.service.volunteer.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author
 * @since 2023-08-11
 * @Description:
 */
@Data
@Accessors(chain = true)
@ApiModel(value="用户信息", description="封装用户信息，作为前端展示")
public class InfoVO {
    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "ID")
    private String id;

    @ApiModelProperty(value = "用户昵称")
    private String nickName;

    @ApiModelProperty(value = "用户账号     强制和邮箱相同")
    private String username;

    @ApiModelProperty(value = "用户头像")
    private String avatar;

    @ApiModelProperty(value = "用户真实姓名")
    private String realName;

    @ApiModelProperty(value = "用户手机号")
    private String phone;

    @ApiModelProperty(value = "用户邮箱")
    private String email;

    @ApiModelProperty(value = "身份  0：普通用户 ，1：会员")
    private Integer identity;

    @ApiModelProperty(value = "省份   如山东省、北京市、新疆维吾尔自治区")
    private String province;

    @ApiModelProperty(value = "市    如泰安市、青岛市")
    private String city;

    @ApiModelProperty(value = "县/区    如泰山区   曹县  ")
    private String county;

    @ApiModelProperty(value = "高中学校名字")
    private String seniorName;

    @ApiModelProperty(value = "选科一  1：物理2：化学3：生物4：政治5：历史6：地理")
    private Integer subjectsOne;

    @ApiModelProperty(value = "选科二  1：物理2：化学3：生物4：政治5：历史6：地理")
    private Integer subjectsTwo;

    @ApiModelProperty(value = "选科三  1：物理2：化学3：生物4：政治5：历史6：地理")
    private Integer subjectsThree;

    @ApiModelProperty(value = "高考成绩")
    private String scores;

    @ApiModelProperty(value = "高考位次")
    private String bits;

}
