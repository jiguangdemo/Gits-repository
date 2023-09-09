package com.sdau.nemt.service.volunteer.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sdau.nemt.common.base.result.R;
import com.sdau.nemt.service.volunteer.entity.Colleges;
import com.sdau.nemt.service.volunteer.entity.CollegesSpecialized;
import com.sdau.nemt.service.volunteer.entity.UserColleges;
import com.sdau.nemt.service.volunteer.entity.UserSpecialized;
import com.sdau.nemt.service.volunteer.entity.vo.InfoVO;
import com.sdau.nemt.service.volunteer.service.CollegesSpecializedService;
import com.sdau.nemt.service.volunteer.service.UserCollegesService;
import com.sdau.nemt.service.volunteer.service.UserSpecializedService;
import com.sdau.nemt.service.volunteer.util.JwtTokenUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author
 * @since 2023-08-11
 */
@Api(tags = "用户对专业的收藏")
@RestController
@RequestMapping("/volunteer/user-specialized")
public class UserSpecializedController {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private UserSpecializedService userSpecializedService;

    @Autowired
    private CollegesSpecializedService collegesSpecializedService;

    @ApiOperation("收藏该院校开设的该专业")
    @GetMapping("collection/{specializedId}")
    public R collection(@ApiParam(name = "specializedId",value = "院校开设专业的id") @PathVariable String specializedId,
                        HttpServletRequest request){
        String token = request.getHeader("token");
        String username = JwtTokenUtils.getUsername(token);
        String user = redisTemplate.opsForValue().get("user" + username);
        InfoVO infoVO = JSON.parseObject(user, InfoVO.class);
        String id = infoVO.getId();
        LambdaQueryWrapper<UserSpecialized> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserSpecialized::getUserId,id);
        wrapper.eq(UserSpecialized::getSpecializedId,specializedId);
        UserSpecialized selectOne = userSpecializedService.getBaseMapper().selectOne(wrapper);
        if (selectOne!=null&&!selectOne.equals("")){
            return R.error().message("已经收藏该专业");
        }
        UserSpecialized userSpecialized = new UserSpecialized();
        userSpecialized.setUserId(id);
        userSpecialized.setSpecializedId(specializedId);
        userSpecializedService.getBaseMapper().insert(userSpecialized);
        return R.ok().message("收藏成功");
    }

    @ApiOperation("查看专业收藏列表")
    @GetMapping("list/{current}/{limit}")
    public R list(HttpServletRequest request,
                  @ApiParam(name = "current", value = "当前页码") @PathVariable Long current,
                  @ApiParam(name = "limit", value = "每页数量") @PathVariable  Long limit
    ){
        String token = request.getHeader("token");
        String username = JwtTokenUtils.getUsername(token);
        String user = redisTemplate.opsForValue().get("user" + username);
        InfoVO infoVO = JSON.parseObject(user, InfoVO.class);
        String id = infoVO.getId();
        LambdaQueryWrapper<UserSpecialized> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserSpecialized::getUserId,id);
        List<UserSpecialized> userSpecializeds = userSpecializedService.getBaseMapper().selectList(wrapper);
        List<String> idList = new ArrayList<>();
        for (int i = 0; i < userSpecializeds.size(); i++) {
            idList.add(userSpecializeds.get(i).getSpecializedId());
        }
        List<CollegesSpecialized> collegesSpecializedList = collegesSpecializedService.listByIds(idList);
//        List<Colleges> colleges = collegesService.getBaseMapper().selectBatchIds(idList);
//        IPage pageModel = new Page(current,limit,colleges.size());
//        pageModel.setRecords(colleges);
        IPage<CollegesSpecialized> pageModel = new Page<>(current,limit,collegesSpecializedList.size());
        Integer pages = 0;
        if(collegesSpecializedList.size()%limit==0){
            pages = Math.toIntExact(collegesSpecializedList.size() / limit);
            System.out.println("pages1"+pages);
        }else {
            pages = Math.toIntExact(collegesSpecializedList.size() / limit) + 1;
            System.out.println("pages2"+pages);
        }
        if(current<pages){
            List<CollegesSpecialized> list = new ArrayList<>();
            for(int i = (int) ((current-1)*limit); i<((current-1)*limit+limit); i++){
                list.add(collegesSpecializedList.get(i));
            }
            pageModel.setRecords(list);
            return R.ok().data("pageModel",pageModel);
        }else {
            List<CollegesSpecialized> list = new ArrayList<>();
            for(int i = (int) ((current-1)*limit); i<collegesSpecializedList.size(); i++){
                list.add(collegesSpecializedList.get(i));
            }
            pageModel.setRecords(list);
            return R.ok().data("pageModel",pageModel);
        }
    }
    @ApiOperation("删除收藏该院校开设的该专业")
    @DeleteMapping("collection/{specializedId}")
    public R remove(@ApiParam(name = "specializedId",value = "院校开设专业的id") @PathVariable String specializedId,
                        HttpServletRequest request){
        String token = request.getHeader("token");
        String username = JwtTokenUtils.getUsername(token);
        String user = redisTemplate.opsForValue().get("user" + username);
        InfoVO infoVO = JSON.parseObject(user, InfoVO.class);
        String id = infoVO.getId();
        LambdaQueryWrapper<UserSpecialized> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserSpecialized::getUserId,id);
        wrapper.eq(UserSpecialized::getSpecializedId,specializedId);
        UserSpecialized selectOne = userSpecializedService.getBaseMapper().selectOne(wrapper);
        if (selectOne==null||selectOne.equals("")){
            return R.error().message("不存在该记录");
        }
        String oneId = selectOne.getId();
        userSpecializedService.getBaseMapper().deleteById(oneId);
//        UserSpecialized userSpecialized = new UserSpecialized();
//        userSpecialized.setUserId(id);
//        userSpecialized.setSpecializedId(specializedId);
//        userSpecializedService.getBaseMapper().insert(userSpecialized);
        return R.ok().message("删除收藏成功");
    }


}

