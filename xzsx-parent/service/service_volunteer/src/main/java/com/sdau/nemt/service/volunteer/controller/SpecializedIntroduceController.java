package com.sdau.nemt.service.volunteer.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sdau.nemt.common.base.result.R;
import com.sdau.nemt.service.volunteer.entity.Specialized;
import com.sdau.nemt.service.volunteer.entity.SpecializedIntroduce;
import com.sdau.nemt.service.volunteer.entity.UserSpecialized;
import com.sdau.nemt.service.volunteer.entity.dto.SpecializedIntroduceDTO;
import com.sdau.nemt.service.volunteer.entity.vo.InfoVO;
import com.sdau.nemt.service.volunteer.service.SpecializedClassifyService;
import com.sdau.nemt.service.volunteer.service.SpecializedIntroduceService;
import com.sdau.nemt.service.volunteer.service.SpecializedService;
import com.sdau.nemt.service.volunteer.service.UserSpecializedService;
import com.sdau.nemt.service.volunteer.util.JwtTokenUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 专业介绍 前端控制器
 * </p>
 *
 * @author
 * @since 2023-08-11
 */
@Api(tags = "专业详情")
@RestController
@RequestMapping("/volunteer/specialized")
public class SpecializedIntroduceController {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private SpecializedService specializedService;

    @Autowired
    private UserSpecializedService userSpecializedService;

    @Autowired
    private SpecializedClassifyService specializedClassifyService;

    @Autowired
    private SpecializedIntroduceService specializedIntroduceService;

    @ApiOperation("专业介绍")
    @GetMapping("introduce/{specializedId}")
    public R getIntroduce(@ApiParam("专业id") @PathVariable String specializedId,
                          HttpServletRequest request){
        String token = request.getHeader("token");
        String username = JwtTokenUtils.getUsername(token);
        String user = redisTemplate.opsForValue().get("user" + username);
        InfoVO infoVO = JSON.parseObject(user, InfoVO.class);
        String id = infoVO.getId();
        LambdaQueryWrapper<SpecializedIntroduce> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SpecializedIntroduce::getSpecializedId,specializedId);
        SpecializedIntroduce introduce = specializedIntroduceService.getBaseMapper().selectOne(wrapper);
//        LambdaQueryWrapper<Specialized> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(Specialized::getId,id);
        Specialized specialized = specializedService.getBaseMapper().selectById(specializedId);
        SpecializedIntroduceDTO specializedIntroduceDTO = new SpecializedIntroduceDTO();
        BeanUtils.copyProperties(introduce,specializedIntroduceDTO);
        BeanUtils.copyProperties(specialized,specializedIntroduceDTO);
        specializedIntroduceDTO.setId(specializedId);
        LambdaQueryWrapper<UserSpecialized> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserSpecialized::getUserId,id);
        queryWrapper.eq(UserSpecialized::getSpecializedId,specializedId);
        UserSpecialized userSpecialized = userSpecializedService.getBaseMapper().selectOne(queryWrapper);
        if (userSpecialized==null||userSpecialized.equals("")){
            specializedIntroduceDTO.setCollection(0);
        }else {
            specializedIntroduceDTO.setCollection(1);
        }

        return R.ok().data("introduce",specializedIntroduceDTO);
    }

}

