package com.sdau.nemt.service.volunteer.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sdau.nemt.common.base.result.R;
import com.sdau.nemt.service.volunteer.entity.DoubleFirstClass;
import com.sdau.nemt.service.volunteer.entity.NationalSpecialized;
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
 *  前端控制器
 * </p>
 *
 * @author
 * @since 2023-08-11
 */
@Api(tags = "国家特色专业")
@RestController
@RequestMapping("/volunteer")
public class NationalSpecializedController {

    @Autowired
    private NationalSpecializedService nationalSpecializedService;

    @ApiOperation("获取国家特色专业名称")
    @GetMapping("/national-specialized/{collegesId}")
    private R nationalSpecialized(@ApiParam(name = "collegesId",value = "院校主键") @PathVariable String collegesId, HttpServletRequest request){
        LambdaQueryWrapper<NationalSpecialized> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(NationalSpecialized::getCollegesId,collegesId);
        List<NationalSpecialized> doubleFirstClasses = nationalSpecializedService.getBaseMapper().selectList(wrapper);
        List<String> modelList = new ArrayList<>();
        for (int i = 0; i < doubleFirstClasses.size(); i++) {
            modelList.add(doubleFirstClasses.get(i).getName());
        }
        return R.ok().data("modelList",modelList);
    }
}

