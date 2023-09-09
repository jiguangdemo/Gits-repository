package com.sdau.nemt.service.volunteer.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sdau.nemt.common.base.result.R;
import com.sdau.nemt.service.volunteer.entity.Colleges;
import com.sdau.nemt.service.volunteer.entity.CollegesSpecialized;
import com.sdau.nemt.service.volunteer.entity.CollegesSpecializedUsual;
import com.sdau.nemt.service.volunteer.entity.Mock;
import com.sdau.nemt.service.volunteer.entity.dto.LocationDTO;
import com.sdau.nemt.service.volunteer.entity.dto.MockDTO;
import com.sdau.nemt.service.volunteer.entity.dto.MockProbabilityDTO;
import com.sdau.nemt.service.volunteer.entity.export.MockExport;
import com.sdau.nemt.service.volunteer.entity.vo.InfoVO;
import com.sdau.nemt.service.volunteer.service.*;
import com.sdau.nemt.service.volunteer.util.ExcelUtil;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author
 * @since 2023-08-11
 */
@Api(tags = "志愿信息")
@RestController
@RequestMapping("/volunteer/mock")
public class MockController {


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


    @ApiOperation("查看用户当前志愿位置情况")
    @GetMapping("location")
    public R location(HttpServletRequest request){
        String token = request.getHeader("token");
        String username = JwtTokenUtils.getUsername(token);
        String user = redisTemplate.opsForValue().get("user" + username);
        InfoVO infoVO = JSON.parseObject(user, InfoVO.class);
        String id = infoVO.getId();
        LambdaQueryWrapper<Mock> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Mock::getUserId,infoVO.getId());
        List<Mock> mockList = mockService.getBaseMapper().selectList(wrapper);
        if (mockList.size()==0){
            List<LocationDTO> list = new ArrayList<>();
            for (int i = 1; i <= 96; i++) {
                LocationDTO locationDTO = new LocationDTO();
                locationDTO.setExist(0);
                locationDTO.setSort(i);
                list.add(locationDTO);
            }
            return R.ok().data("list",list);
        }

        List<Integer> sortList = new ArrayList<>();
        for (int i = 0; i < mockList.size(); i++) {
            sortList.add(mockList.get(i).getSort());
        }
        List<LocationDTO> list = new ArrayList<>();
        for (int i = 1; i <= 96; i++) {
            LocationDTO locationDTO = new LocationDTO();
            for (int j = 0; j < sortList.size(); j++) {
                if (sortList.get(j)==i||sortList.get(j).equals(i)){
                    locationDTO.setExist(1);
                    break;
                }else {
                    locationDTO.setExist(0);
                }
            }
            locationDTO.setSort(i);
            list.add(locationDTO);
        }
        return R.ok().data("list",list);
    }

    @ApiOperation("选择志愿")
    @GetMapping("select-mock/{specializedId}/{sort}")
    public R selectMock(@ApiParam(name = "specializedId",value = "院校开设专业主键id") @PathVariable String specializedId,
                        @ApiParam(name = "sort",value = "设置为第几志愿") @PathVariable(required = true) Integer sort,
                        HttpServletRequest request){
        String token = request.getHeader("token");
        String username = JwtTokenUtils.getUsername(token);
        String user = redisTemplate.opsForValue().get("user" + username);
        InfoVO infoVO = JSON.parseObject(user, InfoVO.class);
        String id = infoVO.getId();
        LambdaQueryWrapper<Mock> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Mock::getUserId,infoVO.getId());
        wrapper.eq(Mock::getMockId,specializedId);
        Mock mock = mockService.getBaseMapper().selectOne(wrapper);
        if (mock!=null){
            return R.error().message("您已经选择了该志愿，请选择其他志愿");
        }
        LambdaQueryWrapper<Mock> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Mock::getUserId,infoVO.getId());
        queryWrapper.eq(Mock::getSort,sort);
        Mock selectOne = mockService.getBaseMapper().selectOne(queryWrapper);
        if (selectOne!=null&&!selectOne.equals("")){
            Mock mockUpdate = new Mock();
            mockUpdate.setMockId(specializedId);
            mockUpdate.setUserId(id);
            mockUpdate.setId(selectOne.getId());
            mockUpdate.setSort(sort);
            mockService.getBaseMapper().updateById(mockUpdate);
            return R.ok().message("志愿替换成功");
        }

        Mock mockModel = new Mock();
        mockModel.setUserId(infoVO.getId());
        mockModel.setMockId(specializedId);
        if (sort!=null&&!sort.equals("")) {
            mockModel.setSort(sort);
        }
        mockService.getBaseMapper().insert(mockModel);
        return R.ok().message("添加志愿成功");
    }

    @ApiOperation("获取所有志愿列表")
    @GetMapping("list")
    public R mockList(HttpServletRequest request){
        String token = request.getHeader("token");
        String username = JwtTokenUtils.getUsername(token);
        String user = redisTemplate.opsForValue().get("user" + username);
        InfoVO infoVO = JSON.parseObject(user, InfoVO.class);
        String id = infoVO.getId();
        LambdaQueryWrapper<Mock> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Mock::getUserId,id);
        wrapper.orderByAsc(Mock::getSort);
        List<Mock> mockList = mockService.getBaseMapper().selectList(wrapper);
        if(mockList==null||mockList.equals("")){
            List<MockDTO> modelList = new ArrayList<>();
            return R.ok().data("modelList",modelList);
        }
        List<MockDTO> modelList = new ArrayList<>();
        List<String> collegesSpecializedIdList = new ArrayList<>();
        for (int i = 0; i < mockList.size(); i++) {
            collegesSpecializedIdList.add(mockList.get(i).getMockId());
        }
        if (collegesSpecializedIdList.size()!=0) {
            for (int i = 0; i < collegesSpecializedIdList.size(); i++) {
                CollegesSpecialized collegesSpecialized = collegesSpecializedService.getBaseMapper().selectById(collegesSpecializedIdList.get(i));
                MockDTO mockDTO = new MockDTO();
                LambdaQueryWrapper<Colleges> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(Colleges::getId,collegesSpecialized.getCollegesId());
                Colleges colleges = collegesService.getBaseMapper().selectOne(queryWrapper);

//                CollegesSpecializedUsual collegesSpecializedUsual = new CollegesSpecializedUsual();
                LambdaQueryWrapper<CollegesSpecializedUsual> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                lambdaQueryWrapper.eq(CollegesSpecializedUsual::getSpecializedId,collegesSpecialized.getId());
                CollegesSpecializedUsual collegesSpecializedUsualOne = collegesSpecializedUsualService.getBaseMapper().selectOne(lambdaQueryWrapper);
                if (collegesSpecializedUsualOne!=null&&!collegesSpecializedUsualOne.equals("")){
                    BeanUtils.copyProperties(collegesSpecializedUsualOne,mockDTO);
                }

                BeanUtils.copyProperties(colleges,mockDTO);
                BeanUtils.copyProperties(collegesSpecialized,mockDTO);
//            mockDTO.setId(collegesSpecializedList.get(i).getId());
                BeanUtils.copyProperties(mockList.get(i),mockDTO);
                modelList.add(mockDTO);
            }

//            List<CollegesSpecialized> collegesSpecializedList = collegesSpecializedService.getBaseMapper().selectBatchIds(collegesSpecializedIdList);
//            for (int i = 0; i < collegesSpecializedList.size(); i++) {
//                MockDTO mockDTO = new MockDTO();
//                LambdaQueryWrapper<Colleges> queryWrapper = new LambdaQueryWrapper<>();
//                queryWrapper.eq(Colleges::getId,collegesSpecializedList.get(i).getCollegesId());
//                Colleges colleges = collegesService.getBaseMapper().selectOne(queryWrapper);
//
//                BeanUtils.copyProperties(colleges,mockDTO);
//                BeanUtils.copyProperties(collegesSpecializedList.get(i),mockDTO);
////            mockDTO.setId(collegesSpecializedList.get(i).getId());
//                BeanUtils.copyProperties(mockList.get(i),mockDTO);
//                modelList.add(mockDTO);
            }
        return R.ok().data("modelList",modelList);
    }

