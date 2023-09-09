package com.sdau.nemt.service.volunteer.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sdau.nemt.common.base.result.R;
import com.sdau.nemt.service.volunteer.entity.Colleges;
import com.sdau.nemt.service.volunteer.entity.CollegesDetail;
import com.sdau.nemt.service.volunteer.entity.CollegesSpecialized;
import com.sdau.nemt.service.volunteer.entity.UserColleges;
import com.sdau.nemt.service.volunteer.entity.dto.CollegesDTO;
import com.sdau.nemt.service.volunteer.entity.dto.CollegesDetailDTO;
import com.sdau.nemt.service.volunteer.entity.dto.DoubleFirstClassDTO;
import com.sdau.nemt.service.volunteer.entity.dto.SpecialTypeDTO;
import com.sdau.nemt.service.volunteer.entity.vo.CollegesQueryVO;
import com.sdau.nemt.service.volunteer.entity.vo.InfoVO;
import com.sdau.nemt.service.volunteer.service.CollegesDetailService;
import com.sdau.nemt.service.volunteer.service.CollegesService;
import com.sdau.nemt.service.volunteer.service.CollegesSpecializedService;
import com.sdau.nemt.service.volunteer.service.UserCollegesService;
import com.sdau.nemt.service.volunteer.util.JwtTokenUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Info;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 院校基本信息 前端控制器
 * </p>
 *
 * @author
 * @since 2023-08-11
 */
@Api(tags = "院校")
@CrossOrigin
@RestController
@RequestMapping("/volunteer/colleges")
public class CollegesController {
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private UserCollegesService userCollegesService;

    @Autowired
    private CollegesService collegesService;

    @Autowired
    private CollegesSpecializedService collegesSpecializedService;

    @Autowired
    private CollegesDetailService collegesDetailService;

//    @ApiOperation("条件分页查询院校")
//    @PostMapping ("findQueryPage/{current}/{limit}")
//    public R findPage(@ApiParam(name = "current",value = "当前页",required = true) @PathVariable Long current,
//                      @ApiParam(name = "limit",value = "每页多少",required = true) @PathVariable Long limit,
//                      @ApiParam(name = "collegesQueryVO",value = "查询对象", required = false)@RequestBody(required = false) CollegesQueryVO collegesQueryVO){
//        Page<Colleges> page = new Page<>(current,limit);
//        if(collegesQueryVO==null||collegesQueryVO.equals("")){
//            IPage<Colleges> pageModel = collegesService.getBaseMapper().selectPage(page,null);
//            return R.ok().data("pageModel",pageModel);
//        }
//        IPage<Colleges> pageModel = collegesService.findQueryPage(page,collegesQueryVO);
//        return R.ok().data("pageModel",pageModel);
//    }

    @ApiOperation("条件分页查询院校")
    @PostMapping ("findQueryPage/{current}/{limit}")
    public R findPage(@ApiParam(name = "current",value = "当前页",required = true) @PathVariable Long current,
                      @ApiParam(name = "limit",value = "每页多少",required = true) @PathVariable Long limit,
                      @ApiParam(name = "collegesQueryVO",value = "查询对象", required = false)@RequestBody(required = false) CollegesQueryVO collegesQueryVO,
                      HttpServletRequest request){
        String token = request.getHeader("token");
        String username = JwtTokenUtils.getUsername(token);
        String user = redisTemplate.opsForValue().get("user" + username);
        InfoVO infoVO = JSON.parseObject(user, InfoVO.class);
        String id = infoVO.getId();
        Page<Colleges> page = new Page<>(current,limit);
        if(collegesQueryVO==null||collegesQueryVO.equals("")){
            LambdaQueryWrapper<Colleges> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.orderByAsc(Colleges::getId);
            IPage<Colleges> pageModel = collegesService.getBaseMapper().selectPage(page,queryWrapper);
            List<Colleges> records = pageModel.getRecords();
            List<CollegesDTO> list = new ArrayList<>();
            for (int i = 0; i < records.size(); i++) {
                CollegesDTO collegesDTO = new CollegesDTO();
                BeanUtils.copyProperties(records.get(i),collegesDTO);
                LambdaQueryWrapper<UserColleges> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(UserColleges::getUserId,id);
                wrapper.eq(UserColleges::getCollegesId,records.get(i).getId());
                UserColleges userColleges = userCollegesService.getBaseMapper().selectOne(wrapper);
                if (userColleges==null||userColleges.equals("")){
                    collegesDTO.setCollection(0);
                }
                else {
                    collegesDTO.setCollection(1);
                }
                list.add(collegesDTO);
            }
            IPage<CollegesDTO> iPage = new Page<>(current,limit);
            iPage.setRecords(list);
            iPage.setPages(pageModel.getPages());
            iPage.setTotal(pageModel.getTotal());
            return R.ok().data("pageModel",iPage);
        }
        IPage<Colleges> pageModel = collegesService.findQueryPage(page,collegesQueryVO);
        List<Colleges> records = pageModel.getRecords();
        List<CollegesDTO> list = new ArrayList<>();
        for (int i = 0; i < records.size(); i++) {
            CollegesDTO collegesDTO = new CollegesDTO();
            BeanUtils.copyProperties(records.get(i),collegesDTO);
            LambdaQueryWrapper<UserColleges> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(UserColleges::getUserId,id);
            wrapper.eq(UserColleges::getCollegesId,records.get(i).getId());
            UserColleges userColleges = userCollegesService.getBaseMapper().selectOne(wrapper);
            if (userColleges==null||userColleges.equals("")){
                collegesDTO.setCollection(0);
            }
            else {
                collegesDTO.setCollection(1);
            }
            list.add(collegesDTO);
        }
        IPage<CollegesDTO> iPage = new Page<>(current,limit);
        iPage.setRecords(list);
        iPage.setPages(pageModel.getPages());
        iPage.setTotal(pageModel.getTotal());
        return R.ok().data("pageModel",iPage);
    }

