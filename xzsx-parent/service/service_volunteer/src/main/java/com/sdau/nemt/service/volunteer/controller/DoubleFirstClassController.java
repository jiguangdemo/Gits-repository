package com.sdau.nemt.service.volunteer.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sdau.nemt.common.base.result.R;
import com.sdau.nemt.service.volunteer.entity.DoubleFirstClass;
import com.sdau.nemt.service.volunteer.entity.FeaturedProfessional;
import com.sdau.nemt.service.volunteer.entity.NationalSpecialized;
import com.sdau.nemt.service.volunteer.service.DoubleFirstClassService;
import com.sdau.nemt.service.volunteer.service.FeaturedProfessionalService;
import com.sdau.nemt.service.volunteer.service.NationalSpecializedService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 双一流专业 前端控制器
 * </p>
 *
 * @author
 * @since 2023-08-11
 */
@Api(tags = "院校开设的双一流专业")
@RestController
@RequestMapping("/volunteer")
public class DoubleFirstClassController {


    @Autowired
    private DoubleFirstClassService doubleFirstClassService;

    @Autowired
    private NationalSpecializedService nationalSpecializedService;

    @ApiOperation("获取双一流专业名称")
    @GetMapping("/double-first-class/{collegesId}")
    private R doubleFirstClass(@ApiParam(name = "collegesId",value = "院校主键") @PathVariable String collegesId, HttpServletRequest request){
        LambdaQueryWrapper<DoubleFirstClass> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DoubleFirstClass::getCollegesId,collegesId);
        List<DoubleFirstClass> doubleFirstClasses = doubleFirstClassService.getBaseMapper().selectList(wrapper);
        List<String> modelList = new ArrayList<>();
        for (int i = 0; i < doubleFirstClasses.size(); i++) {
            modelList.add(doubleFirstClasses.get(i).getName());
        }
        LambdaQueryWrapper<NationalSpecialized> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(NationalSpecialized::getCollegesId,collegesId);
        List<NationalSpecialized> nationalSpecialized = nationalSpecializedService.getBaseMapper().selectList(wrapper1);
        List<String> modelList2 = new ArrayList<>();
        for (int i = 0; i < nationalSpecialized.size(); i++) {
            modelList2.add(nationalSpecialized.get(i).getName());
        }
        return R.ok().data("modelList",modelList).data("size1",modelList.size()).
                data("nationalModelList",modelList2).data("size2",modelList2.size());
    }


}

