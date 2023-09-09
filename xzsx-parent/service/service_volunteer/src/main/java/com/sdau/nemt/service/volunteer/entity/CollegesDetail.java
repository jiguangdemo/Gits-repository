package com.sdau.nemt.service.volunteer.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.sdau.nemt.service.base.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 院校详细信息
 * </p>
 *
 * @author
 * @since 2023-08-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("volunteer_colleges_detail")
@ApiModel(value="CollegesDetail对象", description="院校详细信息")
public class CollegesDetail extends BaseEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "院校主键")
    private String collegesId;

    @ApiModelProperty(value = "院校官网")
    private String website;

    @ApiModelProperty(value = "院校联系电话")
    private String phone;

    @ApiModelProperty(value = "院校邮箱")
    private String email;

    @ApiModelProperty(value = "隶属什么部门   如：教育部、公安部、或农村农业部")
    private String subjection;

    @ApiModelProperty(value = "男女比例")
    private Double maleFemale;

    @ApiModelProperty(value = "软科排名")
    private String softFamily;

    @ApiModelProperty(value = "校友会排名")
    private String alumniAssociation;

    @ApiModelProperty(value = "武书连排名")
    private String wushuCompany;

    @ApiModelProperty(value = "QS世界排名")
    private String qsWorld;

    @ApiModelProperty(value = "US世界排名")
    @TableField("us_World")
    private String usWorld;

    @ApiModelProperty(value = "泰晤士排名")
    private String thames;

    @ApiModelProperty(value = "建校时间")
    private Date foundingTime;

    @ApiModelProperty(value = "校区地址  如：木樨地校区：北京市西城区木樨地南里1号,团河校区：北京市大兴区团河路")
    private String address;

    @ApiModelProperty(value = "占地面积     默认单位/亩")
    private String area;

    @ApiModelProperty(value = "博士点数量")
    private String doctoralPoint;

    @ApiModelProperty(value = "硕士点数量")
    private String masterPoint;

    @ApiModelProperty(value = "重点学科数量")
    private String keyDisciplines;

    @ApiModelProperty(value = "逻辑删除 1（true）已删除， 0（false）未删除")
    @TableLogic
    private Integer isDeleted;


}
