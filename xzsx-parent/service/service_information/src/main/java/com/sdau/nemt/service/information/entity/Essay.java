package com.sdau.nemt.service.information.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.sdau.nemt.service.base.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 文章
 * </p>
 *
 * @author
 * @since 2023-08-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("information_essay")
@ApiModel(value="Essay对象", description="文章")
public class Essay extends BaseEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "文章标题")
    private String title;

    @ApiModelProperty(value = "文章类型,对应类型主键")
    private String typeId;

    @ApiModelProperty(value = "作者")
    private String author;

    @ApiModelProperty(value = "文章简介")
    private String briefly;

    @ApiModelProperty(value = "图片：如需要  则连接阿里OSS")
    private String image;

    @ApiModelProperty(value = "文章内容")
    private String content;

    @ApiModelProperty(value = "发布时间")
    private Date publishTime;

    @ApiModelProperty(value = "文件链接   视文章而定")
    private String file;

    @ApiModelProperty(value = "逻辑删除 1（true）已删除， 0（false）未删除")
    @TableLogic
    private Integer isDeleted;


}
