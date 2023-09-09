package com.sdau.nemt.service.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.sdau.nemt.service.base.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author
 * @since 2023-08-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("user_identity")
@ApiModel(value="Identity对象", description="")
public class Identity extends BaseEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "用户账号")
    private String username;

    @ApiModelProperty(value = "角色")
    private String roleId;


}
