package com.sdau.nemt.service.volunteer.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sdau.nemt.common.base.result.R;
import com.sdau.nemt.service.volunteer.entity.CollegesSpecialized;
import com.sdau.nemt.service.volunteer.entity.SpecializedClassify;
import com.sdau.nemt.service.volunteer.service.CollegesSpecializedService;
import com.sdau.nemt.service.volunteer.service.SpecializedClassifyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 专业类别 门类 前端控制器
 * </p>
 *
 * @author
 * @since 2023-08-11
 */
@Api(tags = "专业分类")
@RestController
@RequestMapping("/volunteer/specialized")
public class SpecializedClassifyController {

    @Autowired
    private SpecializedClassifyService specializedClassifyService;

    @Autowired
    private CollegesSpecializedService collegesSpecializedService;

    @ApiOperation("异步获取专业类别")
    @GetMapping("classify/{level}")
    public R getClassify(@ApiParam(name = "level",value = "水平") @PathVariable Integer level){
        LambdaQueryWrapper<SpecializedClassify> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SpecializedClassify::getLevel,level);
        List<SpecializedClassify> list = specializedClassifyService.getBaseMapper().selectList(wrapper);
        return R.ok().data("list",list);
    }

    @ApiOperation("开设该专业的院校")
    @GetMapping("colleges/{code}")
    public R getColleges(@ApiParam(name = "code",value ="专业代码") @PathVariable String code){
        LambdaQueryWrapper<CollegesSpecialized> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CollegesSpecialized::getCode,code);
        List<CollegesSpecialized> list = collegesSpecializedService.getBaseMapper().selectList(wrapper);
        return R.ok().data("list",list);
    }
}

