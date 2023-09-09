package com.sdau.nemt.service.volunteer.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sdau.nemt.common.base.result.R;
import com.sdau.nemt.service.volunteer.entity.*;
import com.sdau.nemt.service.volunteer.entity.dto.*;
import com.sdau.nemt.service.volunteer.entity.vo.CollegesQueryVO;
import com.sdau.nemt.service.volunteer.entity.vo.InfoVO;
import com.sdau.nemt.service.volunteer.entity.vo.MockCollegesQueryVO;
import com.sdau.nemt.service.volunteer.entity.vo.VolunteerQueryVO;
import com.sdau.nemt.service.volunteer.service.*;
import com.sdau.nemt.service.volunteer.util.JwtTokenUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Info;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * <p>
 * 院校专业 前端控制器
 * </p>
 *
 * @author
 * @since 2023-08-11
 */
@Slf4j
@CrossOrigin
@Api(tags = "院校开设专业，院校录取概率也包含在此处")
@RestController
@RequestMapping("/volunteer/colleges-specialized")
public class CollegesSpecializedController {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private UserSpecializedService userSpecializedService;

    @Autowired
    private CollegesService collegesService;

    @Autowired
    private CollegesSpecializedUsualService collegesSpecializedUsualService;

    @Autowired
    private CollegesSpecializedService collegesSpecializedService;

    @Autowired
    private MockService mockService;

    @ApiOperation("获取某院校开设的专业")
    @GetMapping("one/{current}/{limit}/{collegesId}")
    public R one(
                 @ApiParam(name = "current", value = "当前页", required = true) @PathVariable Long current,
                 @ApiParam(name = "limit", value = "每页多少", required = true) @PathVariable Long limit,
                 @ApiParam(name = "collegesId", value = "院校id", required = true)@PathVariable String collegesId,
                 HttpServletRequest request) {
        String token = request.getHeader("token");
        String username = JwtTokenUtils.getUsername(token);
        String user = redisTemplate.opsForValue().get("user" + username);
        InfoVO infoVO = JSON.parseObject(user, InfoVO.class);
        String id = infoVO.getId();
        Page<CollegesSpecialized> page = new Page<>(current,limit);
        LambdaQueryWrapper<CollegesSpecialized> wrapper = new LambdaQueryWrapper<>();
        log.info("collegesId:"+collegesId);
        wrapper.eq(CollegesSpecialized::getCollegesId,collegesId);
        IPage<CollegesSpecialized> pageModel = collegesSpecializedService.getBaseMapper().selectPage(page, wrapper);
        IPage<CollegesOpenSpecializedDTO> iPage = new Page<>(current,limit);
        iPage.setTotal(pageModel.getTotal());
        iPage.setPages(pageModel.getPages());
        List<CollegesSpecialized> records = pageModel.getRecords();
        List<CollegesOpenSpecializedDTO> list = new ArrayList<>();
        for (int i = 0; i < records.size(); i++) {
            CollegesOpenSpecializedDTO collegesOpenSpecializedDTO = new CollegesOpenSpecializedDTO();
            BeanUtils.copyProperties(records.get(i),collegesOpenSpecializedDTO);
            LambdaQueryWrapper<UserSpecialized> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(UserSpecialized::getUserId,id);
            queryWrapper.eq(UserSpecialized::getSpecializedId,records.get(i).getId());
            UserSpecialized userSpecialized = userSpecializedService.getBaseMapper().selectOne(queryWrapper);
            if (userSpecialized==null||userSpecialized.equals("")){
                collegesOpenSpecializedDTO.setCollection(0);
            }else {
                collegesOpenSpecializedDTO.setCollection(1);
            }
            list.add(collegesOpenSpecializedDTO);
        }
        iPage.setRecords(list);

        return R.ok().data("pageModel",iPage);
    }



