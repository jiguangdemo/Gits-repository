package com.sdau.nemt.service.volunteer.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sdau.nemt.common.base.result.R;
//import com.sdau.nemt.service.user.entity.vo.InfoVO;
import com.sdau.nemt.service.volunteer.entity.Colleges;
import com.sdau.nemt.service.volunteer.entity.CollegesSpecialized;
import com.sdau.nemt.service.volunteer.entity.UserColleges;
import com.sdau.nemt.service.volunteer.entity.vo.InfoVO;
import com.sdau.nemt.service.volunteer.service.CollegesService;
import com.sdau.nemt.service.volunteer.service.UserCollegesService;
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
@Api(tags = "用户对院校的收藏")
@RestController
@RequestMapping("/volunteer/user-colleges")
public class UserCollegesController {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private CollegesService collegesService;

    @Autowired
    private UserCollegesService userCollegesService;

    @ApiOperation("收藏该院校")
    @GetMapping("collection/{collegesId}")
    public R collection(@ApiParam(name = "collegesId",value = "院校id") @PathVariable String collegesId,
                        HttpServletRequest request){
        String token = request.getHeader("token");
        String username = JwtTokenUtils.getUsername(token);
        String user = redisTemplate.opsForValue().get("user" + username);
        InfoVO infoVO = JSON.parseObject(user, InfoVO.class);
        String id = infoVO.getId();
        LambdaQueryWrapper<UserColleges> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserColleges::getUserId,id);
        wrapper.eq(UserColleges::getCollegesId,collegesId);
        UserColleges selectOne = userCollegesService.getBaseMapper().selectOne(wrapper);
        if (selectOne!=null&&!selectOne.equals("")){
            return R.error().message("您已经收藏该院校");
        }
        UserColleges userColleges = new UserColleges();
        userColleges.setUserId(id);
        userColleges.setCollegesId(collegesId);
        userCollegesService.getBaseMapper().insert(userColleges);
        return R.ok().message("收藏成功");

    }

    @ApiOperation("查看院校收藏列表")
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
        LambdaQueryWrapper<UserColleges> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserColleges::getUserId,id);
        List<UserColleges> userColleges = userCollegesService.getBaseMapper().selectList(wrapper);
        List<String> idList = new ArrayList<>();
        for (int i = 0; i < userColleges.size(); i++) {
            idList.add(userColleges.get(i).getCollegesId());
        }
        List<Colleges> colleges = collegesService.getBaseMapper().selectBatchIds(idList);
        IPage pageModel = new Page(current,limit,colleges.size());
        pageModel.setRecords(colleges);
        Integer pages = 0;
        if(colleges.size()%limit==0){
            pages = Math.toIntExact(colleges.size() / limit);
            System.out.println("pages1"+pages);
        }else {
            pages = Math.toIntExact(colleges.size() / limit) + 1;
            System.out.println("pages2"+pages);
        }
        if(current<pages){
            List<Colleges> list = new ArrayList<>();
            for(int i = (int) ((current-1)*limit); i<((current-1)*limit+limit); i++){
                list.add(colleges.get(i));
            }
            pageModel.setRecords(list);
            return R.ok().data("pageModel",pageModel);
        }else {
            List<Colleges> list = new ArrayList<>();
            for(int i = (int) ((current-1)*limit); i<colleges.size(); i++){
                list.add(colleges.get(i));
            }
            pageModel.setRecords(list);
            return R.ok().data("pageModel",pageModel);
        }
    }

    @ApiOperation("删除已经收藏的院校")
    @DeleteMapping("remove/{collegesId}")
    public R remove(@ApiParam(name = "collegesId",value = "院校id") @PathVariable String collegesId,
                    HttpServletRequest request){
        String token = request.getHeader("token");
        String username = JwtTokenUtils.getUsername(token);
        String user = redisTemplate.opsForValue().get("user" + username);
        InfoVO infoVO = JSON.parseObject(user, InfoVO.class);
        String id = infoVO.getId();
        UserColleges userColleges = new UserColleges();
        userColleges.setUserId(id);
        userColleges.setCollegesId(collegesId);
        LambdaQueryWrapper<UserColleges> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserColleges::getUserId,id);
        wrapper.eq(UserColleges::getCollegesId,collegesId);
        UserColleges selectOne = userCollegesService.getBaseMapper().selectOne(wrapper);
        if (selectOne==null||selectOne.equals("")){
            return R.error().message("删除错误");
        }
        userCollegesService.getBaseMapper().delete(wrapper);
//        userCollegesService.getBaseMapper().insert(userColleges);
        return R.ok().message("删除收藏成功");
    }

}

