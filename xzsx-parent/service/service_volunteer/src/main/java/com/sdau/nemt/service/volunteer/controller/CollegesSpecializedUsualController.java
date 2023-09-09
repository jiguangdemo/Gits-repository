package com.sdau.nemt.service.volunteer.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sdau.nemt.common.base.result.R;
import com.sdau.nemt.service.volunteer.entity.CollegesSpecializedUsual;
import com.sdau.nemt.service.volunteer.service.CollegesSpecializedUsualService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 院校专业往年情况 前端控制器
 * </p>
 *
 * @author
 * @since 2023-08-11
 */
@Api(tags = "院校开设专业的历年分数等信息")
@CrossOrigin
@RestController
@RequestMapping("/volunteer/colleges-specialized")
public class CollegesSpecializedUsualController {

//    @Autowired
//    private CollegesSpecializedUsualService collegesSpecializedUsualService;
//
//    @ApiOperation("获取该校该专业历年分数线等信息")
//    @GetMapping("usual/{specialized}")
//    public R getUsual(@ApiParam(name = "specialized",value = "院校开设专业的id") @PathVariable String specialized){
//        LambdaQueryWrapper<CollegesSpecializedUsual> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(CollegesSpecializedUsual::getSpecializedId,specialized);
//        CollegesSpecializedUsual collegesSpecializedUsual = collegesSpecializedUsualService.getBaseMapper().selectOne(wrapper);
//        return R.ok().data("collegesSpecializedUsual",collegesSpecializedUsual);
//    }

}