    @ApiOperation("获取院校开设的所有专业及其分数线")
    @GetMapping("all-usual/{current}/{limit}/{collegesId}")
    public R allUsual(@ApiParam(name = "current", value = "当前页", required = true) @PathVariable Long current,
                      @ApiParam(name = "limit", value = "每页多少", required = true) @PathVariable Long limit,
                      @ApiParam(name = "collegesId",value = "院校主键")@PathVariable String collegesId,
                      HttpServletRequest request){
        String token = request.getHeader("token");
        String username = JwtTokenUtils.getUsername(token);
        String user = redisTemplate.opsForValue().get("user" + username);
        InfoVO infoVO = JSON.parseObject(user, InfoVO.class);
        String id = infoVO.getId();
        Page page = new Page<>(current,limit);
        List<CollegesSpecializedDTO> list = new ArrayList<>();
        LambdaQueryWrapper<CollegesSpecialized> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CollegesSpecialized::getCollegesId,collegesId);
        IPage selectPage = collegesSpecializedService.getBaseMapper().selectPage(page, wrapper);
//        List<CollegesSpecialized> collegesSpecializedList = collegesSpecializedService.getBaseMapper().selectList(wrapper);
        List<CollegesSpecialized> collegesSpecializedList = selectPage.getRecords();
        for (int i = 0; i < collegesSpecializedList.size(); i++) {
            CollegesSpecializedDTO collegesSpecializedDTO = new CollegesSpecializedDTO();
            CollegesSpecializedUsual collegesSpecializedUsual = collegesSpecializedUsualService.getBaseMapper().selectById(collegesSpecializedList.get(i).getId());
            BeanUtils.copyProperties(collegesSpecializedUsual,collegesSpecializedDTO);
            BeanUtils.copyProperties(collegesSpecializedList.get(i),collegesSpecializedDTO);
            LambdaQueryWrapper<UserSpecialized> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(UserSpecialized::getUserId,id);
            queryWrapper.eq(UserSpecialized::getSpecializedId,collegesSpecializedDTO.getId());
            UserSpecialized userSpecialized = userSpecializedService.getBaseMapper().selectOne(queryWrapper);
            if (userSpecialized==null||userSpecialized.equals("")){
                collegesSpecializedDTO.setCollection(0);
            }else {
                collegesSpecializedDTO.setCollection(1);
            }
            list.add(collegesSpecializedDTO);
        }
        IPage pageModel = new Page(current,limit);
        pageModel.setTotal(selectPage.getTotal());
        pageModel.setRecords(list);
        return R.ok().data("pageModel",pageModel);

    }


    @ApiOperation("获取该院校所开设的该专业的历年分数线以及平均分数等信息")
    @GetMapping("usual/{collegesSpecializedId}")
    public R usual(@ApiParam(name = "collegesSpecializedId",value = "院校开设专业的id")@PathVariable String collegesSpecializedId){
        LambdaQueryWrapper<CollegesSpecializedUsual> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CollegesSpecializedUsual::getSpecializedId,collegesSpecializedId);
        CollegesSpecializedUsual collegesSpecializedUsual = collegesSpecializedUsualService.getBaseMapper().selectOne(wrapper);
        CollegesSpecialized collegesSpecialized = collegesSpecializedService.getBaseMapper().selectById(collegesSpecializedId);
        CollegesSpecializedDTO collegesSpecializedDTO = new CollegesSpecializedDTO();
        String collegesId = collegesSpecialized.getCollegesId();
        Colleges colleges = collegesService.getBaseMapper().selectById(collegesId);
        BeanUtils.copyProperties(collegesSpecializedUsual,collegesSpecializedDTO);
        BeanUtils.copyProperties(colleges,collegesSpecializedDTO);
        BeanUtils.copyProperties(collegesSpecialized,collegesSpecializedDTO);
        return R.ok().data("model",collegesSpecializedDTO);
    }

    @ApiOperation("分页条件查询所有院校录取概率")
    @PostMapping("colleges-probability/{current}/{limit}")
    public R collegesProbability(HttpServletRequest request,
                                 @ApiParam(name = "current", value = "当前页", required = true) @PathVariable Long current,
                                 @ApiParam(name = "limit", value = "每页多少", required = true) @PathVariable Long limit,
                                 @ApiParam(name = "collegesQueryVO", value = "院校条件", required = false)@RequestBody CollegesQueryVO collegesQueryVO){
        String token = request.getHeader("token");
        String username = JwtTokenUtils.getUsername(token);
        String user = redisTemplate.opsForValue().get("user" + username);
        InfoVO infoVO = JSON.parseObject(user, InfoVO.class);
        String id = infoVO.getId();
        String scores = infoVO.getScores();
        if(scores==null||scores.equals("")){
            return R.error().message("您未设置成绩或位次信息，无法使用本功能");
        }
        if (infoVO.getBits()==null||infoVO.getBits().equals("")){
            return R.error().message("您未设置成绩或位次信息，无法使用本功能");
        }
        Page<Colleges> page = new Page<>(current,limit);
        if(collegesQueryVO==null||collegesQueryVO.equals("")){
            IPage<Colleges> pageModel = collegesService.getBaseMapper().selectPage(page,null);
            List<Colleges> records = pageModel.getRecords();
            List<CollegesProbabilityDTO> dtoList = new ArrayList<>();
            for (int i = 0; i < records.size(); i++) {
                BeanUtils.copyProperties(records.get(i),dtoList.get(i));
            }
            List<CollegesProbabilityDTO> list = collegesSpecializedService.addProbability(infoVO, dtoList);
//            Page<CollegesProbabilityDTO> dtoPage = new Page<>(current,limit,records.size());
            IPage<CollegesProbabilityDTO> collegesModel = new Page<>();
            collegesModel.setRecords(list);
            collegesModel.setCurrent(current);
            collegesModel.setSize(limit);
            collegesModel.setTotal(records.size());
            return R.ok().data("collegesModel",collegesModel);
        }else {
            IPage<Colleges> pageModel = collegesService.findQueryPage(page, collegesQueryVO);
            List<Colleges> records = pageModel.getRecords();
            List<CollegesProbabilityDTO> dtoList = new ArrayList<>();
            for (int i = 0; i < records.size(); i++) {
                CollegesProbabilityDTO collegesProbabilityDTO = new CollegesProbabilityDTO();
                BeanUtils.copyProperties(records.get(i),collegesProbabilityDTO);
                dtoList.add(collegesProbabilityDTO);
            }
            List<CollegesProbabilityDTO> list = collegesSpecializedService.addProbability(infoVO, dtoList);
//            Page<CollegesProbabilityDTO> dtoPage = new Page<>(current,limit,records.size());
            IPage<CollegesProbabilityDTO> collegesModel = new Page<>();
            collegesModel.setRecords(list);
            collegesModel.setCurrent(current);
            collegesModel.setSize(limit);
            collegesModel.setTotal(records.size());
            return R.ok().data("collegesModel",collegesModel);
        }

    }

    @ApiOperation("分页查询某院校所有专业录取概率")
    @GetMapping("specialized-probability/{collegesId}/{current}/{limit}")
    public R specialized(HttpServletRequest request,
                         @ApiParam(name = "collegesId",value = "院校id") @PathVariable String collegesId,
                         @ApiParam(name = "current", value = "当前页", required = true) @PathVariable Long current,
                         @ApiParam(name = "limit", value = "每页多少", required = true) @PathVariable Long limit){
        String token = request.getHeader("token");
        String username = JwtTokenUtils.getUsername(token);
        String user = redisTemplate.opsForValue().get("user" + username);
        InfoVO infoVO = JSON.parseObject(user, InfoVO.class);
        String id = infoVO.getId();
        String scores = infoVO.getScores();
        if(scores==null||scores.equals("")){
            return R.error().message("您未设置成绩或位次信息，无法使用本功能");
        }
        if (infoVO.getBits()==null||infoVO.getBits().equals("")){
            return R.error().message("您未设置成绩或位次信息，无法使用本功能");
        }
        Page<CollegesSpecialized> page = new Page<>(current,limit);
        LambdaQueryWrapper<CollegesSpecialized> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CollegesSpecialized::getCollegesId,collegesId);
        IPage<CollegesSpecialized> collegesSpecializedPage = collegesSpecializedService.getBaseMapper().selectPage(page,wrapper);
        List<CollegesSpecialized> collegesSpecializedList = collegesSpecializedPage.getRecords();
        List<SpecializedProbabilityDTO> list = collegesSpecializedService.specializedProbability(infoVO, collegesSpecializedList);
        for (int i = 0; i < list.size(); i++) {
            LambdaQueryWrapper<Mock> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Mock::getUserId,id);
            queryWrapper.eq(Mock::getMockId,list.get(i).getId());
            Mock mock = mockService.getBaseMapper().selectOne(queryWrapper);
            if (mock!=null&&!mock.equals("")){
                list.get(i).setSort(mock.getSort());
                list.get(i).setCollection(1);
            }else {
                list.get(i).setSort(0);
                list.get(i).setCollection(0);
            }
        }
        IPage<SpecializedProbabilityDTO> pageModel = new Page<>();
        pageModel.setCurrent(collegesSpecializedPage.getCurrent());
        pageModel.setSize(collegesSpecializedPage.getSize());
        pageModel.setTotal(collegesSpecializedPage.getTotal());
        pageModel.setRecords(list);
        pageModel.setPages(collegesSpecializedPage.getPages());
        return R.ok().data("pageModel",pageModel);
    }




    @ApiOperation("模拟报志愿-----按照院校进行  分页条件查看全部冲、稳、保院校")
    @PostMapping("colleges-mock/{current}/{limit}")
    public R collegesMock(
            @ApiParam(name = "mockQueryVO",value = "院校条件") @RequestBody MockCollegesQueryVO mockQueryVO,
            HttpServletRequest request
            ){
        String token = request.getHeader("token");
        String username = JwtTokenUtils.getUsername(token);
        String user = redisTemplate.opsForValue().get("user" + username);
        InfoVO infoVO = JSON.parseObject(user, InfoVO.class);
        String scores = infoVO.getScores();
        String bits = infoVO.getBits();
        if (scores==null||bits==null){
            return R.error().message("您尚未录入成绩等信息");
        }
        collegesSpecializedService.getCollegesMock(infoVO,mockQueryVO);
        return R.ok();
        //TODO
    }

    @ApiOperation("分页条件查看所有专业录取概率")
    @PostMapping("all-mock-list/{current}/{limit}")
    public R allMockList(
            @ApiParam(name = "current", value = "当前页", required = true) @PathVariable Long current,
            @ApiParam(name = "limit", value = "每页多少", required = true) @PathVariable Long limit,
            @ApiParam(name = "volunteerQueryVO",value = "查询条件")@RequestBody(required = false)VolunteerQueryVO volunteerQueryVO,
            HttpServletRequest request) {
        Page page = new Page<>(current, limit);
        String token = request.getHeader("token");
        String username = JwtTokenUtils.getUsername(token);
        String user = redisTemplate.opsForValue().get("user" + username);
        InfoVO infoVO = JSON.parseObject(user, InfoVO.class);
        String id = infoVO.getId();
        String scores = infoVO.getScores();
        String bits = infoVO.getBits();
        if (scores == null || scores.equals("")) {
            return R.error().message("您未设置成绩或位次信息，无法使用本功能");
        }
        if (infoVO.getBits() == null || infoVO.getBits().equals("")) {
            return R.error().message("您未设置成绩或位次信息，无法使用本功能");
        }
        if (volunteerQueryVO==null||volunteerQueryVO.equals("")){
            LambdaQueryWrapper<Colleges> wrapper = new LambdaQueryWrapper<>();
            List<Colleges> collegesList = collegesService.getBaseMapper().selectList(wrapper);
            List<String> collegesIdList = new ArrayList<>();
            for (int i = 0; i < collegesList.size(); i++) {
                collegesIdList.add(collegesList.get(i).getId());
            }
            LambdaQueryWrapper<CollegesSpecialized> queryWrapper = new LambdaQueryWrapper<>();
//            if (specializedName != null && !specializedName.equals("")) {
//                queryWrapper.like(CollegesSpecialized::getSpecialized, specializedName);
//            }

            if (collegesIdList != null && !collegesIdList.equals("")) {
//            for (int i = 0; i < collegesIdList.size(); i++) {
//                queryWrapper.or().eq(CollegesSpecialized::getCollegesId,collegesIdList.get(i));
//            }
                queryWrapper.in(CollegesSpecialized::getCollegesId, collegesIdList);
            }
            IPage<CollegesSpecialized> iPage = collegesSpecializedService.getBaseMapper().selectPage(page, queryWrapper);
            List<CollegesSpecialized> records = iPage.getRecords();
            IPage<MockProbabilityDTO> pageModel = new Page<>(current, limit);
            pageModel.setPages(iPage.getPages());
            pageModel.setTotal(iPage.getTotal());
            List<MockProbabilityDTO> list = new ArrayList<>();
            for (int i = 0; i < records.size(); i++) {
                MockProbabilityDTO mockProbabilityDTO = new MockProbabilityDTO();
                LambdaQueryWrapper<Colleges> collegesWrapper = new LambdaQueryWrapper<>();
                collegesWrapper.eq(Colleges::getId, records.get(i).getCollegesId());
                Colleges colleges = collegesService.getBaseMapper().selectOne(collegesWrapper);
                LambdaQueryWrapper<CollegesSpecializedUsual> usualWrapper = new LambdaQueryWrapper<>();
                usualWrapper.eq(CollegesSpecializedUsual::getSpecializedId, records.get(i).getId());
                CollegesSpecializedUsual collegesSpecializedUsual = collegesSpecializedUsualService.getBaseMapper().selectOne(usualWrapper);
                BeanUtils.copyProperties(colleges, mockProbabilityDTO);
                BeanUtils.copyProperties(collegesSpecializedUsual, mockProbabilityDTO);
                BeanUtils.copyProperties(records.get(i), mockProbabilityDTO);
                String probability = "";
                Double odds = 0.0;

//            if (mockProbabilityDTO==null&&mockProbabilityDTO.equals("")){
//                return R;
//            }
//            for (int i = 0; i < model.size(); i++) {
                if (mockProbabilityDTO.getLastYearAverageScore() != null) {
                    if (Integer.parseInt(scores) >= Integer.parseInt(mockProbabilityDTO.getLastYearAverageScore())) {
                        odds = odds + 0.2375;
                    }
                }
                if (mockProbabilityDTO.getLastYearAverageBit() != null) {
                    if (Integer.parseInt(bits) <= Integer.parseInt(mockProbabilityDTO.getLastYearAverageBit())) {
                        odds = odds + 0.2375;
                    }
                }
                if (mockProbabilityDTO.getTwoYearsAverageScore() != null) {
                    if (Integer.parseInt(scores) >= Integer.parseInt(mockProbabilityDTO.getTwoYearsAverageScore())) {
                        odds = odds + 0.2375;
                    }
                }
                if (mockProbabilityDTO.getTwoYearsAverageBit() != null) {
                    if (Integer.parseInt(bits) <= Integer.parseInt(mockProbabilityDTO.getTwoYearsAverageBit())) {
                        odds = odds + 0.2375;
                    }
                }
                if (odds >= 0.9) {
                    probability = "95%";
                } else if (odds >= 0.7) {
                    probability = "75%";
                } else if (odds >= 0.4) {
                    probability = "50%";
                } else if (odds >= 0.2) {
                    probability = "25%";
                } else {
                    probability = "1%";
                }
                mockProbabilityDTO.setProbability(probability);
                if (odds>=0.9){
                    mockProbabilityDTO.setStrategy("保");
                }else if (odds>=0.4){
                    mockProbabilityDTO.setStrategy("稳");
                }else {
                    mockProbabilityDTO.setStrategy("冲");
                }
                //查看是否已经收藏该院校   或 收藏位置
                LambdaQueryWrapper<Mock> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                lambdaQueryWrapper.eq(Mock::getUserId,id);
                lambdaQueryWrapper.eq(Mock::getMockId,mockProbabilityDTO.getId());
                Mock mock = mockService.getBaseMapper().selectOne(lambdaQueryWrapper);
                if (mock!=null&&!mock.equals("")){
                    mockProbabilityDTO.setCollection(1);
                    mockProbabilityDTO.setSort(mock.getSort());
                }else {
                    mockProbabilityDTO.setCollection(0);
                    mockProbabilityDTO.setSort(0);
                }

                list.add(mockProbabilityDTO);
//            odds = 0.0;
//            probability = "";

//        }
            }
            pageModel.setRecords(list);
            return R.ok().data("pageModel", pageModel);
        }else {
            String collegesName = volunteerQueryVO.getCollegesName();
            Integer type = volunteerQueryVO.getType();
            String kind = volunteerQueryVO.getKind();
            Integer category = volunteerQueryVO.getCategory();
            Integer worldClass = volunteerQueryVO.getWorldClass();
            Integer innovation = volunteerQueryVO.getInnovation();
            String province = volunteerQueryVO.getProvince();
            String city = volunteerQueryVO.getCity();
            Integer doubleFirstClass = volunteerQueryVO.getDoubleFirstClass();
            Integer strongFoundation = volunteerQueryVO.getStrongFoundation();
            String specializedName = volunteerQueryVO.getSpecializedName();
            LambdaQueryWrapper<Colleges> wrapper = new LambdaQueryWrapper<>();
            if (collegesName!=null&&!collegesName.equals("")){
                wrapper.like(Colleges::getName,collegesName);
            }
            if (type!=null&&!type.equals("")){
                wrapper.eq(Colleges::getType,type);
            }
            if (kind!=null&&!kind.equals("")){
                wrapper.eq(Colleges::getKind,kind);
            }
            if (category!=null&&!category.equals("")){
                wrapper.eq(Colleges::getCategory,category);
            }
            if (worldClass!=null&&!worldClass.equals("")){
                wrapper.eq(Colleges::getWorldClass,worldClass);
            }
            if (innovation!=null&&!innovation.equals("")){
                wrapper.eq(Colleges::getInnovation,innovation);
            }
            if (province!=null&&!province.equals("")){
                wrapper.eq(Colleges::getProvince,province);
            }
            if (city!=null&&!city.equals("")){
                wrapper.eq(Colleges::getCity,city);
            }
            if (doubleFirstClass!=null&&!doubleFirstClass.equals("")){
                wrapper.eq(Colleges::getDoubleFirstClass,doubleFirstClass);
            }
            if (strongFoundation!=null&&!strongFoundation.equals("")){
                wrapper.eq(Colleges::getStrongFoundation,strongFoundation);
            }

            LambdaQueryWrapper<CollegesSpecialized> queryWrapper = new LambdaQueryWrapper<>();
            if (specializedName!=null&&!specializedName.equals("")){
                queryWrapper.like(CollegesSpecialized::getSpecialized,specializedName);

            }
            if (type!=null&&!type.equals("")){
                queryWrapper.eq(CollegesSpecialized::getType,type);
            }
            List<Colleges> collegesList = collegesService.getBaseMapper().selectList(wrapper);
            List<String> collegesIdList = new ArrayList<>();
            for (int i = 0; i < collegesList.size(); i++) {
                collegesIdList.add(collegesList.get(i).getId());
            }

            if (collegesIdList.size()!=0) {
//            for (int i = 0; i < collegesIdList.size(); i++) {
//                queryWrapper.or().eq(CollegesSpecialized::getCollegesId,collegesIdList.get(i));
//            }
                queryWrapper.in(CollegesSpecialized::getCollegesId, collegesIdList);
            }
            IPage<CollegesSpecialized> iPage = collegesSpecializedService.getBaseMapper().selectPage(page, queryWrapper);
            List<CollegesSpecialized> records = iPage.getRecords();
            IPage<MockProbabilityDTO> pageModel = new Page<>(current, limit);
            if(records==null||records.equals("")){
                pageModel.setPages(1);
                pageModel.setTotal(0);
                return R.ok().data("pageModel",pageModel);
            }
            pageModel.setPages(iPage.getPages());
            pageModel.setTotal(iPage.getTotal());
            List<MockProbabilityDTO> list = new ArrayList<>();
            for (int i = 0; i < records.size(); i++) {
                MockProbabilityDTO mockProbabilityDTO = new MockProbabilityDTO();
                LambdaQueryWrapper<Colleges> collegesWrapper = new LambdaQueryWrapper<>();
                collegesWrapper.eq(Colleges::getId, records.get(i).getCollegesId());
                Colleges colleges = collegesService.getBaseMapper().selectOne(collegesWrapper);
                LambdaQueryWrapper<CollegesSpecializedUsual> usualWrapper = new LambdaQueryWrapper<>();
                usualWrapper.eq(CollegesSpecializedUsual::getSpecializedId, records.get(i).getId());
                CollegesSpecializedUsual collegesSpecializedUsual = collegesSpecializedUsualService.getBaseMapper().selectOne(usualWrapper);
                BeanUtils.copyProperties(colleges, mockProbabilityDTO);
                BeanUtils.copyProperties(collegesSpecializedUsual, mockProbabilityDTO);
                BeanUtils.copyProperties(records.get(i), mockProbabilityDTO);
                String probability = "";
                Double odds = 0.0;

//            if (mockProbabilityDTO==null&&mockProbabilityDTO.equals("")){
//                return R;
//            }
//            for (int i = 0; i < model.size(); i++) {
                if (mockProbabilityDTO.getLastYearAverageScore() != null) {
                    if (Integer.parseInt(scores) >= Integer.parseInt(mockProbabilityDTO.getLastYearAverageScore())) {
                        odds = odds + 0.2375;
                    }
                }
                if (mockProbabilityDTO.getLastYearAverageBit() != null) {
                    if (Integer.parseInt(bits) <= Integer.parseInt(mockProbabilityDTO.getLastYearAverageBit())) {
                        odds = odds + 0.2375;
                    }
                }
                if (mockProbabilityDTO.getTwoYearsAverageScore() != null) {
                    if (Integer.parseInt(scores) >= Integer.parseInt(mockProbabilityDTO.getTwoYearsAverageScore())) {
                        odds = odds + 0.2375;
                    }
                }
                if (mockProbabilityDTO.getTwoYearsAverageBit() != null) {
                    if (Integer.parseInt(bits) <= Integer.parseInt(mockProbabilityDTO.getTwoYearsAverageBit())) {
                        odds = odds + 0.2375;
                    }
                }
                if (odds >= 0.9) {
                    probability = "95%";
                } else if (odds >= 0.7) {
                    probability = "75%";
                } else if (odds >= 0.4) {
                    probability = "50%";
                } else if (odds >= 0.2) {
                    probability = "25%";
                } else {
                    probability = "1%";
                }
                if (odds>=0.9){
                    mockProbabilityDTO.setStrategy("保");
                }else if (odds>=0.4){
                    mockProbabilityDTO.setStrategy("稳");
                }else {
                    mockProbabilityDTO.setStrategy("冲");
                }
                mockProbabilityDTO.setProbability(probability);

                //查看是否已经收藏该院校   或 收藏位置
                LambdaQueryWrapper<Mock> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                lambdaQueryWrapper.eq(Mock::getUserId,id);
                lambdaQueryWrapper.eq(Mock::getMockId,mockProbabilityDTO.getId());
                Mock mock = mockService.getBaseMapper().selectOne(lambdaQueryWrapper);
                if (mock!=null&&!mock.equals("")){
                    mockProbabilityDTO.setCollection(1);
                    mockProbabilityDTO.setSort(mock.getSort());
                }else {
                    mockProbabilityDTO.setCollection(0);
                    mockProbabilityDTO.setSort(0);
                }
                list.add(mockProbabilityDTO);
//            odds = 0.0;
//            probability = "";
//        }
            }
            pageModel.setRecords(list);
            return R.ok().data("pageModel", pageModel);
        }
    }




    @ApiOperation("分页条件查看所有专业录取概率")
    @PostMapping("all-mock-strategy-list/{current}/{limit}")
    public R allStrategyMockList(
            @ApiParam(name = "current", value = "当前页", required = true) @PathVariable Long current,
            @ApiParam(name = "limit", value = "每页多少", required = true) @PathVariable Long limit,
            @ApiParam(name = "volunteerQueryVO",value = "查询条件")@RequestBody(required = false)VolunteerQueryVO volunteerQueryVO,
            HttpServletRequest request) {
        Page page = new Page<>(current, limit);
        String token = request.getHeader("token");
        String username = JwtTokenUtils.getUsername(token);
        String user = redisTemplate.opsForValue().get("user" + username);
        InfoVO infoVO = JSON.parseObject(user, InfoVO.class);
        String id = infoVO.getId();
        String scores = infoVO.getScores();
        String bits = infoVO.getBits();
        if (scores == null || scores.equals("")) {
            return R.error().message("您未设置成绩或位次信息，无法使用本功能");
        }
        if (infoVO.getBits() == null || infoVO.getBits().equals("")) {
            return R.error().message("您未设置成绩或位次信息，无法使用本功能");
        }
        if (volunteerQueryVO==null||volunteerQueryVO.equals("")){
            LambdaQueryWrapper<Colleges> wrapper = new LambdaQueryWrapper<>();
            List<Colleges> collegesList = collegesService.getBaseMapper().selectList(wrapper);
            List<String> collegesIdList = new ArrayList<>();
            for (int i = 0; i < collegesList.size(); i++) {
                collegesIdList.add(collegesList.get(i).getId());
            }
            LambdaQueryWrapper<CollegesSpecialized> queryWrapper = new LambdaQueryWrapper<>();
//            if (specializedName != null && !specializedName.equals("")) {
//                queryWrapper.like(CollegesSpecialized::getSpecialized, specializedName);
//            }

            if (collegesIdList != null && !collegesIdList.equals("")) {
//            for (int i = 0; i < collegesIdList.size(); i++) {
//                queryWrapper.or().eq(CollegesSpecialized::getCollegesId,collegesIdList.get(i));
//            }
                queryWrapper.in(CollegesSpecialized::getCollegesId, collegesIdList);
            }
            IPage<CollegesSpecialized> iPage = collegesSpecializedService.getBaseMapper().selectPage(page, queryWrapper);
            List<CollegesSpecialized> records = iPage.getRecords();
            IPage<MockProbabilityDTO> pageModel = new Page<>(current, limit);
            pageModel.setPages(iPage.getPages());
            pageModel.setTotal(iPage.getTotal());
            List<MockProbabilityDTO> list = new ArrayList<>();
            for (int i = 0; i < records.size(); i++) {
                MockProbabilityDTO mockProbabilityDTO = new MockProbabilityDTO();
                LambdaQueryWrapper<Colleges> collegesWrapper = new LambdaQueryWrapper<>();
                collegesWrapper.eq(Colleges::getId, records.get(i).getCollegesId());
                Colleges colleges = collegesService.getBaseMapper().selectOne(collegesWrapper);
                LambdaQueryWrapper<CollegesSpecializedUsual> usualWrapper = new LambdaQueryWrapper<>();
                usualWrapper.eq(CollegesSpecializedUsual::getSpecializedId, records.get(i).getId());
                CollegesSpecializedUsual collegesSpecializedUsual = collegesSpecializedUsualService.getBaseMapper().selectOne(usualWrapper);
                BeanUtils.copyProperties(colleges, mockProbabilityDTO);
                BeanUtils.copyProperties(collegesSpecializedUsual, mockProbabilityDTO);
                BeanUtils.copyProperties(records.get(i), mockProbabilityDTO);
                String probability = "";
                Double odds = 0.0;

//            if (mockProbabilityDTO==null&&mockProbabilityDTO.equals("")){
//                return R;
//            }
//            for (int i = 0; i < model.size(); i++) {
                if (mockProbabilityDTO.getLastYearAverageScore() != null) {
                    if (Integer.parseInt(scores) >= Integer.parseInt(mockProbabilityDTO.getLastYearAverageScore())) {
                        odds = odds + 0.2375;
                    }
                }
                if (mockProbabilityDTO.getLastYearAverageBit() != null) {
                    if (Integer.parseInt(bits) <= Integer.parseInt(mockProbabilityDTO.getLastYearAverageBit())) {
                        odds = odds + 0.2375;
                    }
                }
                if (mockProbabilityDTO.getTwoYearsAverageScore() != null) {
                    if (Integer.parseInt(scores) >= Integer.parseInt(mockProbabilityDTO.getTwoYearsAverageScore())) {
                        odds = odds + 0.2375;
                    }
                }
                if (mockProbabilityDTO.getTwoYearsAverageBit() != null) {
                    if (Integer.parseInt(bits) <= Integer.parseInt(mockProbabilityDTO.getTwoYearsAverageBit())) {
                        odds = odds + 0.2375;
                    }
                }
                if (odds >= 0.9) {
                    probability = "95%";
                } else if (odds >= 0.7) {
                    probability = "75%";
                } else if (odds >= 0.4) {
                    probability = "50%";
                } else if (odds >= 0.2) {
                    probability = "25%";
                } else {
                    probability = "1%";
                }
                mockProbabilityDTO.setProbability(probability);
                if (odds>=0.9){
                    mockProbabilityDTO.setStrategy("保");
                }else if (odds>=0.4){
                    mockProbabilityDTO.setStrategy("稳");
                }else {
                    mockProbabilityDTO.setStrategy("冲");
                }
                //查看是否已经收藏该院校   或 收藏位置
                LambdaQueryWrapper<Mock> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                lambdaQueryWrapper.eq(Mock::getUserId,id);
                lambdaQueryWrapper.eq(Mock::getMockId,mockProbabilityDTO.getId());
                Mock mock = mockService.getBaseMapper().selectOne(lambdaQueryWrapper);
                if (mock!=null&&!mock.equals("")){
                    mockProbabilityDTO.setCollection(1);
                    mockProbabilityDTO.setSort(mock.getSort());
                }else {
                    mockProbabilityDTO.setCollection(0);
                    mockProbabilityDTO.setSort(0);
                }
                list.add(mockProbabilityDTO);
            }
            pageModel.setRecords(list);
            return R.ok().data("pageModel", pageModel);
        }{
            IPage<MockProbabilityDTO> pageModel = new Page<>();
            pageModel.setCurrent(current);
            pageModel.setSize(limit);
            List<MockProbabilityDTO> list = new ArrayList<>();
            Integer strategy = volunteerQueryVO.getStrategy();
            if (strategy.equals(0)){
                LambdaQueryWrapper<CollegesSpecializedUsual> wrapper = new LambdaQueryWrapper<>();
                wrapper.le(CollegesSpecializedUsual::getLastYearAverageBit,bits);
                wrapper.ge(CollegesSpecializedUsual::getLastYearAverageScore,scores);
                IPage<CollegesSpecializedUsual> selectPage = collegesSpecializedUsualService.getBaseMapper().selectPage(page, wrapper);
                pageModel.setPages(selectPage.getPages());
                pageModel.setTotal(selectPage.getTotal());
                List<CollegesSpecializedUsual> records = selectPage.getRecords();
                for (int i = 0; i < records.size(); i++) {
                    MockProbabilityDTO mockProbabilityDTO = new MockProbabilityDTO();
                    String specializeId = records.get(i).getId();
                    CollegesSpecialized collegesSpecialized = collegesSpecializedService.getBaseMapper().selectById(specializeId);
                    String collegesId = collegesSpecialized.getCollegesId();
                    Colleges colleges = collegesService.getBaseMapper().selectById(collegesId);
                    BeanUtils.copyProperties(records.get(i),mockProbabilityDTO);
                    BeanUtils.copyProperties(colleges,mockProbabilityDTO);
                    BeanUtils.copyProperties(collegesSpecialized,mockProbabilityDTO);
                    LambdaQueryWrapper<Mock> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                    lambdaQueryWrapper.eq(Mock::getMockId,mockProbabilityDTO.getId());
                    lambdaQueryWrapper.eq(Mock::getUserId,id);
                    Mock mock = mockService.getBaseMapper().selectOne(lambdaQueryWrapper);
                    if (mock!=null&&!mock.equals("")){
                        mockProbabilityDTO.setCollection(1);
                        mockProbabilityDTO.setSort(mock.getSort());
                    }else {
                        mockProbabilityDTO.setCollection(0);
                        mockProbabilityDTO.setSort(0);
                    }
                    mockProbabilityDTO.setStrategy("冲");
                    mockProbabilityDTO.setProbability("1%-25%");
                    list.add(mockProbabilityDTO);
                }
            }else if (strategy.equals(1)){
                LambdaQueryWrapper<CollegesSpecializedUsual> wrapper = new LambdaQueryWrapper<>();
                wrapper.ge(CollegesSpecializedUsual::getLastYearAverageBit,bits);
                wrapper.le(CollegesSpecializedUsual::getTwoYearsAverageBit,bits);
                IPage<CollegesSpecializedUsual> selectPage = collegesSpecializedUsualService.getBaseMapper().selectPage(page, wrapper);
                pageModel.setPages(selectPage.getPages());
                pageModel.setTotal(selectPage.getTotal());
                List<CollegesSpecializedUsual> records = selectPage.getRecords();
                for (int i = 0; i < records.size(); i++) {
                    MockProbabilityDTO mockProbabilityDTO = new MockProbabilityDTO();
                    String specializeId = records.get(i).getId();
                    CollegesSpecialized collegesSpecialized = collegesSpecializedService.getBaseMapper().selectById(specializeId);
                    String collegesId = collegesSpecialized.getCollegesId();
                    Colleges colleges = collegesService.getBaseMapper().selectById(collegesId);
                    BeanUtils.copyProperties(records.get(i),mockProbabilityDTO);
                    BeanUtils.copyProperties(colleges,mockProbabilityDTO);
                    BeanUtils.copyProperties(collegesSpecialized,mockProbabilityDTO);
                    LambdaQueryWrapper<Mock> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                    lambdaQueryWrapper.eq(Mock::getMockId,mockProbabilityDTO.getId());
                    lambdaQueryWrapper.eq(Mock::getUserId,id);
                    Mock mock = mockService.getBaseMapper().selectOne(lambdaQueryWrapper);
                    if (mock!=null&&!mock.equals("")){
                        mockProbabilityDTO.setCollection(1);
                        mockProbabilityDTO.setSort(mock.getSort());
                    }else {
                        mockProbabilityDTO.setCollection(0);
                        mockProbabilityDTO.setSort(0);
                    }
                    mockProbabilityDTO.setStrategy("稳");
                    mockProbabilityDTO.setProbability("30%-60%");
                    list.add(mockProbabilityDTO);
                }
            }else {
                LambdaQueryWrapper<CollegesSpecializedUsual> wrapper = new LambdaQueryWrapper<>();
                wrapper.ge(CollegesSpecializedUsual::getLastYearAverageBit,bits);
                wrapper.ge(CollegesSpecializedUsual::getTwoYearsAverageBit,bits);
                IPage<CollegesSpecializedUsual> selectPage = collegesSpecializedUsualService.getBaseMapper().selectPage(page, wrapper);
                pageModel.setPages(selectPage.getPages());
                pageModel.setTotal(selectPage.getTotal());
                List<CollegesSpecializedUsual> records = selectPage.getRecords();
                for (int i = 0; i < records.size(); i++) {
                    MockProbabilityDTO mockProbabilityDTO = new MockProbabilityDTO();
                    String specializeId = records.get(i).getId();
                    CollegesSpecialized collegesSpecialized = collegesSpecializedService.getBaseMapper().selectById(specializeId);
                    String collegesId = collegesSpecialized.getCollegesId();
                    Colleges colleges = collegesService.getBaseMapper().selectById(collegesId);
                    BeanUtils.copyProperties(records.get(i),mockProbabilityDTO);
                    BeanUtils.copyProperties(colleges,mockProbabilityDTO);
                    BeanUtils.copyProperties(collegesSpecialized,mockProbabilityDTO);
                    LambdaQueryWrapper<Mock> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                    lambdaQueryWrapper.eq(Mock::getMockId,mockProbabilityDTO.getId());
                    lambdaQueryWrapper.eq(Mock::getUserId,id);
                    Mock mock = mockService.getBaseMapper().selectOne(lambdaQueryWrapper);
                    if (mock!=null&&!mock.equals("")){
                        mockProbabilityDTO.setCollection(1);
                        mockProbabilityDTO.setSort(mock.getSort());
                    }else {
                        mockProbabilityDTO.setCollection(0);
                        mockProbabilityDTO.setSort(0);
                    }
                    mockProbabilityDTO.setStrategy("保");
                    mockProbabilityDTO.setProbability("70%-80%");
                    list.add(mockProbabilityDTO);
                }
            }

            pageModel.setRecords(list);
            return R.ok().data("pageModel", pageModel);
        }
    }



