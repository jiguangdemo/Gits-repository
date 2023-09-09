package com.sdau.nemt.service.volunteer.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sdau.nemt.common.base.result.R;
import com.sdau.nemt.service.volunteer.entity.AATable;
import com.sdau.nemt.service.volunteer.service.AATableService;
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
 * 2022年分数对应最低位次，用于判断学生位次 前端控制器
 * </p>
 *
 * @author
 * @since 2023-08-11
 */
@Api(tags = "一分一段表")
@RestController
@RequestMapping("/volunteer")
public class AATableController {
    @Autowired
    private AATableService aaTableService;
//
//    @GetMapping("list")
//    public R list(){
//        return R.ok().data("list",aaTableService.list());
//    }
@ApiOperation("根据分数查询位次")
@GetMapping("bits/{scores}")
public R getBits(@ApiParam(value = "高考分数",required = true) @PathVariable Integer scores,
                 HttpServletRequest request){
    QueryWrapper<AATable> wrapper = new QueryWrapper<>();
    wrapper.eq("scores",scores);
    AATable aaTable = aaTableService.getBaseMapper().selectOne(wrapper);
    if (aaTable.equals("")||aaTable==null){
        return R.ok().data("bits",55);
    }
    String bits = aaTable.getBit();
    return R.ok().data("bits",bits).data("allBits",aaTable);
}

}

