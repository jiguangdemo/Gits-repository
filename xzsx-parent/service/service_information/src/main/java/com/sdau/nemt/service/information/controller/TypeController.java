package com.sdau.nemt.service.information.controller;


import com.sdau.nemt.common.base.result.R;
import com.sdau.nemt.service.information.entity.Type;
import com.sdau.nemt.service.information.service.TypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 文章分类 前端控制器
 * </p>
 *
 * @author
 * @since 2023-08-10
 */
@Api(tags = "文章分类")
@RestController
@RequestMapping("/information")
public class TypeController {

    @Autowired
    private TypeService typeService;

    @ApiOperation("获取文章分类")
    @GetMapping("type")
    public R getType(){
        List<Type> typeList = typeService.getBaseMapper().selectList(null);
        return R.ok().data("typeList",typeList);
    }

}

