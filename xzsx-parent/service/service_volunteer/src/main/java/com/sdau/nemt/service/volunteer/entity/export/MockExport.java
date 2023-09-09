package com.sdau.nemt.service.volunteer.entity.export;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @author
 * @since 2023-08-11
 * @Description:
 */
@Getter
@Setter
@EqualsAndHashCode
public class MockExport {
    private static final long serialVersionUID=1L;

    @ExcelProperty({"志愿表格","院校开设专业的主键id"})
    private String id;

    @ExcelProperty({"志愿表格","院校名称"})
    private String name;

    @ExcelProperty({"志愿表格", "院校类别  如：综合、农林、理工"})
    private String kind;

    @ExcelProperty({"志愿表格", "办学性质 0：公办   1：私办"})
    private Integer category;

    @ExcelProperty({"志愿表格", "专业代码"})
    private String code;

    @ExcelProperty({"志愿表格", "专业名称"})
    private String specialized;

    @ExcelProperty({"志愿表格", "本专业面向山东招生计划数量"})
    private String plan;

    @ExcelProperty({"志愿表格", "学制  如：四  、 八"})
    private String academic;

    @ExcelProperty({"志愿表格", "学费"})
    private String tuition;

    @ExcelProperty({"志愿表格", "选科要求  如：物/地/史(3选1)   字符串类型"})
    private String subjectRequest;

    @ExcelIgnore
    private Integer sort;
}
