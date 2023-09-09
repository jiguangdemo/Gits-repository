package com.sdau.nemt.service.volunteer.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sdau.nemt.common.base.result.R;
import com.sdau.nemt.service.volunteer.entity.Colleges;
import com.sdau.nemt.service.volunteer.entity.Specialized;
import com.sdau.nemt.service.volunteer.entity.SpecializedClassify;
import com.sdau.nemt.service.volunteer.entity.UserSpecialized;
import com.sdau.nemt.service.volunteer.entity.dto.SpecializedDTO;
import com.sdau.nemt.service.volunteer.entity.vo.InfoVO;
import com.sdau.nemt.service.volunteer.entity.vo.SpecializedQueryVO;
import com.sdau.nemt.service.volunteer.service.SpecializedClassifyService;
import com.sdau.nemt.service.volunteer.service.SpecializedService;
import com.sdau.nemt.service.volunteer.service.UserSpecializedService;
import com.sdau.nemt.service.volunteer.util.JwtTokenUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 具体专业 前端控制器
 * </p>
 *
 * @author
 * @since 2023-08-11
 */
@Api(tags = "专业")
@CrossOrigin
@RestController
@RequestMapping("/volunteer/specialized")
public class SpecializedController {
    @Autowired
    private UserSpecializedService userSpecializedService;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private SpecializedService specializedService;

    @Autowired
    private SpecializedClassifyService specializedClassifyService;

  /*  @ApiOperation("条件分页查询专业列表")
    @PostMapping("page/{current}/{limit}")
    public R pageSpecialized(
            @ApiParam(name = "current", value = "当前页", required = true) @PathVariable Long current,
            @ApiParam(name = "limit", value = "每页多少", required = true) @PathVariable Long limit,
            @ApiParam(name = "specializedQueryVO", value = "专业查询条件对象", required = false) @RequestBody(required = false) SpecializedQueryVO specializedQueryVO){
        Page<Specialized> page = new Page<>(current,limit);
        if(specializedQueryVO==null||specializedQueryVO.equals("")){
            IPage<Specialized> pageModel = specializedService.getBaseMapper().selectPage(page, null);
            return R.ok().data("pageModel",pageModel);
        }
        IPage<Specialized> pageModel = specializedService.findQueryPage(page,specializedQueryVO);
        return R.ok().data("pageModel",pageModel);
    }*/

