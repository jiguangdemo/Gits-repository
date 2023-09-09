package com.sdau.nemt.service.user.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 关注院校 前端控制器
 * </p>
 *
 * @author
 * @since 2023-08-11
 */
//@Api(tags = "用户院校")
@CrossOrigin
@RestController
@RequestMapping("/user/colleges")
public class CollegesController {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

//    @Autowired
//    private CollegesService collegesService;
//
//    @Qualifier
//    private CollegesFeignService collegesFeignService;
//
//    @ApiOperation("收藏该院校")
//    @GetMapping("collection/{collegesId}")
//    public R collection(@ApiParam(name = "collegesId",value = "院校id") @PathVariable String collegesId,
//                        HttpServletRequest request){
//        String token = request.getHeader("token");
//        String username = JwtTokenUtils.getUsername(token);
//        String user = redisTemplate.opsForValue().get("user" + username);
//        InfoVO infoVO = JSON.parseObject(user, InfoVO.class);
//        String id = infoVO.getId();
//        Colleges colleges = new Colleges();
//        colleges.setId(id);
//        colleges.setCollegesId(collegesId);
//        collegesService.getBaseMapper().insert(colleges);
//        return R.ok().message("收藏成功");
//    }

//    @ApiOperation("查看收藏的院校列表")
//    @GetMapping("list")
//    public R list(HttpServletRequest request){
//        String token = request.getHeader("token");
//        String username = JwtTokenUtils.getUsername(token);
//        String user = redisTemplate.opsForValue().get("user" + username);
//        InfoVO infoVO = JSON.parseObject(user, InfoVO.class);
//        String id = infoVO.getId();
//        System.out.println("id:"+id);
//        QueryWrapper<Colleges> wrapper = new QueryWrapper<>();
//        wrapper.eq("id",id);
//        List<Colleges> colleges = collegesService.getBaseMapper().selectList(wrapper);
//        if(colleges==null){
//            return R.ok().data("colleges", null);
//        }
//        List<String> listId = new ArrayList<>();
//        for (int i = 0; i < colleges.size(); i++) {
//            listId.add(colleges.get(i).getId());
//        }
//        List<Object> list = collegesFeignService.list(listId);
//        return R.ok().data("list",list);
//
//    }

}