//    @ApiOperation("分页条件查看所有专业录取概率")
//    @PostMapping("all-mock-list/{current}/{limit}")
//    public R allMockList(
//            @ApiParam(name = "current", value = "当前页", required = true) @PathVariable Long current,
//            @ApiParam(name = "limit", value = "每页多少", required = true) @PathVariable Long limit,
//            @ApiParam(name = "volunteerQueryVO",value = "查询条件")@RequestBody(required = false)VolunteerQueryVO volunteerQueryVO,
//            HttpServletRequest request) {
//        Page page = new Page<>(current, limit);
//        String token = request.getHeader("token");
//        String username = JwtTokenUtils.getUsername(token);
//        String user = redisTemplate.opsForValue().get("user" + username);
//        InfoVO infoVO = JSON.parseObject(user, InfoVO.class);
//        String id = infoVO.getId();
//        String scores = infoVO.getScores();
//        String bits = infoVO.getBits();
//        if (scores == null || scores.equals("")) {
//            return R.error().message("您未设置成绩或位次信息，无法使用本功能");
//        }
//        if (infoVO.getBits() == null || infoVO.getBits().equals("")) {
//            return R.error().message("您未设置成绩或位次信息，无法使用本功能");
//        }
//        if (volunteerQueryVO==null||volunteerQueryVO.equals("")){
//            LambdaQueryWrapper<Colleges> wrapper = new LambdaQueryWrapper<>();
//            List<Colleges> collegesList = collegesService.getBaseMapper().selectList(wrapper);
//            List<String> collegesIdList = new ArrayList<>();
//            for (int i = 0; i < collegesList.size(); i++) {
//                collegesIdList.add(collegesList.get(i).getId());
//            }
//            LambdaQueryWrapper<CollegesSpecialized> queryWrapper = new LambdaQueryWrapper<>();
////            if (specializedName != null && !specializedName.equals("")) {
////                queryWrapper.like(CollegesSpecialized::getSpecialized, specializedName);
////            }
//
//            if (collegesIdList != null && !collegesIdList.equals("")) {
////            for (int i = 0; i < collegesIdList.size(); i++) {
////                queryWrapper.or().eq(CollegesSpecialized::getCollegesId,collegesIdList.get(i));
////            }
//                queryWrapper.in(CollegesSpecialized::getCollegesId, collegesIdList);
//            }
//            IPage<CollegesSpecialized> iPage = collegesSpecializedService.getBaseMapper().selectPage(page, queryWrapper);
//            List<CollegesSpecialized> records = iPage.getRecords();
//            IPage<MockProbabilityDTO> pageModel = new Page<>(current, limit);
//            pageModel.setPages(iPage.getPages());
//            pageModel.setTotal(iPage.getTotal());
//            List<MockProbabilityDTO> list = new ArrayList<>();
//            for (int i = 0; i < records.size(); i++) {
//                MockProbabilityDTO mockProbabilityDTO = new MockProbabilityDTO();
//                LambdaQueryWrapper<Colleges> collegesWrapper = new LambdaQueryWrapper<>();
//                collegesWrapper.eq(Colleges::getId, records.get(i).getCollegesId());
//                Colleges colleges = collegesService.getBaseMapper().selectOne(collegesWrapper);
//                LambdaQueryWrapper<CollegesSpecializedUsual> usualWrapper = new LambdaQueryWrapper<>();
//                usualWrapper.eq(CollegesSpecializedUsual::getSpecializedId, records.get(i).getId());
//                CollegesSpecializedUsual collegesSpecializedUsual = collegesSpecializedUsualService.getBaseMapper().selectOne(usualWrapper);
//                BeanUtils.copyProperties(colleges, mockProbabilityDTO);
//                BeanUtils.copyProperties(collegesSpecializedUsual, mockProbabilityDTO);
//                BeanUtils.copyProperties(records.get(i), mockProbabilityDTO);
//                String probability = "";
//                Double odds = 0.0;
//
////            if (mockProbabilityDTO==null&&mockProbabilityDTO.equals("")){
////                return R;
////            }
////            for (int i = 0; i < model.size(); i++) {
//                if (mockProbabilityDTO.getLastYearAverageScore() != null) {
//                    if (Integer.parseInt(scores) >= Integer.parseInt(mockProbabilityDTO.getLastYearAverageScore())) {
//                        odds = odds + 0.2375;
//                    }
//                }
//                if (mockProbabilityDTO.getLastYearAverageBit() != null) {
//                    if (Integer.parseInt(bits) <= Integer.parseInt(mockProbabilityDTO.getLastYearAverageBit())) {
//                        odds = odds + 0.2375;
//                    }
//                }
//                if (mockProbabilityDTO.getTwoYearsAverageScore() != null) {
//                    if (Integer.parseInt(scores) >= Integer.parseInt(mockProbabilityDTO.getTwoYearsAverageScore())) {
//                        odds = odds + 0.2375;
//                    }
//                }
//                if (mockProbabilityDTO.getTwoYearsAverageBit() != null) {
//                    if (Integer.parseInt(bits) <= Integer.parseInt(mockProbabilityDTO.getTwoYearsAverageBit())) {
//                        odds = odds + 0.2375;
//                    }
//                }
//                if (odds >= 0.9) {
//                    probability = "95%";
//                } else if (odds >= 0.7) {
//                    probability = "75%";
//                } else if (odds >= 0.4) {
//                    probability = "50%";
//                } else if (odds >= 0.2) {
//                    probability = "25%";
//                } else {
//                    probability = "1%";
//                }
//                mockProbabilityDTO.setProbability(probability);
//                if (odds>=0.9){
//                    mockProbabilityDTO.setStrategy("保");
//                }else if (odds>=0.4){
//                    mockProbabilityDTO.setStrategy("稳");
//                }else {
//                    mockProbabilityDTO.setStrategy("冲");
//                }
//                //查看是否已经收藏该院校   或 收藏位置
//                LambdaQueryWrapper<Mock> lambdaQueryWrapper = new LambdaQueryWrapper<>();
//                lambdaQueryWrapper.eq(Mock::getUserId,id);
//                lambdaQueryWrapper.eq(Mock::getMockId,mockProbabilityDTO.getId());
//                Mock mock = mockService.getBaseMapper().selectOne(lambdaQueryWrapper);
//                if (mock!=null&&!mock.equals("")){
//                    mockProbabilityDTO.setCollection(1);
//                    mockProbabilityDTO.setSort(mock.getSort());
//                }else {
//                    mockProbabilityDTO.setCollection(0);
//                    mockProbabilityDTO.setSort(0);
//                }
//
//                list.add(mockProbabilityDTO);
////            odds = 0.0;
////            probability = "";
//
////        }
//            }
//            pageModel.setRecords(list);
//            return R.ok().data("pageModel", pageModel);
//        }else {
//            String collegesName = volunteerQueryVO.getCollegesName();
//            Integer type = volunteerQueryVO.getType();
//            String kind = volunteerQueryVO.getKind();
//            Integer category = volunteerQueryVO.getCategory();
//            Integer worldClass = volunteerQueryVO.getWorldClass();
//            Integer innovation = volunteerQueryVO.getInnovation();
//            String province = volunteerQueryVO.getProvince();
//            String city = volunteerQueryVO.getCity();
//            Integer doubleFirstClass = volunteerQueryVO.getDoubleFirstClass();
//            Integer strongFoundation = volunteerQueryVO.getStrongFoundation();
//            String specializedName = volunteerQueryVO.getSpecializedName();
//            LambdaQueryWrapper<Colleges> wrapper = new LambdaQueryWrapper<>();
//            if (collegesName!=null&&!collegesName.equals("")){
//                wrapper.like(Colleges::getName,collegesName);
//            }
//            if (type!=null&&!type.equals("")){
//                wrapper.eq(Colleges::getType,type);
//            }
//            if (kind!=null&&!kind.equals("")){
//                wrapper.eq(Colleges::getKind,kind);
//            }
//            if (category!=null&&!category.equals("")){
//                wrapper.eq(Colleges::getCategory,category);
//            }
//            if (worldClass!=null&&!worldClass.equals("")){
//                wrapper.eq(Colleges::getWorldClass,worldClass);
//            }
//            if (innovation!=null&&!innovation.equals("")){
//                wrapper.eq(Colleges::getInnovation,innovation);
//            }
//            if (province!=null&&!province.equals("")){
//                wrapper.eq(Colleges::getProvince,province);
//            }
//            if (city!=null&&!city.equals("")){
//                wrapper.eq(Colleges::getCity,city);
//            }
//            if (doubleFirstClass!=null&&!doubleFirstClass.equals("")){
//                wrapper.eq(Colleges::getDoubleFirstClass,doubleFirstClass);
//            }
//            if (strongFoundation!=null&&!strongFoundation.equals("")){
//                wrapper.eq(Colleges::getStrongFoundation,strongFoundation);
//            }
//
//            LambdaQueryWrapper<CollegesSpecialized> queryWrapper = new LambdaQueryWrapper<>();
//            if (specializedName!=null&&!specializedName.equals("")){
//                queryWrapper.like(CollegesSpecialized::getSpecialized,specializedName);
//            }
//
//            List<Colleges> collegesList = collegesService.getBaseMapper().selectList(wrapper);
//            List<String> collegesIdList = new ArrayList<>();
//            for (int i = 0; i < collegesList.size(); i++) {
//                collegesIdList.add(collegesList.get(i).getId());
//            }
//
//            if (collegesIdList.size()!=0) {
////            for (int i = 0; i < collegesIdList.size(); i++) {
////                queryWrapper.or().eq(CollegesSpecialized::getCollegesId,collegesIdList.get(i));
////            }
//                queryWrapper.in(CollegesSpecialized::getCollegesId, collegesIdList);
//            }
//            IPage<CollegesSpecialized> iPage = collegesSpecializedService.getBaseMapper().selectPage(page, queryWrapper);
//            List<CollegesSpecialized> records = iPage.getRecords();
//            IPage<MockProbabilityDTO> pageModel = new Page<>(current, limit);
//            if(records==null||records.equals("")){
//                pageModel.setPages(1);
//                pageModel.setTotal(0);
//                return R.ok().data("pageModel",pageModel);
//            }
//            pageModel.setPages(iPage.getPages());
//            pageModel.setTotal(iPage.getTotal());
//            List<MockProbabilityDTO> list = new ArrayList<>();
//            for (int i = 0; i < records.size(); i++) {
//                MockProbabilityDTO mockProbabilityDTO = new MockProbabilityDTO();
//                LambdaQueryWrapper<Colleges> collegesWrapper = new LambdaQueryWrapper<>();
//                collegesWrapper.eq(Colleges::getId, records.get(i).getCollegesId());
//                Colleges colleges = collegesService.getBaseMapper().selectOne(collegesWrapper);
//                LambdaQueryWrapper<CollegesSpecializedUsual> usualWrapper = new LambdaQueryWrapper<>();
//                usualWrapper.eq(CollegesSpecializedUsual::getSpecializedId, records.get(i).getId());
//                CollegesSpecializedUsual collegesSpecializedUsual = collegesSpecializedUsualService.getBaseMapper().selectOne(usualWrapper);
//                BeanUtils.copyProperties(colleges, mockProbabilityDTO);
//                BeanUtils.copyProperties(collegesSpecializedUsual, mockProbabilityDTO);
//                BeanUtils.copyProperties(records.get(i), mockProbabilityDTO);
//                String probability = "";
//                Double odds = 0.0;
//
////            if (mockProbabilityDTO==null&&mockProbabilityDTO.equals("")){
////                return R;
////            }
////            for (int i = 0; i < model.size(); i++) {
//                if (mockProbabilityDTO.getLastYearAverageScore() != null) {
//                    if (Integer.parseInt(scores) >= Integer.parseInt(mockProbabilityDTO.getLastYearAverageScore())) {
//                        odds = odds + 0.2375;
//                    }
//                }
//                if (mockProbabilityDTO.getLastYearAverageBit() != null) {
//                    if (Integer.parseInt(bits) <= Integer.parseInt(mockProbabilityDTO.getLastYearAverageBit())) {
//                        odds = odds + 0.2375;
//                    }
//                }
//                if (mockProbabilityDTO.getTwoYearsAverageScore() != null) {
//                    if (Integer.parseInt(scores) >= Integer.parseInt(mockProbabilityDTO.getTwoYearsAverageScore())) {
//                        odds = odds + 0.2375;
//                    }
//                }
//                if (mockProbabilityDTO.getTwoYearsAverageBit() != null) {
//                    if (Integer.parseInt(bits) <= Integer.parseInt(mockProbabilityDTO.getTwoYearsAverageBit())) {
//                        odds = odds + 0.2375;
//                    }
//                }
//                if (odds >= 0.9) {
//                    probability = "95%";
//                } else if (odds >= 0.7) {
//                    probability = "75%";
//                } else if (odds >= 0.4) {
//                    probability = "50%";
//                } else if (odds >= 0.2) {
//                    probability = "25%";
//                } else {
//                    probability = "1%";
//                }
//                if (odds>=0.9){
//                    mockProbabilityDTO.setStrategy("保");
//                }else if (odds>=0.4){
//                    mockProbabilityDTO.setStrategy("稳");
//                }else {
//                    mockProbabilityDTO.setStrategy("冲");
//                }
//                mockProbabilityDTO.setProbability(probability);
//
//                //查看是否已经收藏该院校   或 收藏位置
//                LambdaQueryWrapper<Mock> lambdaQueryWrapper = new LambdaQueryWrapper<>();
//                lambdaQueryWrapper.eq(Mock::getUserId,id);
//                lambdaQueryWrapper.eq(Mock::getMockId,mockProbabilityDTO.getId());
//                Mock mock = mockService.getBaseMapper().selectOne(lambdaQueryWrapper);
//                if (mock!=null&&!mock.equals("")){
//                    mockProbabilityDTO.setCollection(1);
//                    mockProbabilityDTO.setSort(mock.getSort());
//                }else {
//                    mockProbabilityDTO.setCollection(0);
//                    mockProbabilityDTO.setSort(0);
//                }
//                list.add(mockProbabilityDTO);
////            odds = 0.0;
////            probability = "";
////        }
//            }
//            pageModel.setRecords(list);
//            return R.ok().data("pageModel", pageModel);
//        }
//    }
//
//