//    @ApiOperation("排序保存")
//    @PutMapping("save-sort")
    public R saveSort(@ApiParam(name = "mockDTOList",value = "所有志愿")@RequestBody(required = true) List<MockDTO> mockDTOList){

        for (int i = 0; i < mockDTOList.size(); i++) {
            MockDTO mockDTO = new MockDTO();
            mockDTO = mockDTOList.get(i);
            Mock mock = new Mock();
            mock.setId(mockDTO.getId());
            mock.setSort(mockDTO.getSort());
            mockService.getBaseMapper().updateById(mock);
        }
        return R.ok().message("排序成功");
    }

    @ApiOperation("志愿列表页面--删除志愿")
    @DeleteMapping("remove-mock/{id}")
    public R removeMock(@ApiParam(name = "id",value = "志愿主键")@PathVariable String id){
        int i = mockService.getBaseMapper().deleteById(id);
        if (i == 1){
            return R.ok().message("删除志愿成功");
        }else {
            return R.ok().message("删除错误，无该志愿");
        }
    }

    @ApiOperation("模拟填报志愿页面--删除志愿")
    @DeleteMapping("remove-volunteer/{id}")
    public R removeVolunteer(@ApiParam(name = "id",value = "志愿主键")@PathVariable String id,
                             HttpServletRequest request){
        String token = request.getHeader("token");
        String username = JwtTokenUtils.getUsername(token);
        String user = redisTemplate.opsForValue().get("user" + username);
        InfoVO infoVO = JSON.parseObject(user, InfoVO.class);
        String userId = infoVO.getId();
        LambdaQueryWrapper<Mock> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Mock::getUserId,userId);
        wrapper.eq(Mock::getMockId,id);
        int i = mockService.getBaseMapper().delete(wrapper);
        if (i == 1){
            return R.ok().message("删除志愿成功");
        }else {
            return R.ok().message("删除错误，无该志愿");
        }
    }
    //@ApiOperation("导出志愿表")
    //@GetMapping("export")
    public R export(HttpServletRequest request){
        String token = request.getHeader("token");
        String username = JwtTokenUtils.getUsername(token);
        String user = redisTemplate.opsForValue().get("user" + username);
        InfoVO infoVO = JSON.parseObject(user, InfoVO.class);
        String id = infoVO.getId();
        LambdaQueryWrapper<Mock> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Mock::getUserId,id);
        List<Mock> mockList = mockService.getBaseMapper().selectList(wrapper);
        List<MockExport> modelList = new ArrayList<>();
        List<String> collegesSpecializedIdList = new ArrayList<>();
        for (int i = 0; i < mockList.size(); i++) {
            collegesSpecializedIdList.add(mockList.get(i).getMockId());
        }
        List<CollegesSpecialized> collegesSpecializedList = collegesSpecializedService.getBaseMapper().selectBatchIds(collegesSpecializedIdList);
        for (int i = 0; i < collegesSpecializedList.size(); i++) {
            MockExport mockDTO = new MockExport();
            LambdaQueryWrapper<Colleges> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Colleges::getId,collegesSpecializedList.get(i).getCollegesId());
            Colleges colleges = collegesService.getBaseMapper().selectOne(queryWrapper);
            BeanUtils.copyProperties(colleges,mockDTO);
            BeanUtils.copyProperties(collegesSpecializedList.get(i),mockDTO);
            mockDTO.setId(collegesSpecializedList.get(i).getId());
            modelList.add(mockDTO);
        }
        String excelWrite = ExcelUtil.mockExcelWrite("志愿列表", modelList);
        return R.ok().data("excel",excelWrite);
    }

    @ApiOperation("上移志愿")
    @GetMapping("move-up/{mockId}/{sort}")
    public R moveUp(@ApiParam(value = "志愿id", name = "mockId") @PathVariable String mockId,
                    @ApiParam(value = "志愿sort",name = "sort")@PathVariable Integer sort,
                    HttpServletRequest request){
        String token = request.getHeader("token");
        String username = JwtTokenUtils.getUsername(token);
        String user = redisTemplate.opsForValue().get("user" + username);
        InfoVO infoVO = JSON.parseObject(user, InfoVO.class);
        String id = infoVO.getId();
        Integer upSort = sort - 1;
        Mock mock = new Mock();
        mock.setId(mockId);
        mock.setSort(upSort);
        LambdaQueryWrapper<Mock> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Mock::getUserId,id);
        wrapper.eq(Mock::getSort,upSort);
        Mock mockUp = mockService.getBaseMapper().selectOne(wrapper);
        if (mockUp==null||mockUp.equals("")){
            mockService.getBaseMapper().updateById(mock);
        }else {
            mockUp.setSort(sort);
            mockService.getBaseMapper().updateById(mock);
            mockService.getBaseMapper().updateById(mockUp);
        }
        return R.ok().message("往上移动成功");
    }

    @ApiOperation("下移志愿")
    @GetMapping("move-down/{mockId}/{sort}")
    public R moveDowm(@ApiParam(value = "志愿id", name = "mockId") @PathVariable String mockId,
                      @ApiParam(value = "志愿sort",name = "sort")@PathVariable Integer sort,
                    HttpServletRequest request){
        String token = request.getHeader("token");
        String username = JwtTokenUtils.getUsername(token);
        String user = redisTemplate.opsForValue().get("user" + username);
        InfoVO infoVO = JSON.parseObject(user, InfoVO.class);
        String id = infoVO.getId();
        Integer downSort = sort + 1;
        Mock mock = new Mock();
        mock.setId(mockId);
        mock.setSort(downSort);
        LambdaQueryWrapper<Mock> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Mock::getUserId,id);
        wrapper.eq(Mock::getSort,downSort);
        Mock mockUp = mockService.getBaseMapper().selectOne(wrapper);
        if (mockUp==null||mockUp.equals("")){
            mockService.getBaseMapper().updateById(mock);
        }else {
            mockUp.setSort(sort);
            mockService.getBaseMapper().updateById(mock);
            mockService.getBaseMapper().updateById(mockUp);
        }
        return R.ok().message("往下移动成功");
    }

    @ApiOperation("移动志愿")
    @GetMapping("move/{mockId}/{sort}/{moveSort}")
    public R move(@ApiParam(value = "志愿id", name = "mockId") @PathVariable String mockId,
                  @ApiParam(value = "志愿sort",name = "sort")@PathVariable Integer sort,
                  @ApiParam(value = "目标位置",name = "moveSort")@PathVariable Integer moveSort,
                  HttpServletRequest request){
        String token = request.getHeader("token");
        String username = JwtTokenUtils.getUsername(token);
        String user = redisTemplate.opsForValue().get("user" + username);
        InfoVO infoVO = JSON.parseObject(user, InfoVO.class);
        String id = infoVO.getId();
        Mock mock = mockService.getBaseMapper().selectById(mockId);
        mock.setSort(moveSort);
        LambdaQueryWrapper<Mock> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Mock::getUserId,id);
        wrapper.eq(Mock::getSort,moveSort);
        Mock selectOne = mockService.getBaseMapper().selectOne(wrapper);
        if (selectOne==null||selectOne.equals("")){
            mockService.getBaseMapper().updateById(mock);
        }else {
            selectOne.setSort(sort);
            mockService.getBaseMapper().updateById(mock);
            mockService.getBaseMapper().updateById(selectOne);
        }
        return R.ok().message("移动成功");
    }


    @ApiOperation("智能推荐志愿")
    @GetMapping("intelligent")
    public R intelligent(HttpServletRequest request){
        String token = request.getHeader("token");
        String username = JwtTokenUtils.getUsername(token);
        String user = redisTemplate.opsForValue().get("user" + username);
        InfoVO infoVO = JSON.parseObject(user, InfoVO.class);
        String id = infoVO.getId();
        String scores = infoVO.getScores();
        String bitsString = infoVO.getBits();
        Integer subjectsOne = infoVO.getSubjectsOne();
        Integer subjectsTwo = infoVO.getSubjectsTwo();
        Integer subjectsThree = infoVO.getSubjectsThree();
        String one = "";
        String two = "";
        String three = "";
        if (subjectsOne==1){
            one = "物";
        }else if (subjectsOne==2){
            one = "化";
        }else if (subjectsOne==3){
            one = "生";
        }else if (subjectsOne==4){
            one = "政";
        }else if (subjectsOne==5){
            one = "历";
        }else if (subjectsOne==6){
            one = "地";
        }
        if (subjectsTwo==1){
            two = "物";
        }else if (subjectsTwo==2){
            two = "化";
        }else if (subjectsTwo==3){
            two = "生";
        }else if (subjectsTwo==4){
            two = "政";
        }else if (subjectsTwo==5){
            two = "历";
        }else if (subjectsTwo==6){
            two = "地";
        }
        if (subjectsThree==1){
            three = "物";
        }else if (subjectsThree==2){
            three = "化";
        }else if (subjectsThree==3){
            three = "生";
        }else if (subjectsThree==4){
            three = "政";
        }else if (subjectsThree==5){
            three = "历";
        }else if (subjectsThree==6){
            three = "地";
        }
        Integer bits = Integer.parseInt(bitsString);
//        Page<CollegesSpecializedUsual> page = new Page<>(current,10);
//        IPage<MockProbabilityDTO> pageModel = new Page<>();
        List<MockProbabilityDTO> chongModelList = new ArrayList<>();
        List<MockProbabilityDTO> wenModelList = new ArrayList<>();
        List<MockProbabilityDTO> baoModelList = new ArrayList<>();
        LambdaQueryWrapper<CollegesSpecializedUsual> wrapper = new LambdaQueryWrapper<>();
        if (bits<=3000){
            wrapper.ge(CollegesSpecializedUsual::getLastYearLowestBit,bits-0.4*bits);
            wrapper.le(CollegesSpecializedUsual::getLastYearLowestBit,bits);
            wrapper.ge(CollegesSpecializedUsual::getTwoYearsLowestBit,bits-0.4*bits);
            wrapper.le(CollegesSpecializedUsual::getTwoYearsLowestBit,bits);
        }else if (bits<=8000){
            wrapper.ge(CollegesSpecializedUsual::getLastYearLowestBit,bits-0.2*bits);
            wrapper.le(CollegesSpecializedUsual::getLastYearLowestBit,bits);
            wrapper.ge(CollegesSpecializedUsual::getTwoYearsLowestBit,bits-0.2*bits);
            wrapper.le(CollegesSpecializedUsual::getTwoYearsLowestBit,bits);
        }else if (bits<=30000){
            wrapper.ge(CollegesSpecializedUsual::getLastYearLowestBit,bits-0.1*bits);
            wrapper.le(CollegesSpecializedUsual::getLastYearLowestBit,bits);
            wrapper.ge(CollegesSpecializedUsual::getTwoYearsLowestBit,bits-0.1*bits);
            wrapper.le(CollegesSpecializedUsual::getTwoYearsLowestBit,bits);
        }else if (bits<=50000){
            wrapper.ge(CollegesSpecializedUsual::getLastYearLowestBit,bits-0.08*bits);
            wrapper.le(CollegesSpecializedUsual::getLastYearLowestBit,bits);
            wrapper.ge(CollegesSpecializedUsual::getTwoYearsLowestBit,bits-0.08*bits);
            wrapper.le(CollegesSpecializedUsual::getTwoYearsLowestBit,bits);
        }else if (bits<=100000){
            wrapper.ge(CollegesSpecializedUsual::getLastYearLowestBit,bits-0.1*bits);
            wrapper.le(CollegesSpecializedUsual::getLastYearLowestBit,bits);
            wrapper.ge(CollegesSpecializedUsual::getTwoYearsLowestBit,bits-0.1*bits);
            wrapper.le(CollegesSpecializedUsual::getTwoYearsLowestBit,bits);
        }else if (bits<=180000){
            wrapper.ge(CollegesSpecializedUsual::getLastYearLowestBit,bits-0.05*bits);
            wrapper.le(CollegesSpecializedUsual::getLastYearLowestBit,bits);
            wrapper.ge(CollegesSpecializedUsual::getTwoYearsLowestBit,bits-0.05*bits);
            wrapper.le(CollegesSpecializedUsual::getTwoYearsLowestBit,bits);
        }else if (bits<=270000){
            wrapper.ge(CollegesSpecializedUsual::getLastYearLowestBit,bits-0.03*bits);
            wrapper.le(CollegesSpecializedUsual::getLastYearLowestBit,bits);
            wrapper.ge(CollegesSpecializedUsual::getTwoYearsLowestBit,bits-0.03*bits);
            wrapper.le(CollegesSpecializedUsual::getTwoYearsLowestBit,bits);
        }else if (bits<=400000){
            wrapper.ge(CollegesSpecializedUsual::getLastYearLowestBit,bits-0.08*bits);
            wrapper.le(CollegesSpecializedUsual::getLastYearLowestBit,bits);
            wrapper.ge(CollegesSpecializedUsual::getTwoYearsLowestBit,bits-0.08*bits);
            wrapper.le(CollegesSpecializedUsual::getTwoYearsLowestBit,bits);
        }else if (bits<=550000){
            wrapper.ge(CollegesSpecializedUsual::getLastYearLowestBit,bits-0.05*bits);
            wrapper.le(CollegesSpecializedUsual::getLastYearLowestBit,bits);
            wrapper.ge(CollegesSpecializedUsual::getTwoYearsLowestBit,bits-0.05*bits);
            wrapper.le(CollegesSpecializedUsual::getTwoYearsLowestBit,bits);
        }else {
            wrapper.ge(CollegesSpecializedUsual::getLastYearLowestBit,bits-0.08*bits);
            wrapper.le(CollegesSpecializedUsual::getLastYearLowestBit,bits);
            wrapper.ge(CollegesSpecializedUsual::getTwoYearsLowestBit,bits-0.08*bits);
            wrapper.le(CollegesSpecializedUsual::getTwoYearsLowestBit,bits);
        }

        List<CollegesSpecializedUsual> oneCollegesSpecializedUsualList = collegesSpecializedUsualService.getBaseMapper().selectList(wrapper);

        for (int i = 0; i < oneCollegesSpecializedUsualList.size(); i++) {
            MockProbabilityDTO mockProbabilityDTO = new MockProbabilityDTO();
            CollegesSpecialized collegesSpecialized = collegesSpecializedService.getBaseMapper().selectById(oneCollegesSpecializedUsualList.get(i).getSpecializedId());
            Colleges colleges = collegesService.getBaseMapper().selectById(collegesSpecialized.getCollegesId());
            if (colleges!=null&&!colleges.equals("")) {
                BeanUtils.copyProperties(oneCollegesSpecializedUsualList.get(i),mockProbabilityDTO);
                BeanUtils.copyProperties(colleges, mockProbabilityDTO);
                BeanUtils.copyProperties(collegesSpecialized,mockProbabilityDTO);
            }
            LambdaQueryWrapper<Mock> wrapper5 = new LambdaQueryWrapper<>();
            wrapper5.eq(Mock::getUserId,id);
            wrapper5.eq(Mock::getMockId,collegesSpecialized.getId());
            Mock mock = mockService.getBaseMapper().selectOne(wrapper5);
            if (mock!=null&&!mock.equals("")){
                mockProbabilityDTO.setSort(mock.getSort());
                mockProbabilityDTO.setCollection(1);
            }else {
                mockProbabilityDTO.setSort(0);
                mockProbabilityDTO.setCollection(0);
            }
            mockProbabilityDTO.setStrategy("冲");
            if (colleges!=null&&!colleges.equals("")) {
                chongModelList.add(mockProbabilityDTO);
            }
        }
        List<MockProbabilityDTO> oneList = new ArrayList<>();
        Pattern pattern = Pattern.compile("不限");
        for (int i = 0; i < chongModelList.size(); i++) {
            Matcher matcher = pattern.matcher(chongModelList.get(i).getSubjectRequest());
            if(matcher.find()){
                oneList.add(chongModelList.get(i));
            }
        }
        Pattern patterntwo = Pattern.compile(one);
        for (int i = 0; i < chongModelList.size(); i++) {
            Matcher matcher = patterntwo.matcher(chongModelList.get(i).getSubjectRequest());
            if(matcher.find()){
                oneList.add(chongModelList.get(i));
            }
        }
        Pattern patternthree = Pattern.compile(two);
        for (int i = 0; i < chongModelList.size(); i++) {
            Matcher matcher = patternthree.matcher(chongModelList.get(i).getSubjectRequest());
            if(matcher.find()){
                oneList.add(chongModelList.get(i));
            }
        }
        Pattern patternfour = Pattern.compile(three);
        for (int i = 0; i < chongModelList.size(); i++) {
            Matcher matcher = patternfour.matcher(chongModelList.get(i).getSubjectRequest());
            if(matcher.find()){
                oneList.add(chongModelList.get(i));
            }
        }
        List<MockProbabilityDTO> chongList = oneList.stream().distinct().collect(Collectors.toList());



        LambdaQueryWrapper<CollegesSpecializedUsual> wrapper2 = new LambdaQueryWrapper<>();
        if (bits<=3000){
            wrapper2.ge(CollegesSpecializedUsual::getLastYearLowestBit,bits);
            wrapper2.le(CollegesSpecializedUsual::getLastYearLowestBit,bits+0.4*bits);
            wrapper2.ge(CollegesSpecializedUsual::getTwoYearsLowestBit,bits);
            wrapper2.le(CollegesSpecializedUsual::getTwoYearsLowestBit,bits+0.4*bits);
        }else if (bits<=8000){
            wrapper2.ge(CollegesSpecializedUsual::getLastYearLowestBit,bits);
            wrapper2.le(CollegesSpecializedUsual::getLastYearLowestBit,bits+0.2*bits);
            wrapper2.ge(CollegesSpecializedUsual::getTwoYearsLowestBit,bits);
            wrapper2.le(CollegesSpecializedUsual::getTwoYearsLowestBit,bits+0.2*bits);
        }else if (bits<=30000){
            wrapper2.ge(CollegesSpecializedUsual::getLastYearLowestBit,bits);
            wrapper2.le(CollegesSpecializedUsual::getLastYearLowestBit,bits+0.1*bits);
            wrapper2.ge(CollegesSpecializedUsual::getTwoYearsLowestBit,bits);
            wrapper2.le(CollegesSpecializedUsual::getTwoYearsLowestBit,bits+0.1*bits);
        }else if (bits<=50000){
            wrapper2.ge(CollegesSpecializedUsual::getLastYearLowestBit,bits);
            wrapper2.le(CollegesSpecializedUsual::getLastYearLowestBit,bits+0.12*bits);
            wrapper2.ge(CollegesSpecializedUsual::getTwoYearsLowestBit,bits);
            wrapper2.le(CollegesSpecializedUsual::getTwoYearsLowestBit,bits+0.12*bits);
        }else if (bits<=100000){
            wrapper2.ge(CollegesSpecializedUsual::getLastYearLowestBit,bits);
            wrapper2.le(CollegesSpecializedUsual::getLastYearLowestBit,bits+0.1*bits);
            wrapper2.ge(CollegesSpecializedUsual::getTwoYearsLowestBit,bits);
            wrapper2.le(CollegesSpecializedUsual::getTwoYearsLowestBit,bits+0.1*bits);
        }else if (bits<=180000){
            wrapper2.ge(CollegesSpecializedUsual::getLastYearLowestBit,bits+0.01*bits);
            wrapper2.le(CollegesSpecializedUsual::getLastYearLowestBit,bits+0.07*bits);
            wrapper2.ge(CollegesSpecializedUsual::getTwoYearsLowestBit,bits+0.01*bits);
            wrapper2.le(CollegesSpecializedUsual::getTwoYearsLowestBit,bits+0.07*bits);
        }else if (bits<=270000){
            wrapper2.ge(CollegesSpecializedUsual::getLastYearLowestBit,bits+0.01*bits);
            wrapper2.le(CollegesSpecializedUsual::getLastYearLowestBit,bits+0.05*bits);
            wrapper2.ge(CollegesSpecializedUsual::getTwoYearsLowestBit,bits+0.01*bits);
            wrapper2.le(CollegesSpecializedUsual::getTwoYearsLowestBit,bits+0.05*bits);
        }else if (bits<=400000){
            wrapper2.ge(CollegesSpecializedUsual::getLastYearLowestBit,bits);
            wrapper2.le(CollegesSpecializedUsual::getLastYearLowestBit,bits+0.15*bits);
            wrapper2.ge(CollegesSpecializedUsual::getTwoYearsLowestBit,bits);
            wrapper2.le(CollegesSpecializedUsual::getTwoYearsLowestBit,bits+0.15*bits);
        }else if (bits<=550000){
            wrapper2.ge(CollegesSpecializedUsual::getLastYearLowestBit,bits);
            wrapper2.le(CollegesSpecializedUsual::getLastYearLowestBit,bits+0.08*bits);
            wrapper2.ge(CollegesSpecializedUsual::getTwoYearsLowestBit,bits);
            wrapper2.le(CollegesSpecializedUsual::getTwoYearsLowestBit,bits+0.08*bits);
        }else {
            wrapper2.ge(CollegesSpecializedUsual::getLastYearLowestBit,bits);
            wrapper2.le(CollegesSpecializedUsual::getLastYearLowestBit,bits+0.15*bits);
            wrapper2.ge(CollegesSpecializedUsual::getTwoYearsLowestBit,bits);
            wrapper2.le(CollegesSpecializedUsual::getTwoYearsLowestBit,bits+0.15*bits);
        }

        List<CollegesSpecializedUsual> twoCollegesSpecializedUsualList = collegesSpecializedUsualService.getBaseMapper().selectList(wrapper2);
        for (int i = 0; i < twoCollegesSpecializedUsualList.size(); i++) {
            MockProbabilityDTO mockProbabilityDTO = new MockProbabilityDTO();
            CollegesSpecialized collegesSpecialized = collegesSpecializedService.getBaseMapper().selectById(twoCollegesSpecializedUsualList.get(i).getSpecializedId());
            Colleges colleges = collegesService.getBaseMapper().selectById(collegesSpecialized.getCollegesId());
            if (colleges!=null&&!colleges.equals("")) {
                BeanUtils.copyProperties(twoCollegesSpecializedUsualList.get(i),mockProbabilityDTO);
                BeanUtils.copyProperties(colleges, mockProbabilityDTO);
                BeanUtils.copyProperties(collegesSpecialized,mockProbabilityDTO);
            }
            LambdaQueryWrapper<Mock> wrapper6 = new LambdaQueryWrapper<>();
            wrapper6.eq(Mock::getUserId,id);
            wrapper6.eq(Mock::getMockId,collegesSpecialized.getId());
            Mock mock = mockService.getBaseMapper().selectOne(wrapper6);
            if (mock!=null&&!mock.equals("")){
                mockProbabilityDTO.setSort(mock.getSort());
                mockProbabilityDTO.setCollection(1);
            }else {
                mockProbabilityDTO.setSort(0);
                mockProbabilityDTO.setCollection(0);
            }
            mockProbabilityDTO.setStrategy("稳");
            if (colleges!=null&&!colleges.equals("")) {
                wenModelList.add(mockProbabilityDTO);
            }
        }

        List<MockProbabilityDTO> twoList = new ArrayList<>();
        Pattern pattern5 = Pattern.compile("不限");
        for (int i = 0; i < wenModelList.size(); i++) {
            Matcher matcher = pattern5.matcher(wenModelList.get(i).getSubjectRequest());
            if(matcher.find()){
                twoList.add(wenModelList.get(i));
            }
        }
        Pattern pattern6 = Pattern.compile(one);
        for (int i = 0; i < wenModelList.size(); i++) {
            Matcher matcher = pattern6.matcher(wenModelList.get(i).getSubjectRequest());
            if(matcher.find()){
                twoList.add(wenModelList.get(i));
            }
        }
        Pattern pattern7 = Pattern.compile(two);
        for (int i = 0; i < wenModelList.size(); i++) {
            Matcher matcher = pattern7.matcher(wenModelList.get(i).getSubjectRequest());
            if(matcher.find()){
                twoList.add(wenModelList.get(i));
            }
        }
        Pattern pattern8 = Pattern.compile(three);
        for (int i = 0; i < wenModelList.size(); i++) {
            Matcher matcher = pattern8.matcher(wenModelList.get(i).getSubjectRequest());
            if(matcher.find()){
                twoList.add(wenModelList.get(i));
            }
        }
        List<MockProbabilityDTO> wenList = twoList.stream().distinct().collect(Collectors.toList());

        LambdaQueryWrapper<CollegesSpecializedUsual> wrapper3 = new LambdaQueryWrapper<>();
        if (bits<=3000){
            wrapper3.ge(CollegesSpecializedUsual::getLastYearLowestBit,bits+0.4*bits);
            wrapper3.le(CollegesSpecializedUsual::getLastYearLowestBit,bits+0.8*bits);
            wrapper3.ge(CollegesSpecializedUsual::getTwoYearsLowestBit,bits+0.4*bits);
            wrapper3.le(CollegesSpecializedUsual::getTwoYearsLowestBit,bits+0.8*bits);
        }else if (bits<=8000){
            wrapper3.ge(CollegesSpecializedUsual::getLastYearLowestBit,bits+0.2*bits);
            wrapper3.le(CollegesSpecializedUsual::getLastYearLowestBit,bits+0.4*bits);
            wrapper3.ge(CollegesSpecializedUsual::getTwoYearsLowestBit,bits+0.2*bits);
            wrapper3.le(CollegesSpecializedUsual::getTwoYearsLowestBit,bits+0.4*bits);
        }else if (bits<=30000){
            wrapper3.ge(CollegesSpecializedUsual::getLastYearLowestBit,bits+0.1*bits);
            wrapper3.le(CollegesSpecializedUsual::getLastYearLowestBit,bits+0.2*bits);
            wrapper3.ge(CollegesSpecializedUsual::getTwoYearsLowestBit,bits+0.1*bits);
            wrapper3.le(CollegesSpecializedUsual::getTwoYearsLowestBit,bits+0.2*bits);
        }else if (bits<=50000){
            wrapper3.ge(CollegesSpecializedUsual::getLastYearLowestBit,bits+0.12*bits);
            wrapper3.le(CollegesSpecializedUsual::getLastYearLowestBit,bits+0.25*bits);
            wrapper3.ge(CollegesSpecializedUsual::getTwoYearsLowestBit,bits+0.12*bits);
            wrapper3.le(CollegesSpecializedUsual::getTwoYearsLowestBit,bits+0.25*bits);
        }else if (bits<=100000){
            wrapper3.ge(CollegesSpecializedUsual::getLastYearLowestBit,bits+0.1*bits);
            wrapper3.le(CollegesSpecializedUsual::getLastYearLowestBit,bits+0.2*bits);
            wrapper3.ge(CollegesSpecializedUsual::getTwoYearsLowestBit,bits+0.1*bits);
            wrapper3.le(CollegesSpecializedUsual::getTwoYearsLowestBit,bits+0.2*bits);
        }else if (bits<=180000){
            wrapper3.ge(CollegesSpecializedUsual::getLastYearLowestBit,bits+0.08*bits);
            wrapper3.le(CollegesSpecializedUsual::getLastYearLowestBit,bits+0.13*bits);
            wrapper3.ge(CollegesSpecializedUsual::getTwoYearsLowestBit,bits+0.08*bits);
            wrapper3.le(CollegesSpecializedUsual::getTwoYearsLowestBit,bits+0.13*bits);
        }else if (bits<=270000){
            wrapper3.ge(CollegesSpecializedUsual::getLastYearLowestBit,bits+0.05*bits);
            wrapper3.le(CollegesSpecializedUsual::getLastYearLowestBit,bits+0.12*bits);
            wrapper3.ge(CollegesSpecializedUsual::getTwoYearsLowestBit,bits+0.05*bits);
            wrapper3.le(CollegesSpecializedUsual::getTwoYearsLowestBit,bits+0.12*bits);
        }else if (bits<=400000){
            wrapper3.ge(CollegesSpecializedUsual::getLastYearLowestBit,bits+0.15*bits);
            wrapper3.le(CollegesSpecializedUsual::getLastYearLowestBit,bits+0.3*bits);
            wrapper3.ge(CollegesSpecializedUsual::getTwoYearsLowestBit,bits+0.15*bits);
            wrapper3.le(CollegesSpecializedUsual::getTwoYearsLowestBit,bits+0.3*bits);
        }else if (bits<=550000){
            wrapper3.ge(CollegesSpecializedUsual::getLastYearLowestBit,bits+0.08*bits);
            wrapper3.le(CollegesSpecializedUsual::getLastYearLowestBit,bits+0.15*bits);
            wrapper3.ge(CollegesSpecializedUsual::getTwoYearsLowestBit,bits+0.08*bits);
            wrapper3.le(CollegesSpecializedUsual::getTwoYearsLowestBit,bits+0.15*bits);
        }else {
            wrapper3.ge(CollegesSpecializedUsual::getLastYearLowestBit,bits+0.15*bits);
            wrapper3.le(CollegesSpecializedUsual::getLastYearLowestBit,bits+0.3*bits);
            wrapper3.ge(CollegesSpecializedUsual::getTwoYearsLowestBit,bits+0.15*bits);
            wrapper3.le(CollegesSpecializedUsual::getTwoYearsLowestBit,bits+0.3*bits);
        }

        List<CollegesSpecializedUsual> threeCollegesSpecializedUsualList = collegesSpecializedUsualService.getBaseMapper().selectList(wrapper3);
        for (int i = 0; i < threeCollegesSpecializedUsualList.size(); i++) {
            MockProbabilityDTO mockProbabilityDTO = new MockProbabilityDTO();

            CollegesSpecialized collegesSpecialized = collegesSpecializedService.getBaseMapper().selectById(threeCollegesSpecializedUsualList.get(i).getSpecializedId());
            Colleges colleges = collegesService.getBaseMapper().selectById(collegesSpecialized.getCollegesId());
            if (colleges!=null&&!colleges.equals("")) {
                BeanUtils.copyProperties(threeCollegesSpecializedUsualList.get(i),mockProbabilityDTO);
                BeanUtils.copyProperties(colleges, mockProbabilityDTO);
                BeanUtils.copyProperties(collegesSpecialized,mockProbabilityDTO);
            }
            LambdaQueryWrapper<Mock> wrapper4 = new LambdaQueryWrapper<>();
            wrapper4.eq(Mock::getUserId,id);
            wrapper4.eq(Mock::getMockId,collegesSpecialized.getId());
            Mock mock = mockService.getBaseMapper().selectOne(wrapper4);
            if (mock!=null&&!mock.equals("")){
                mockProbabilityDTO.setSort(mock.getSort());
                mockProbabilityDTO.setCollection(1);
            }else {
                mockProbabilityDTO.setSort(0);
                mockProbabilityDTO.setCollection(0);
            }
            mockProbabilityDTO.setStrategy("保");
            if (colleges!=null&&!colleges.equals("")) {
                baoModelList.add(mockProbabilityDTO);
            }
        }

        List<MockProbabilityDTO> threeList = new ArrayList<>();
        Pattern pattern9 = Pattern.compile("不限");
        for (int i = 0; i < baoModelList.size(); i++) {
            Matcher matcher = pattern9.matcher(baoModelList.get(i).getSubjectRequest());
            if(matcher.find()){
                threeList.add(baoModelList.get(i));
            }
        }
        Pattern pattern10 = Pattern.compile(one);
        for (int i = 0; i < baoModelList.size(); i++) {
            Matcher matcher = pattern10.matcher(baoModelList.get(i).getSubjectRequest());
            if(matcher.find()){
                threeList.add(baoModelList.get(i));
            }
        }
        Pattern pattern11 = Pattern.compile(two);
        for (int i = 0; i < baoModelList.size(); i++) {
            Matcher matcher = pattern11.matcher(baoModelList.get(i).getSubjectRequest());
            if(matcher.find()){
                threeList.add(baoModelList.get(i));
            }
        }
        Pattern pattern12 = Pattern.compile(three);
        for (int i = 0; i < baoModelList.size(); i++) {
            Matcher matcher = pattern12.matcher(baoModelList.get(i).getSubjectRequest());
            if(matcher.find()){
                threeList.add(baoModelList.get(i));
            }
        }
        List<MockProbabilityDTO> baoList = threeList.stream().distinct().collect(Collectors.toList());
//        pageModel.setPages(pageOne.getPages()+pageTwo.getPages()+pageThree.getPages());
//        pageModel.setTotal(pageOne.getTotal()+pageTwo.getTotal()+pageThree.getTotal());
//        pageModel.setRecords(modelList);
//        pageModel.setCurrent(current);
//        pageModel.setSize(modelList.size());
        return R.ok().data("chongModelList",chongList).data("chong",chongList.size()).
                data("wenModelList",wenList).
                data("wen",wenList.size()).
                data("baoModelList",baoList).data("bao",baoList.size());

    }