    @ApiOperation("获取单个院校基本信息")
    @GetMapping("one/{id}")
    public R getOne(@ApiParam(value = "院校id",required = true) @PathVariable String id){
        Colleges colleges = collegesService.getBaseMapper().selectById(id);
        return R.ok().data("colleges",colleges);
    }

    @ApiOperation("学校详情")
    @GetMapping("detail/{collegesId}")
    public R getColleges(@ApiParam(name = "collegesId",value = "院校主键id")@PathVariable String collegesId,
                         HttpServletRequest request){
        String token = request.getHeader("token");
        String username = JwtTokenUtils.getUsername(token);
        String user = redisTemplate.opsForValue().get("user" + username);
        InfoVO infoVO = JSON.parseObject(user, InfoVO.class);
        String id = infoVO.getId();
        CollegesDetailDTO collegesDetail = collegesService.getCollegesDetail(collegesId);
        LambdaQueryWrapper<UserColleges> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserColleges::getUserId,id);
        wrapper.eq(UserColleges::getCollegesId,collegesId);
        UserColleges userColleges = userCollegesService.getBaseMapper().selectOne(wrapper);
        if (userColleges==null||userColleges.equals("")){
            collegesDetail.setCollection(0);
        }
        else {
            collegesDetail.setCollection(1);
        }
        //将国家特色专业和双一流专业封装
        LambdaQueryWrapper<CollegesSpecialized> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CollegesSpecialized::getCollegesId,collegesId);
        queryWrapper.eq(CollegesSpecialized::getSpecialType,1);
        List<CollegesSpecialized> collegesSpecializedList = collegesSpecializedService.getBaseMapper().selectList(queryWrapper);
        List<SpecialTypeDTO> specialTypeList = new ArrayList<>();
        if (collegesSpecializedList!=null&&!collegesSpecializedList.equals("")) {
            for (int i = 0; i < collegesSpecializedList.size(); i++) {
                SpecialTypeDTO specialTypeDTO = new SpecialTypeDTO();
                BeanUtils.copyProperties(collegesSpecializedList.get(i), specialTypeDTO);
                specialTypeList.add(specialTypeDTO);
            }
            collegesDetail.setSpecialTypeList(specialTypeList);
        }
        LambdaQueryWrapper<CollegesSpecialized> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(CollegesSpecialized::getCollegesId,collegesId);
        queryWrapper1.eq(CollegesSpecialized::getDoubleFirstClass,1);
        List<CollegesSpecialized> collegesSpecializeds = collegesSpecializedService.getBaseMapper().selectList(queryWrapper1);
        List<DoubleFirstClassDTO> doubleFirstClassDTOList = new ArrayList<>();
        if (collegesSpecializeds!=null&&!collegesSpecializeds.equals("")) {
            for (int i = 0; i < collegesSpecializeds.size(); i++) {
                DoubleFirstClassDTO doubleFirstClassDTO = new DoubleFirstClassDTO();
                BeanUtils.copyProperties(collegesSpecializeds.get(i), doubleFirstClassDTO);
                doubleFirstClassDTOList.add(doubleFirstClassDTO);
            }
            collegesDetail.setDoubleFirstClassList(doubleFirstClassDTOList);
        }
        return R.ok().data("collegesDetail",collegesDetail);
    }
//
//    @ApiOperation("feign远程调用,按照院校listId查询收藏列表")
//    @PostMapping("list")
//    public List<Colleges> list(@RequestBody List<String> listId){
//        List<Colleges> colleges = collegesService.getBaseMapper().selectBatchIds(listId);
//        return colleges;
//    }




}