    @ApiOperation("条件查询专业列表   非分页")
    @PostMapping("page/{current}/{limit}")
    public R pageSpecialized(
//            @ApiParam(name = "current", value = "当前页", required = true) @PathVariable Long current,
//            @ApiParam(name = "limit", value = "每页多少", required = true) @PathVariable Long limit,
            @ApiParam(name = "specializedQueryVO", value = "专业查询条件对象", required = false) @RequestBody(required = false) SpecializedQueryVO specializedQueryVO){
//        Page page = new Page<>(current,limit);
        String name = specializedQueryVO.getName();
        String code = specializedQueryVO.getCode();
        String classifyId = specializedQueryVO.getClassifyId();
        LambdaQueryWrapper<SpecializedClassify> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SpecializedClassify::getId,classifyId);
        SpecializedClassify specializedClassify = specializedClassifyService.getBaseMapper().selectOne(queryWrapper);
        LambdaQueryWrapper<Specialized> wrapper = new LambdaQueryWrapper<>();
        if (name!=null&&!name.equals("")){
            wrapper.like(Specialized::getName,name);
        }
        if (code!=null&&!code.equals("")){
            wrapper.eq(Specialized::getCode,code);
        }
        if (classifyId!=null&&!classifyId.equals("")){
            wrapper.eq(Specialized::getClassifyId,classifyId);
        }
        List<Specialized> specializedList = specializedService.getBaseMapper().selectList(wrapper);
        SpecializedDTO specializedDTO = new SpecializedDTO();
        BeanUtils.copyProperties(specializedClassify,specializedDTO);
        specializedDTO.setList(specializedList);
        IPage<SpecializedDTO> pageModel = new Page<>();
        List<SpecializedDTO> list = new ArrayList<>();
        list.add(specializedDTO);
        pageModel.setRecords(list);
        pageModel.setTotal(list.size());
        return R.ok().data("pageModel",pageModel);
//
//        IPage<Specialized> pageModel = specializedService.findQueryPage(page,specializedQueryVO);
//        return R.ok().data("pageModel",pageModel);

    }

    @ApiOperation("查询所有本科或专科专业")
    @PostMapping("page-all/{current}/{limit}/{level}")
    public R pageAllSpecialized(
            @ApiParam(name = "current", value = "当前页", required = true) @PathVariable Long current,
            @ApiParam(name = "limit", value = "每页多少", required = true) @PathVariable Long limit,
            @ApiParam(name = "level",value = "本科为0，专科为1",required = true) @PathVariable Integer level
    ){
        Page page = new Page<>(current,limit);
        LambdaQueryWrapper<SpecializedClassify> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SpecializedClassify::getLevel,level);
        IPage pageModel = specializedClassifyService.getBaseMapper().selectPage(page, wrapper);
        List<SpecializedClassify> specializedClassifyList = pageModel.getRecords();
        List<SpecializedDTO> list = new ArrayList<>();
        for (int i = 0; i < specializedClassifyList.size(); i++) {
            SpecializedDTO specializedDTO = new SpecializedDTO();
            BeanUtils.copyProperties(specializedClassifyList.get(i),specializedDTO);
            LambdaQueryWrapper<Specialized> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Specialized::getClassifyId,specializedClassifyList.get(i).getId());
            List<Specialized> specializedList = specializedService.getBaseMapper().selectList(queryWrapper);
            list.add(specializedDTO);
            list.get(i).setList(specializedList);
        }
        pageModel.setRecords(list);
        return R.ok().data("pageModel",pageModel);
    }


    @ApiOperation("专业收藏")
    @GetMapping("collection/{specializedId}")
    public R collection(@ApiParam(value = "专业id",name = "specializedId") @PathVariable String specializedId
            , HttpServletRequest request){
        String token = request.getHeader("token");
        String username = JwtTokenUtils.getUsername(token);
        String user = redisTemplate.opsForValue().get("user" + username);
        InfoVO infoVO = JSON.parseObject(user, InfoVO.class);
        String id = infoVO.getId();
        UserSpecialized userSpecialized = new UserSpecialized();
        userSpecialized.setUserId(id);
        userSpecialized.setSpecializedId(specializedId);
        LambdaQueryWrapper<UserSpecialized> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserSpecialized::getUserId,id);
        queryWrapper.eq(UserSpecialized::getSpecializedId,specializedId);
        UserSpecialized selectOne = userSpecializedService.getBaseMapper().selectOne(queryWrapper);
        if (selectOne!=null&&!selectOne.equals("")){
            return R.error().message("您已经收藏过该院校");
        }
        userSpecializedService.getBaseMapper().insert(userSpecialized);
        return R.ok().message("收藏成功");
    }

    @ApiOperation("查看所有收藏的专业")
    @GetMapping("all-collection/{current}/{limit}")
    public R allCollection(  @ApiParam(name = "current", value = "当前页", required = true) @PathVariable Long current,
                             @ApiParam(name = "limit", value = "每页多少", required = true) @PathVariable Long limit,
                             HttpServletRequest request){
        String token = request.getHeader("token");
        String username = JwtTokenUtils.getUsername(token);
        String user = redisTemplate.opsForValue().get("user" + username);
        InfoVO infoVO = JSON.parseObject(user, InfoVO.class);
        String id = infoVO.getId();
        Page page = new Page<>(current,limit);
        LambdaQueryWrapper<UserSpecialized> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserSpecialized::getUserId,id);
        IPage<UserSpecialized> selectPage = userSpecializedService.getBaseMapper().selectPage(page, queryWrapper);
        List<UserSpecialized> records = selectPage.getRecords();
        List<Specialized> list = new ArrayList<>();
        for (int i = 0; i < records.size(); i++) {
            Specialized specialized = specializedService.getBaseMapper().selectById(records.get(i).getSpecializedId());
            list.add(specialized);
        }
        IPage<Specialized> pageModel = new Page<>(current,limit);
        pageModel.setPages(selectPage.getPages());
        pageModel.setTotal(selectPage.getTotal());
        pageModel.setRecords(list);
        return R.ok().data("pageModel",pageModel);
    }

    @ApiOperation("取消收藏该专业")
    @DeleteMapping("remove-collection/{specializedId}")
    public R removeCollection(@ApiParam(value = "专业id",name = "specializedId") @PathVariable String specializedId
            , HttpServletRequest request){
        String token = request.getHeader("token");
        String username = JwtTokenUtils.getUsername(token);
        String user = redisTemplate.opsForValue().get("user" + username);
        InfoVO infoVO = JSON.parseObject(user, InfoVO.class);
        String id = infoVO.getId();
        LambdaQueryWrapper<UserSpecialized> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserSpecialized::getUserId,id);
        queryWrapper.eq(UserSpecialized::getSpecializedId,specializedId);
        userSpecializedService.getBaseMapper().delete(queryWrapper);
        return R.ok().message("取消收藏成功");

    }


}