//    @ApiOperation("智能推荐志愿")
//    @GetMapping("intelligent/{current}")
//    public R intelligent(@ApiParam(name = "current",value = "当前页",required = true) @PathVariable Long current,
////                         @RequestBody()
//                         HttpServletRequest request){
//        String token = request.getHeader("token");
//        String username = JwtTokenUtils.getUsername(token);
//        String user = redisTemplate.opsForValue().get("user" + username);
//        InfoVO infoVO = JSON.parseObject(user, InfoVO.class);
//        String id = infoVO.getId();
//        String scores = infoVO.getScores();
//        String bitsString = infoVO.getBits();
//        Integer bits = Integer.parseInt(bitsString);
//
//        Page<CollegesSpecializedUsual> page = new Page<>(current,10);
//        IPage<MockProbabilityDTO> pageModel = new Page<>();
//        List<MockProbabilityDTO> modelList = new ArrayList<>();
//        LambdaQueryWrapper<CollegesSpecializedUsual> wrapper = new LambdaQueryWrapper<>();
//        if (bits<=3000){
//            wrapper.ge(CollegesSpecializedUsual::getLastYearLowestBit,bits-0.2*bits);
//            wrapper.le(CollegesSpecializedUsual::getLastYearLowestBit,bits);
//            wrapper.ge(CollegesSpecializedUsual::getTwoYearsLowestBit,bits-0.2*bits);
//            wrapper.le(CollegesSpecializedUsual::getTwoYearsLowestBit,bits);
//        }else if (bits<=8000){
//            wrapper.ge(CollegesSpecializedUsual::getLastYearLowestBit,bits-0.15*bits);
//            wrapper.le(CollegesSpecializedUsual::getLastYearLowestBit,bits);
//            wrapper.ge(CollegesSpecializedUsual::getTwoYearsLowestBit,bits-0.15*bits);
//            wrapper.le(CollegesSpecializedUsual::getTwoYearsLowestBit,bits);
//        }else {
//            wrapper.ge(CollegesSpecializedUsual::getLastYearLowestBit,bits-0.05*bits);
//            wrapper.le(CollegesSpecializedUsual::getLastYearLowestBit,bits);
//            wrapper.ge(CollegesSpecializedUsual::getTwoYearsLowestBit,bits-0.05*bits);
//            wrapper.le(CollegesSpecializedUsual::getTwoYearsLowestBit,bits);
//        }
//
//        Page<CollegesSpecializedUsual> pageOne = collegesSpecializedUsualService.getBaseMapper().selectPage(page, wrapper);
//
//        for (int i = 0; i < pageOne.getRecords().size(); i++) {
//            MockProbabilityDTO mockProbabilityDTO = new MockProbabilityDTO();
//            CollegesSpecialized collegesSpecialized = collegesSpecializedService.getBaseMapper().selectById(pageOne.getRecords().get(i).getSpecializedId());
//            Colleges colleges = collegesService.getBaseMapper().selectById(collegesSpecialized.getCollegesId());
//            if (colleges!=null&&!colleges.equals("")) {
//                BeanUtils.copyProperties(pageOne.getRecords().get(i),mockProbabilityDTO);
//                BeanUtils.copyProperties(colleges, mockProbabilityDTO);
//                BeanUtils.copyProperties(collegesSpecialized,mockProbabilityDTO);
//            }
//            LambdaQueryWrapper<Mock> wrapper5 = new LambdaQueryWrapper<>();
//            wrapper5.eq(Mock::getUserId,id);
//            wrapper5.eq(Mock::getMockId,collegesSpecialized.getId());
//            Mock mock = mockService.getBaseMapper().selectOne(wrapper5);
//            if (mock!=null&&!mock.equals("")){
//                mockProbabilityDTO.setSort(mock.getSort());
//                mockProbabilityDTO.setCollection(1);
//            }else {
//                mockProbabilityDTO.setSort(0);
//                mockProbabilityDTO.setCollection(0);
//            }
//            mockProbabilityDTO.setStrategy("冲");
//            if (colleges!=null&&!colleges.equals("")) {
//                modelList.add(mockProbabilityDTO);
//            }
//        }
//
//        LambdaQueryWrapper<CollegesSpecializedUsual> wrapper2 = new LambdaQueryWrapper<>();
//        if (bits<=3000){
//            wrapper2.ge(CollegesSpecializedUsual::getLastYearLowestBit,bits);
//            wrapper2.le(CollegesSpecializedUsual::getLastYearLowestBit,bits+0.3*bits);
//            wrapper2.ge(CollegesSpecializedUsual::getTwoYearsLowestBit,bits);
//            wrapper2.le(CollegesSpecializedUsual::getTwoYearsLowestBit,bits+0.3*bits);
//        }else if (bits<=8000){
//            wrapper2.ge(CollegesSpecializedUsual::getLastYearLowestBit,bits);
//            wrapper2.le(CollegesSpecializedUsual::getLastYearLowestBit,bits+0.15*bits);
//            wrapper2.ge(CollegesSpecializedUsual::getTwoYearsLowestBit,bits);
//            wrapper2.le(CollegesSpecializedUsual::getTwoYearsLowestBit,bits+0.15*bits);
//        }else {
//            wrapper2.ge(CollegesSpecializedUsual::getLastYearLowestBit,bits);
//            wrapper2.le(CollegesSpecializedUsual::getLastYearLowestBit,bits+0.1*bits);
//            wrapper2.ge(CollegesSpecializedUsual::getTwoYearsLowestBit,bits);
//            wrapper2.le(CollegesSpecializedUsual::getTwoYearsLowestBit,bits+0.1*bits);
//        }
//
//        Page<CollegesSpecializedUsual> pageTwo = collegesSpecializedUsualService.getBaseMapper().selectPage(page, wrapper2);
//        for (int i = 0; i < pageTwo.getRecords().size(); i++) {
//            MockProbabilityDTO mockProbabilityDTO = new MockProbabilityDTO();
//
//            CollegesSpecialized collegesSpecialized = collegesSpecializedService.getBaseMapper().selectById(pageTwo.getRecords().get(i).getSpecializedId());
//            Colleges colleges = collegesService.getBaseMapper().selectById(collegesSpecialized.getCollegesId());
//            if (colleges!=null&&!colleges.equals("")) {
//                BeanUtils.copyProperties(pageTwo.getRecords().get(i),mockProbabilityDTO);
//                BeanUtils.copyProperties(colleges, mockProbabilityDTO);
//                BeanUtils.copyProperties(collegesSpecialized,mockProbabilityDTO);
//            }
//            LambdaQueryWrapper<Mock> wrapper6 = new LambdaQueryWrapper<>();
//            wrapper6.eq(Mock::getUserId,id);
//            wrapper6.eq(Mock::getMockId,collegesSpecialized.getId());
//            Mock mock = mockService.getBaseMapper().selectOne(wrapper6);
//            if (mock!=null&&!mock.equals("")){
//                mockProbabilityDTO.setSort(mock.getSort());
//                mockProbabilityDTO.setCollection(1);
//            }else {
//                mockProbabilityDTO.setSort(0);
//                mockProbabilityDTO.setCollection(0);
//            }
//            mockProbabilityDTO.setStrategy("稳");
//            if (colleges!=null&&!colleges.equals("")) {
//                modelList.add(mockProbabilityDTO);
//            }
//        }
//
//        LambdaQueryWrapper<CollegesSpecializedUsual> wrapper3 = new LambdaQueryWrapper<>();
//        if (bits<=3000){
//            wrapper3.ge(CollegesSpecializedUsual::getLastYearLowestBit,bits+0.3*bits);
//            wrapper3.le(CollegesSpecializedUsual::getLastYearLowestBit,bits+0.8*bits);
//            wrapper3.ge(CollegesSpecializedUsual::getTwoYearsLowestBit,bits+0.3*bits);
//            wrapper3.le(CollegesSpecializedUsual::getTwoYearsLowestBit,bits+0.8*bits);
//        }else if (bits<=8000){
//            wrapper3.ge(CollegesSpecializedUsual::getLastYearLowestBit,bits+0.15*bits);
//            wrapper3.le(CollegesSpecializedUsual::getLastYearLowestBit,bits+0.35*bits);
//            wrapper3.ge(CollegesSpecializedUsual::getTwoYearsLowestBit,bits+0.15*bits);
//            wrapper3.le(CollegesSpecializedUsual::getTwoYearsLowestBit,bits+0.35*bits);
//        }else {
//            wrapper3.ge(CollegesSpecializedUsual::getLastYearLowestBit,bits+0.1*bits);
//            wrapper3.le(CollegesSpecializedUsual::getLastYearLowestBit,bits+0.15*bits);
//            wrapper3.ge(CollegesSpecializedUsual::getTwoYearsLowestBit,bits+0.1*bits);
//            wrapper3.le(CollegesSpecializedUsual::getTwoYearsLowestBit,bits+0.15*bits);
//        }
//
//        Page<CollegesSpecializedUsual> pageThree = collegesSpecializedUsualService.getBaseMapper().selectPage(page, wrapper3);
//        for (int i = 0; i < pageThree.getRecords().size(); i++) {
//            MockProbabilityDTO mockProbabilityDTO = new MockProbabilityDTO();
//
//            CollegesSpecialized collegesSpecialized = collegesSpecializedService.getBaseMapper().selectById(pageThree.getRecords().get(i).getSpecializedId());
//            Colleges colleges = collegesService.getBaseMapper().selectById(collegesSpecialized.getCollegesId());
//            if (colleges!=null&&!colleges.equals("")) {
//                BeanUtils.copyProperties(pageThree.getRecords().get(i),mockProbabilityDTO);
//                BeanUtils.copyProperties(colleges, mockProbabilityDTO);
//                BeanUtils.copyProperties(collegesSpecialized,mockProbabilityDTO);
//            }
//            LambdaQueryWrapper<Mock> wrapper4 = new LambdaQueryWrapper<>();
//            wrapper4.eq(Mock::getUserId,id);
//            wrapper4.eq(Mock::getMockId,collegesSpecialized.getId());
//            Mock mock = mockService.getBaseMapper().selectOne(wrapper4);
//            if (mock!=null&&!mock.equals("")){
//                mockProbabilityDTO.setSort(mock.getSort());
//                mockProbabilityDTO.setCollection(1);
//            }else {
//                mockProbabilityDTO.setSort(0);
//                mockProbabilityDTO.setCollection(0);
//            }
//            mockProbabilityDTO.setStrategy("保");
//            if (colleges!=null&&!colleges.equals("")) {
//                modelList.add(mockProbabilityDTO);
//            }
//        }
//        pageModel.setPages(pageOne.getPages()+pageTwo.getPages()+pageThree.getPages());
//        pageModel.setTotal(pageOne.getTotal()+pageTwo.getTotal()+pageThree.getTotal());
//        pageModel.setRecords(modelList);
//        pageModel.setCurrent(current);
//        pageModel.setSize(modelList.size());
//        return R.ok().data("pageModel",pageModel).data("chong",pageOne.getTotal()).data("wen",pageTwo.getTotal()).data("bao",pageThree.getTotal());
//
//    }
//
//    @ApiOperation("智能推荐志愿")
//    @GetMapping("intelligent/{current}")
//    public R intelligent(@ApiParam(name = "current",value = "当前页",required = true) @PathVariable Long current,
//                         HttpServletRequest request){
//        String token = request.getHeader("token");
//        String username = JwtTokenUtils.getUsername(token);
//        String user = redisTemplate.opsForValue().get("user" + username);
//        InfoVO infoVO = JSON.parseObject(user, InfoVO.class);
//        String id = infoVO.getId();
//        String scores = infoVO.getScores();
//        String bitsString = infoVO.getBits();
//        Integer bits = Integer.parseInt(bitsString);
//
//        Page<CollegesSpecializedUsual> page = new Page<>(current,10);
//        IPage<MockProbabilityDTO> pageModel = new Page<>();
//        List<MockProbabilityDTO> modelList = new ArrayList<>();
//
//        LambdaQueryWrapper<CollegesSpecializedUsual> wrapper = new LambdaQueryWrapper<>();
//        wrapper.ge(CollegesSpecializedUsual::getLastYearLowestBit,bits-0.05*bits);
//        wrapper.le(CollegesSpecializedUsual::getLastYearLowestBit,bits);
//        wrapper.ge(CollegesSpecializedUsual::getTwoYearsLowestBit,bits-0.05*bits);
//        wrapper.le(CollegesSpecializedUsual::getTwoYearsLowestBit,bits);
//        Page<CollegesSpecializedUsual> pageOne = collegesSpecializedUsualService.getBaseMapper().selectPage(page, wrapper);
//        for (int i = 0; i < pageOne.getRecords().size(); i++) {
//            MockProbabilityDTO mockProbabilityDTO = new MockProbabilityDTO();
//
//            CollegesSpecialized collegesSpecialized = collegesSpecializedService.getBaseMapper().selectById(pageOne.getRecords().get(i).getSpecializedId());
//            Colleges colleges = collegesService.getBaseMapper().selectById(collegesSpecialized.getCollegesId());
//            if (colleges!=null&&!colleges.equals("")) {
//                BeanUtils.copyProperties(pageOne.getRecords().get(i),mockProbabilityDTO);
//                BeanUtils.copyProperties(colleges, mockProbabilityDTO);
//                BeanUtils.copyProperties(collegesSpecialized,mockProbabilityDTO);
//            }
//            LambdaQueryWrapper<Mock> wrapper3 = new LambdaQueryWrapper<>();
//            wrapper3.eq(Mock::getUserId,id);
//            wrapper3.eq(Mock::getMockId,collegesSpecialized.getId());
//            Mock mock = mockService.getBaseMapper().selectOne(wrapper3);
//            if (mock!=null&&!mock.equals("")){
//                mockProbabilityDTO.setSort(mock.getSort());
//                mockProbabilityDTO.setCollection(1);
//            }else {
//                mockProbabilityDTO.setSort(0);
//                mockProbabilityDTO.setCollection(0);
//            }
//            mockProbabilityDTO.setStrategy("冲");
//            if (colleges!=null&&!colleges.equals("")) {
//                modelList.add(mockProbabilityDTO);
//            }
//        }
//
//        pageModel.setPages(pageOne.getPages());
//        LambdaQueryWrapper<CollegesSpecializedUsual> wrapper2 = new LambdaQueryWrapper<>();
//        wrapper2.ge(CollegesSpecializedUsual::getLastYearLowestBit,bits);
//        wrapper2.le(CollegesSpecializedUsual::getLastYearLowestBit,bits+0.1*bits);
//        wrapper2.ge(CollegesSpecializedUsual::getTwoYearsLowestBit,bits);
//        wrapper2.le(CollegesSpecializedUsual::getTwoYearsLowestBit,bits+0.1*bits);
//        Page<CollegesSpecializedUsual> pageTwo = collegesSpecializedUsualService.getBaseMapper().selectPage(page, wrapper2);
//        for (int i = 0; i < pageTwo.getRecords().size(); i++) {
//            MockProbabilityDTO mockProbabilityDTO = new MockProbabilityDTO();
//
//            CollegesSpecialized collegesSpecialized = collegesSpecializedService.getBaseMapper().selectById(pageTwo.getRecords().get(i).getSpecializedId());
//            Colleges colleges = collegesService.getBaseMapper().selectById(collegesSpecialized.getCollegesId());
//            if (colleges!=null&&!colleges.equals("")) {
//                BeanUtils.copyProperties(pageTwo.getRecords().get(i),mockProbabilityDTO);
//                BeanUtils.copyProperties(colleges, mockProbabilityDTO);
//                BeanUtils.copyProperties(collegesSpecialized,mockProbabilityDTO);
//            }
//            LambdaQueryWrapper<Mock> wrapper3 = new LambdaQueryWrapper<>();
//            wrapper3.eq(Mock::getUserId,id);
//            wrapper3.eq(Mock::getMockId,collegesSpecialized.getId());
//            Mock mock = mockService.getBaseMapper().selectOne(wrapper3);
//            if (mock!=null&&!mock.equals("")){
//                mockProbabilityDTO.setSort(mock.getSort());
//                mockProbabilityDTO.setCollection(1);
//            }else {
//                mockProbabilityDTO.setSort(0);
//                mockProbabilityDTO.setCollection(0);
//            }
//            mockProbabilityDTO.setStrategy("稳");
//            if (colleges!=null&&!colleges.equals("")) {
//                modelList.add(mockProbabilityDTO);
//            }
//        }
//
//        LambdaQueryWrapper<CollegesSpecializedUsual> wrapper3 = new LambdaQueryWrapper<>();
//        wrapper3.ge(CollegesSpecializedUsual::getLastYearLowestBit,+0.1*bits);
//        wrapper3.le(CollegesSpecializedUsual::getLastYearLowestBit,bits+0.15*bits);
//        wrapper3.ge(CollegesSpecializedUsual::getTwoYearsLowestBit,+0.1*bits);
//        wrapper3.le(CollegesSpecializedUsual::getTwoYearsLowestBit,bits+0.15*bits);
//        Page<CollegesSpecializedUsual> pageThree = collegesSpecializedUsualService.getBaseMapper().selectPage(page, wrapper3);
//        for (int i = 0; i < pageThree.getRecords().size(); i++) {
//            MockProbabilityDTO mockProbabilityDTO = new MockProbabilityDTO();
//
//            CollegesSpecialized collegesSpecialized = collegesSpecializedService.getBaseMapper().selectById(pageThree.getRecords().get(i).getSpecializedId());
//            Colleges colleges = collegesService.getBaseMapper().selectById(collegesSpecialized.getCollegesId());
//            if (colleges!=null&&!colleges.equals("")) {
//                BeanUtils.copyProperties(pageThree.getRecords().get(i),mockProbabilityDTO);
//                BeanUtils.copyProperties(colleges, mockProbabilityDTO);
//                BeanUtils.copyProperties(collegesSpecialized,mockProbabilityDTO);
//            }
//            LambdaQueryWrapper<Mock> wrapper4 = new LambdaQueryWrapper<>();
//            wrapper4.eq(Mock::getUserId,id);
//            wrapper4.eq(Mock::getMockId,collegesSpecialized.getId());
//            Mock mock = mockService.getBaseMapper().selectOne(wrapper4);
//            if (mock!=null&&!mock.equals("")){
//                mockProbabilityDTO.setSort(mock.getSort());
//                mockProbabilityDTO.setCollection(1);
//            }else {
//                mockProbabilityDTO.setSort(0);
//                mockProbabilityDTO.setCollection(0);
//            }
//            mockProbabilityDTO.setStrategy("保");
//            if (colleges!=null&&!colleges.equals("")) {
//                modelList.add(mockProbabilityDTO);
//            }
//        }
//
//        pageModel.setTotal(modelList.size());
//        pageModel.setRecords(modelList);
//        pageModel.setCurrent(current);
//        pageModel.setSize(10);
//        return R.ok().data("pageModel",pageModel);
//
//    }

}

