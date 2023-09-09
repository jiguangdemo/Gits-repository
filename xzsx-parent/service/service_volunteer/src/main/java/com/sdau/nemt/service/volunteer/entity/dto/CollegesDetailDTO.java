package com.sdau.nemt.service.volunteer.entity.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author
 * @since 2023-08-11
 * @Description:
 */
@ApiModel(value = "视图层院校详细信息")
@Data
public class CollegesDetailDTO {

    @ApiModelProperty(value = "院校主键")
    private String collegesId;

    @ApiModelProperty(value = "院校报考代码")
    private String code;

    @ApiModelProperty(value = "校徽")
    private String badge;

    @ApiModelProperty(value = "院校名称")
    private String name;

    @ApiModelProperty(value = "学校类型 0：普通本科     1：专科")
    private Integer type;

    @ApiModelProperty(value = "院校类别  如：综合类、农林类、理工类")
    private String kind;

    @ApiModelProperty(value = "办学性质 0：公办   1：私办")
    private Integer category;

    @ApiModelProperty(value = "985:    0:yes   1:no")
    private Integer worldClass;

    @ApiModelProperty(value = "211:    0:yes    1:no")
    private Integer innovation;

    @ApiModelProperty(value = "双一流： 0：yes  1：no")
    private Integer doubleFirstClass;

    @ApiModelProperty(value = "强基计划     0：yes    1：no")
    private Integer strongFoundation;

    @ApiModelProperty(value = "省")
    private String province;

    @ApiModelProperty(value = "市")
    private String city;

    @ApiModelProperty(value = "院校介绍")
    private String introduce;

    @ApiModelProperty(value = "院校图片")
    private String images;

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

    @ApiModelProperty(value = "国家特色建设专业")
    private List<SpecialTypeDTO> SpecialTypeList;

    @ApiModelProperty(value = "双一流建设专业")
    private List<DoubleFirstClassDTO> doubleFirstClassList;

    @ApiModelProperty(value = "是否收藏")
    private Integer collection;
}
