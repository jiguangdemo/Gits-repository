package com.sdau.nemt.service.information.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sdau.nemt.common.base.result.R;
import com.sdau.nemt.service.information.entity.Essay;
import com.sdau.nemt.service.information.entity.vo.EssayQueryVO;
import com.sdau.nemt.service.information.service.EssayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 文章 前端控制器
 * </p>
 *
 * @author
 * @since 2023-08-10
 */
@Api(tags = "高考文章资讯")
@RestController
@RequestMapping("/information/essay")
public class EssayController {

    @Autowired
    private EssayService essayService;

    @ApiOperation("条件分页查询文章")
    @PostMapping("page/{current}/{limit}")
    public R getEssay(@ApiParam(name = "current",value = "当前页",required = true) @PathVariable Long current,
                      @ApiParam(name = "limit",value = "每页多少",required = true) @PathVariable Long limit,
                      @ApiParam(name = "essayQueryVO",value = "文章条件", required = false)@RequestBody(required = false) EssayQueryVO essayQueryVO){
        if(essayQueryVO == null || essayQueryVO.equals("")){
            Page<Essay> page = new Page<>(current,limit);
            IPage<Essay> pageModel = essayService.getBaseMapper().selectPage(page,null);
            return R.ok().data("pageModel",pageModel);
        }
        Page<Essay> page = new Page<>(current,limit);
        IPage<Essay> pageModel = essayService.findQueryPage(page,essayQueryVO);
        return R.ok().data("pageModel",pageModel);
    }
}