//    @ApiOperation("分页条件查看所有专业录取概率")
//    @PostMapping("all-mock-strategy-list/{current}/{limit}")
//    public R allStrategyMockList(
//            @ApiParam(name = "current", value = "当前页", required = true) @PathVariable Long current,
//            @ApiParam(name = "limit", value = "每页多少", required = true) @PathVariable Long limit,
//            @ApiParam(name = "volunteerQueryVO",value = "查询条件")@RequestBody(required = false)VolunteerQueryVO volunteerQueryVO,
//            HttpServletRequest request) {
//        Page page = new Page<>(current, limit);
//        String token = request.getHeader("token");
//        String username = JwtTokenUtils.getUsername(token);
//        String user = redisTemplate.opsForValue().get("user" + username);
//        InfoVO infoVO = JSON.parseObject(user, InfoVO.class);
//        String id = infoVO.getId();
//        String scores = infoVO.getScores();
//        String bits = infoVO.getBits();
//        if (scores == null || scores.equals("")) {
//            return R.error().message("您未设置成绩或位次信息，无法使用本功能");
//        }
//        if (infoVO.getBits() == null || infoVO.getBits().equals("")) {
//            return R.error().message("您未设置成绩或位次信息，无法使用本功能");
//        }
//        if (volunteerQueryVO==null||volunteerQueryVO.equals("")){
//            LambdaQueryWrapper<Colleges> wrapper = new LambdaQueryWrapper<>();
//            List<Colleges> collegesList = collegesService.getBaseMapper().selectList(wrapper);
//            List<String> collegesIdList = new ArrayList<>();
//            for (int i = 0; i < collegesList.size(); i++) {
//                collegesIdList.add(collegesList.get(i).getId());
//            }
//            LambdaQueryWrapper<CollegesSpecialized> queryWrapper = new LambdaQueryWrapper<>();
////            if (specializedName != null && !specializedName.equals("")) {
////                queryWrapper.like(CollegesSpecialized::getSpecialized, specializedName);
////            }
//
//            if (collegesIdList != null && !collegesIdList.equals("")) {
////            for (int i = 0; i < collegesIdList.size(); i++) {
////                queryWrapper.or().eq(CollegesSpecialized::getCollegesId,collegesIdList.get(i));
////            }
//                queryWrapper.in(CollegesSpecialized::getCollegesId, collegesIdList);
//            }
//            IPage<CollegesSpecialized> iPage = collegesSpecializedService.getBaseMapper().selectPage(page, queryWrapper);
//            List<CollegesSpecialized> records = iPage.getRecords();
//            IPage<MockProbabilityDTO> pageModel = new Page<>(current, limit);
//            pageModel.setPages(iPage.getPages());
//            pageModel.setTotal(iPage.getTotal());
//            List<MockProbabilityDTO> list = new ArrayList<>();
//            for (int i = 0; i < records.size(); i++) {
//                MockProbabilityDTO mockProbabilityDTO = new MockProbabilityDTO();
//                LambdaQueryWrapper<Colleges> collegesWrapper = new LambdaQueryWrapper<>();
//                collegesWrapper.eq(Colleges::getId, records.get(i).getCollegesId());
//                Colleges colleges = collegesService.getBaseMapper().selectOne(collegesWrapper);
//                LambdaQueryWrapper<CollegesSpecializedUsual> usualWrapper = new LambdaQueryWrapper<>();
//                usualWrapper.eq(CollegesSpecializedUsual::getSpecializedId, records.get(i).getId());
//                CollegesSpecializedUsual collegesSpecializedUsual = collegesSpecializedUsualService.getBaseMapper().selectOne(usualWrapper);
//                BeanUtils.copyProperties(colleges, mockProbabilityDTO);
//                BeanUtils.copyProperties(collegesSpecializedUsual, mockProbabilityDTO);
//                BeanUtils.copyProperties(records.get(i), mockProbabilityDTO);
//                String probability = "";
//                Double odds = 0.0;
//
////            if (mockProbabilityDTO==null&&mockProbabilityDTO.equals("")){
////                return R;
////            }
////            for (int i = 0; i < model.size(); i++) {
//                if (mockProbabilityDTO.getLastYearAverageScore() != null) {
//                    if (Integer.parseInt(scores) >= Integer.parseInt(mockProbabilityDTO.getLastYearAverageScore())) {
//                        odds = odds + 0.2375;
//                    }
//                }
//                if (mockProbabilityDTO.getLastYearAverageBit() != null) {
//                    if (Integer.parseInt(bits) <= Integer.parseInt(mockProbabilityDTO.getLastYearAverageBit())) {
//                        odds = odds + 0.2375;
//                    }
//                }
//                if (mockProbabilityDTO.getTwoYearsAverageScore() != null) {
//                    if (Integer.parseInt(scores) >= Integer.parseInt(mockProbabilityDTO.getTwoYearsAverageScore())) {
//                        odds = odds + 0.2375;
//                    }
//                }
//                if (mockProbabilityDTO.getTwoYearsAverageBit() != null) {
//                    if (Integer.parseInt(bits) <= Integer.parseInt(mockProbabilityDTO.getTwoYearsAverageBit())) {
//                        odds = odds + 0.2375;
//                    }
//                }
//                if (odds >= 0.9) {
//                    probability = "95%";
//                } else if (odds >= 0.7) {
//                    probability = "75%";
//                } else if (odds >= 0.4) {
//                    probability = "50%";
//                } else if (odds >= 0.2) {
//                    probability = "25%";
//                } else {
//                    probability = "1%";
//                }
//                mockProbabilityDTO.setProbability(probability);
//                if (odds>=0.9){
//                    mockProbabilityDTO.setStrategy("保");
//                }else if (odds>=0.4){
//                    mockProbabilityDTO.setStrategy("稳");
//                }else {
//                    mockProbabilityDTO.setStrategy("冲");
//                }
//                //查看是否已经收藏该院校   或 收藏位置
//                LambdaQueryWrapper<Mock> lambdaQueryWrapper = new LambdaQueryWrapper<>();
//                lambdaQueryWrapper.eq(Mock::getUserId,id);
//                lambdaQueryWrapper.eq(Mock::getMockId,mockProbabilityDTO.getId());
//                Mock mock = mockService.getBaseMapper().selectOne(lambdaQueryWrapper);
//                if (mock!=null&&!mock.equals("")){
//                    mockProbabilityDTO.setCollection(1);
//                    mockProbabilityDTO.setSort(mock.getSort());
//                }else {
//                    mockProbabilityDTO.setCollection(0);
//                    mockProbabilityDTO.setSort(0);
//                }
//                list.add(mockProbabilityDTO);
//            }
//            pageModel.setRecords(list);
//            return R.ok().data("pageModel", pageModel);
//        }else {
//                Integer strategy = volunteerQueryVO.getStrategy();
//                if (strategy.equals(2)){
//                    LambdaQueryWrapper<CollegesSpecializedUsual> wrapper = new LambdaQueryWrapper<>();
//                    wrapper.le(CollegesSpecializedUsual::getLastYearAverageScore,scores).le(CollegesSpecializedUsual::getTwoYearsAverageScore,scores).ge(CollegesSpecializedUsual::getLastYearAverageBit,bits).ge(CollegesSpecializedUsual::getTwoYearsAverageBit,bits);
//                    IPage<CollegesSpecializedUsual> pageOne = collegesSpecializedUsualService.getBaseMapper().selectPage(page, wrapper);
//                    List<CollegesSpecializedUsual> records = pageOne.getRecords();
//                    IPage<MockProbabilityDTO> pageModel = new Page<>(current,limit);
//                    pageModel.setTotal(pageOne.getTotal());
//                    pageModel.setPages(pageOne.getPages());
//                    for (int i = 0; i < records.size(); i++) {
//                        MockProbabilityDTO mockProbabilityDTO = new MockProbabilityDTO();
//                        BeanUtils.copyProperties(records.get(i),mockProbabilityDTO);
//                        LambdaQueryWrapper<CollegesSpecialized> queryWrapper = new LambdaQueryWrapper<>();
//                        queryWrapper.eq(CollegesSpecialized::getId,records.get(i).getSpecializedId());
//                        CollegesSpecialized collegesSpecialized = collegesSpecializedService.getBaseMapper().selectOne(queryWrapper);
//                        BeanUtils.copyProperties(collegesSpecialized,mockProbabilityDTO);
//                        LambdaQueryWrapper<Colleges> wrapper1 = new LambdaQueryWrapper<>();
//                        wrapper1.eq(Colleges::getId,collegesSpecialized.getCollegesId());
//                        Colleges colleges = collegesService.getBaseMapper().selectOne(wrapper1);
//                        BeanUtils.copyProperties(colleges,mockProbabilityDTO);
//                        mockProbabilityDTO.setId(collegesSpecialized.getId());
//                        mockProbabilityDTO.setProbability("保");
//                        mockProbabilityDTO.setProbability("95%");
//                        //查看是否已经收藏该院校   或 收藏位置
//                        LambdaQueryWrapper<Mock> lambdaQueryWrapper = new LambdaQueryWrapper<>();
//                        lambdaQueryWrapper.eq(Mock::getUserId,id);
//                        lambdaQueryWrapper.eq(Mock::getMockId,mockProbabilityDTO.getId());
//                        Mock mock = mockService.getBaseMapper().selectOne(lambdaQueryWrapper);
//                        if (mock!=null&&!mock.equals("")){
//                            mockProbabilityDTO.setCollection(1);
//                            mockProbabilityDTO.setSort(mock.getSort());
//                        }else {
//                            mockProbabilityDTO.setCollection(0);
//                            mockProbabilityDTO.setSort(0);
//                        }
//                        pageModel.getRecords().add(mockProbabilityDTO);
//                    }
//
//                    return R.ok().data("pageModel",pageModel);
//
//                }else if (strategy.equals(1)){
//                    LambdaQueryWrapper<CollegesSpecializedUsual> wrapper = new LambdaQueryWrapper<>();
//                    LambdaQueryWrapper<CollegesSpecializedUsual> wrapper1 = new LambdaQueryWrapper<>();
//                    LambdaQueryWrapper<CollegesSpecializedUsual> wrapper2 = new LambdaQueryWrapper<>();
//                    LambdaQueryWrapper<CollegesSpecializedUsual> wrapper3 = new LambdaQueryWrapper<>();
//                    LambdaQueryWrapper<CollegesSpecializedUsual> wrapper4 = new LambdaQueryWrapper<>();
//
//                    LambdaQueryWrapper<CollegesSpecializedUsual> wrapper5 = new LambdaQueryWrapper<>();
//                    LambdaQueryWrapper<CollegesSpecializedUsual> wrapper6 = new LambdaQueryWrapper<>();
//                    LambdaQueryWrapper<CollegesSpecializedUsual> wrapper7 = new LambdaQueryWrapper<>();
//                    LambdaQueryWrapper<CollegesSpecializedUsual> wrapper8 = new LambdaQueryWrapper<>();
//                    LambdaQueryWrapper<CollegesSpecializedUsual> wrapper9 = new LambdaQueryWrapper<>();
//                    LambdaQueryWrapper<CollegesSpecializedUsual> wrapper10 = new LambdaQueryWrapper<>();
//                    wrapper1.ge(CollegesSpecializedUsual::getLastYearAverageScore,scores).ge(CollegesSpecializedUsual::getTwoYearsAverageScore,scores).
//                            le(CollegesSpecializedUsual::getLastYearAverageBit,bits).ge(CollegesSpecializedUsual::getTwoYearsAverageBit,bits);
//                    wrapper2.ge(CollegesSpecializedUsual::getLastYearAverageScore,scores).ge(CollegesSpecializedUsual::getTwoYearsAverageScore,scores).ge(CollegesSpecializedUsual::getLastYearAverageBit,bits).le(CollegesSpecializedUsual::getTwoYearsAverageBit,bits);
//                    wrapper3.ge(CollegesSpecializedUsual::getLastYearAverageScore,scores).le(CollegesSpecializedUsual::getTwoYearsAverageScore,scores).le(CollegesSpecializedUsual::getLastYearAverageBit,bits).le(CollegesSpecializedUsual::getTwoYearsAverageBit,bits);
//                    wrapper4.le(CollegesSpecializedUsual::getLastYearAverageScore,scores).ge(CollegesSpecializedUsual::getTwoYearsAverageScore,scores).le(CollegesSpecializedUsual::getLastYearAverageBit,bits).le(CollegesSpecializedUsual::getTwoYearsAverageBit,bits);
//
//                    wrapper5.ge(CollegesSpecializedUsual::getLastYearAverageScore,scores).ge(CollegesSpecializedUsual::getTwoYearsAverageScore,scores).
//                            ge(CollegesSpecializedUsual::getLastYearAverageBit,bits).ge(CollegesSpecializedUsual::getTwoYearsAverageBit,bits);
//                    wrapper6.ge(CollegesSpecializedUsual::getLastYearAverageScore,scores).le(CollegesSpecializedUsual::getTwoYearsAverageScore,scores).ge(CollegesSpecializedUsual::getLastYearAverageBit,bits).le(CollegesSpecializedUsual::getTwoYearsAverageBit,bits);
//                    wrapper7.le(CollegesSpecializedUsual::getLastYearAverageScore,scores).le(CollegesSpecializedUsual::getTwoYearsAverageScore,scores).le(CollegesSpecializedUsual::getLastYearAverageBit,bits).le(CollegesSpecializedUsual::getTwoYearsAverageBit,bits);
//                    wrapper8.le(CollegesSpecializedUsual::getLastYearAverageScore,scores).ge(CollegesSpecializedUsual::getTwoYearsAverageScore,scores).ge(CollegesSpecializedUsual::getLastYearAverageBit,bits).le(CollegesSpecializedUsual::getTwoYearsAverageBit,bits);
//                    wrapper9.ge(CollegesSpecializedUsual::getLastYearAverageScore,scores).le(CollegesSpecializedUsual::getTwoYearsAverageScore,scores).le(CollegesSpecializedUsual::getLastYearAverageBit,bits).ge(CollegesSpecializedUsual::getTwoYearsAverageBit,bits);
//                    wrapper10.le(CollegesSpecializedUsual::getLastYearAverageScore,scores).ge(CollegesSpecializedUsual::getTwoYearsAverageScore,scores).le(CollegesSpecializedUsual::getLastYearAverageBit,bits).ge(CollegesSpecializedUsual::getTwoYearsAverageBit,bits);
//
//                    wrapper.or((Consumer<LambdaQueryWrapper<CollegesSpecializedUsual>>) wrapper1)
//                            .or((Consumer<LambdaQueryWrapper<CollegesSpecializedUsual>>) wrapper2)
//                            .or((Consumer<LambdaQueryWrapper<CollegesSpecializedUsual>>) wrapper3)
//                            .or((Consumer<LambdaQueryWrapper<CollegesSpecializedUsual>>) wrapper4)
//                            .or((Consumer<LambdaQueryWrapper<CollegesSpecializedUsual>>) wrapper5)
//                            .or((Consumer<LambdaQueryWrapper<CollegesSpecializedUsual>>) wrapper6)
//                            .or((Consumer<LambdaQueryWrapper<CollegesSpecializedUsual>>) wrapper7)
//                            .or((Consumer<LambdaQueryWrapper<CollegesSpecializedUsual>>) wrapper8)
//                            .or((Consumer<LambdaQueryWrapper<CollegesSpecializedUsual>>) wrapper9)
//                            .or((Consumer<LambdaQueryWrapper<CollegesSpecializedUsual>>) wrapper10);
//                    IPage<CollegesSpecializedUsual> pageOne = collegesSpecializedUsualService.getBaseMapper().selectPage(page, wrapper);
//                    List<CollegesSpecializedUsual> records = pageOne.getRecords();
//                    IPage<MockProbabilityDTO> pageModel = new Page<>(current,limit);
//                    pageModel.setTotal(pageOne.getTotal());
//                    pageModel.setPages(pageOne.getPages());
//                    for (int i = 0; i < records.size(); i++) {
//                        MockProbabilityDTO mockProbabilityDTO = new MockProbabilityDTO();
//                        BeanUtils.copyProperties(records.get(i),mockProbabilityDTO);
//                        LambdaQueryWrapper<CollegesSpecialized> queryWrapper = new LambdaQueryWrapper<>();
//                        queryWrapper.eq(CollegesSpecialized::getId,records.get(i).getSpecializedId());
//                        CollegesSpecialized collegesSpecialized = collegesSpecializedService.getBaseMapper().selectOne(queryWrapper);
//                        BeanUtils.copyProperties(collegesSpecialized,mockProbabilityDTO);
//                        Colleges colleges = collegesService.getBaseMapper().selectById(collegesSpecialized.getCollegesId());
//                        BeanUtils.copyProperties(colleges,mockProbabilityDTO);
//                        mockProbabilityDTO.setId(collegesSpecialized.getId());
//                        mockProbabilityDTO.setProbability("稳");
//                        mockProbabilityDTO.setProbability("50%~75%");
//                        //查看是否已经收藏该院校   或 收藏位置
//                        LambdaQueryWrapper<Mock> lambdaQueryWrapper = new LambdaQueryWrapper<>();
//                        lambdaQueryWrapper.eq(Mock::getUserId,id);
//                        lambdaQueryWrapper.eq(Mock::getMockId,mockProbabilityDTO.getId());
//                        Mock mock = mockService.getBaseMapper().selectOne(lambdaQueryWrapper);
//                        if (mock!=null&&!mock.equals("")){
//                            mockProbabilityDTO.setCollection(1);
//                            mockProbabilityDTO.setSort(mock.getSort());
//                        }else {
//                            mockProbabilityDTO.setCollection(0);
//                            mockProbabilityDTO.setSort(0);
//                        }
//                        pageModel.getRecords().add(mockProbabilityDTO);
//                    }
//
//                    return R.ok().data("pageModel",pageModel);
//                }else {
//                    LambdaQueryWrapper<CollegesSpecializedUsual> wrapper = new LambdaQueryWrapper<>();
//                    LambdaQueryWrapper<CollegesSpecializedUsual> wrapper1 = new LambdaQueryWrapper<>();
//                    LambdaQueryWrapper<CollegesSpecializedUsual> wrapper2 = new LambdaQueryWrapper<>();
//                    LambdaQueryWrapper<CollegesSpecializedUsual> wrapper3 = new LambdaQueryWrapper<>();
//                    LambdaQueryWrapper<CollegesSpecializedUsual> wrapper4 = new LambdaQueryWrapper<>();
//
//                    wrapper1.ge(CollegesSpecializedUsual::getLastYearAverageScore,scores).le(CollegesSpecializedUsual::getTwoYearsAverageScore,scores).
//                            ge(CollegesSpecializedUsual::getLastYearAverageBit,bits).ge(CollegesSpecializedUsual::getTwoYearsAverageBit,bits);
//                    wrapper2.le(CollegesSpecializedUsual::getLastYearAverageScore,scores).ge(CollegesSpecializedUsual::getTwoYearsAverageScore,scores).ge(CollegesSpecializedUsual::getLastYearAverageBit,bits).ge(CollegesSpecializedUsual::getTwoYearsAverageBit,bits);
//                    wrapper3.le(CollegesSpecializedUsual::getLastYearAverageScore,scores).le(CollegesSpecializedUsual::getTwoYearsAverageScore,scores).ge(CollegesSpecializedUsual::getLastYearAverageBit,bits).le(CollegesSpecializedUsual::getTwoYearsAverageBit,bits);
//                    wrapper4.le(CollegesSpecializedUsual::getLastYearAverageScore,scores).le(CollegesSpecializedUsual::getTwoYearsAverageScore,scores).le(CollegesSpecializedUsual::getLastYearAverageBit,bits).ge(CollegesSpecializedUsual::getTwoYearsAverageBit,bits);
//
//                    wrapper.le(CollegesSpecializedUsual::getLastYearAverageScore,scores).le(CollegesSpecializedUsual::getTwoYearsAverageScore,scores).ge(CollegesSpecializedUsual::getLastYearAverageBit,bits).ge(CollegesSpecializedUsual::getTwoYearsAverageBit,bits)
//                            .or((Consumer<LambdaQueryWrapper<CollegesSpecializedUsual>>) wrapper1)
//                            .or((Consumer<LambdaQueryWrapper<CollegesSpecializedUsual>>) wrapper2)
//                            .or((Consumer<LambdaQueryWrapper<CollegesSpecializedUsual>>) wrapper3)
//                            .or((Consumer<LambdaQueryWrapper<CollegesSpecializedUsual>>) wrapper4);
//                    IPage<CollegesSpecializedUsual> pageOne = collegesSpecializedUsualService.getBaseMapper().selectPage(page, wrapper);
//                    List<CollegesSpecializedUsual> records = pageOne.getRecords();
//                    IPage<MockProbabilityDTO> pageModel = new Page<>(current,limit);
//                    pageModel.setTotal(pageOne.getTotal());
//                    pageModel.setPages(pageOne.getPages());
//                    for (int i = 0; i < records.size(); i++) {
//                        MockProbabilityDTO mockProbabilityDTO = new MockProbabilityDTO();
//                        BeanUtils.copyProperties(records.get(i),mockProbabilityDTO);
//                        LambdaQueryWrapper<CollegesSpecialized> queryWrapper = new LambdaQueryWrapper<>();
//                        queryWrapper.eq(CollegesSpecialized::getId,records.get(i).getSpecializedId());
//                        CollegesSpecialized collegesSpecialized = collegesSpecializedService.getBaseMapper().selectOne(queryWrapper);
//                        BeanUtils.copyProperties(collegesSpecialized,mockProbabilityDTO);
//                        Colleges colleges = collegesService.getBaseMapper().selectById(collegesSpecialized.getCollegesId());
//                        BeanUtils.copyProperties(colleges,mockProbabilityDTO);
//                        mockProbabilityDTO.setId(collegesSpecialized.getId());
//                        mockProbabilityDTO.setProbability("冲");
//                        mockProbabilityDTO.setProbability("1%~25%");
//                        //查看是否已经收藏该院校   或 收藏位置
//                        LambdaQueryWrapper<Mock> lambdaQueryWrapper = new LambdaQueryWrapper<>();
//                        lambdaQueryWrapper.eq(Mock::getUserId,id);
//                        lambdaQueryWrapper.eq(Mock::getMockId,mockProbabilityDTO.getId());
//                        Mock mock = mockService.getBaseMapper().selectOne(lambdaQueryWrapper);
//                        if (mock!=null&&!mock.equals("")){
//                            mockProbabilityDTO.setCollection(1);
//                            mockProbabilityDTO.setSort(mock.getSort());
//                        }else {
//                            mockProbabilityDTO.setCollection(0);
//                            mockProbabilityDTO.setSort(0);
//                        }
//                        pageModel.getRecords().add(mockProbabilityDTO);
//                    }
//
//                    return R.ok().data("pageModel",pageModel);
//                }
//        }
//    }

}

