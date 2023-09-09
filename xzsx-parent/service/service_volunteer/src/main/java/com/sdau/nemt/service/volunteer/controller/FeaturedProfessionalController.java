package com.sdau.nemt.service.volunteer.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sdau.nemt.common.base.result.R;
import com.sdau.nemt.service.volunteer.entity.FeaturedProfessional;
import com.sdau.nemt.service.volunteer.service.FeaturedProfessionalService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 一流专业和特色专业 前端控制器
 * </p>
 *
 * @author
 * @since 2023-08-11
 */
//@Api(tags = "院校详情页面开设的专业，如双一流，国家特色等")
//@RestController
//@RequestMapping("/volunteer")
public class FeaturedProfessionalController {

    @Autowired
    private FeaturedProfessionalService featuredProfessionalService;

//    @ApiOperation("获取双一流，国家特色等专业信息")
//    @GetMapping("/featured-professional/{collegesId}")
    private R featured(@ApiParam(name = "collegesId",value = "院校主键") @PathVariable String collegesId, HttpServletRequest request){
        LambdaQueryWrapper<FeaturedProfessional> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FeaturedProfessional::getCollegesId,collegesId);
        FeaturedProfessional featuredProfessional = featuredProfessionalService.getBaseMapper().selectOne(wrapper);
        return R.ok().data("model",featuredProfessional);
    }
}

