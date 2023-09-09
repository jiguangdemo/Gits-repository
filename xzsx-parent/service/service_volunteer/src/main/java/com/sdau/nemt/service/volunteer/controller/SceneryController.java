package com.sdau.nemt.service.volunteer.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sdau.nemt.common.base.result.R;
import com.sdau.nemt.service.volunteer.entity.FeaturedProfessional;
import com.sdau.nemt.service.volunteer.entity.Scenery;
import com.sdau.nemt.service.volunteer.service.SceneryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 校园风光 前端控制器
 * </p>
 *
 * @author
 * @since 2023-08-11
 */
@Api(tags = "院校风景")
@RestController
@RequestMapping("/volunteer")
public class SceneryController {

    @Autowired
    private SceneryService sceneryService;

    @ApiOperation("获取院校风景")
    @GetMapping("/scenery/{collegesId}")
    private R bigData(@ApiParam(name = "collegesId",value = "院校主键") @PathVariable String collegesId, HttpServletRequest request){
        LambdaQueryWrapper<Scenery> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Scenery::getCollegesId,collegesId);
        List<Scenery> sceneryList = sceneryService.getBaseMapper().selectList(wrapper);
        return R.ok().data("modelList",sceneryList);
    }

}

