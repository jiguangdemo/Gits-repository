package com.sdau.nemt.service.information.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sdau.nemt.common.base.result.R;
import com.sdau.nemt.service.information.entity.Teacher;
import com.sdau.nemt.service.information.entity.dto.TeacherDTO;
import com.sdau.nemt.service.information.entity.vo.InfoVO;
import com.sdau.nemt.service.information.entity.vo.TeacherQueryVO;
import com.sdau.nemt.service.information.entity.vo.TeacherVO;
import com.sdau.nemt.service.information.feign.InfoFeignService;
import com.sdau.nemt.service.information.service.TeacherService;
import com.sdau.nemt.service.information.util.JwtTokenUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author
 * @since 2023-08-10
 */
@Api(tags = "咨询师")
@RestController
@RequestMapping("/information/teacher")
public class TeacherController {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private TeacherService teacherService;

    @ApiOperation("添加咨询师")
    @PostMapping("save")
    public R saveTeacher(@RequestBody TeacherQueryVO teacherQueryVO){
        if (teacherQueryVO.equals("")||teacherQueryVO==null){
            return R.error();
        }
        Teacher teacher = new Teacher();
        BeanUtils.copyProperties(teacherQueryVO,teacher);
        int i = teacherService.getBaseMapper().insert(teacher);
        if (i!=1){
            return R.error();
        }
        return R.ok();
    }

    @ApiOperation("删除讲师")
    @DeleteMapping("delete/{id}")
    public R deletedTeacher(@PathVariable String id){
        int i = teacherService.getBaseMapper().deleteById(id);
        if (i!=1){
            return R.error();
        }
        return R.ok();
    }

    @ApiOperation("修改讲师信息")
    @PutMapping("update")
    public R updateTeacher(@RequestBody TeacherDTO teacherDTO){
        if (teacherDTO.equals("")||teacherDTO==null){
            return R.error();
        }
        Teacher teacher = new Teacher();
        BeanUtils.copyProperties(teacherDTO,teacher);
        int i = teacherService.getBaseMapper().updateById(teacher);
        if (i!=1){
            return R.error();
        }
        return R.ok();
    }

    @ApiOperation("获取咨询师信息")
    @PostMapping("page/{current}/{limit}")
    public R getPage(@ApiParam("当前页") @PathVariable Long current,
                     @ApiParam("每页多少条")@PathVariable Long limit,
                     @ApiParam(name = "teacherVO",value = "试图层条件" , required = false) @RequestBody(required = false) TeacherVO teacherVO){
        Page<Teacher> page = new Page<>(current,limit);
        if(teacherVO!=null) {
            IPage<TeacherDTO> pageModel = teacherService.findQueryPage(page, teacherVO);
            return R.ok().data("pageModel",pageModel);
        }else {
            Page<Teacher> pageModel = teacherService.getBaseMapper().selectPage(page,null);
            List<TeacherDTO> list = new ArrayList<>();
            List<Teacher> records = pageModel.getRecords();
            for(int i =0 ;i<records.size(); i++){
                TeacherDTO teacherDTO = new TeacherDTO();
                BeanUtils.copyProperties(records.get(i),teacherDTO);
                list.add(teacherDTO);
            }
            IPage<TeacherDTO> teacherDTOIPage = new Page<>();
            BeanUtils.copyProperties(pageModel,teacherDTOIPage);
            teacherDTOIPage.setRecords(list);
            return R.ok().data("pageModel",teacherDTOIPage);
        }
    }

    @ApiOperation("获取咨询师联系方式")
    @GetMapping("info/{id}")
    public R getInfo(@ApiParam("咨询师id")@PathVariable String id,
                     HttpServletRequest request){
        //判断用户是否为会员
        String token = request.getHeader("token");
        System.out.println("token: "+token);
        String username = JwtTokenUtils.getUsername(token);
        String user = redisTemplate.opsForValue().get("user" + username);
        InfoVO infoVO = JSON.parseObject(user, InfoVO.class);
        System.out.println(user);
        Integer identity = infoVO.getIdentity();

        Teacher teacher = teacherService.getBaseMapper().selectById(id);
        if (teacher.getLevel()==1){
            return R.ok().data("teacher",teacher);
        }else {
            if (identity.equals("1")){
                return R.ok().data("teacher",teacher);
            }else {
                return R.error().message("该志愿师为VIP专享");
            }
        }
    }
}

