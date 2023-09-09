package com.sdau.nemt.service.user.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sdau.nemt.common.base.result.R;
import com.sdau.nemt.common.base.util.ExceptionUtils;
import com.sdau.nemt.service.user.entity.Grades;
import com.sdau.nemt.service.user.entity.Info;
import com.sdau.nemt.service.user.entity.form.GradesForm;
import com.sdau.nemt.service.user.entity.vo.FindPasswordVO;
import com.sdau.nemt.service.user.entity.vo.InfoVO;
import com.sdau.nemt.service.user.entity.vo.RegisterVO;
import com.sdau.nemt.service.user.service.GradesService;
import com.sdau.nemt.service.user.service.InfoService;
import com.sdau.nemt.service.user.util.JwtTokenUtils;
import com.sdau.nemt.service.user.util.MailUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 用户信息 前端控制器
 * </p>
 *
 * @author
 * @since 2023-08-11
 */
@Api(tags = "用户信息(含用户注册接口)")
@RestController
@CrossOrigin
@RequestMapping("/user")
public class InfoController {
    @Autowired
    private InfoService infoService;

    @Autowired
    private GradesService gradesService;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

//    @ApiOperation("图形注册验证码")
//    @GetMapping("registerCode")
//    public R loginCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        HttpSession session = request.getSession();
//        //定义图形/*56
//        // 验证码的长、宽、验证码字符数、干扰线宽度
//        ShearCaptcha captcha = CaptchaUtil.createShearCaptcha(150, 40, 5, 4);
//        //图形验证码写出，可以写出到文件，也可以写出到流
//        captcha.write(response.getOutputStream());
//        //获取验证码中的文字内容
//        String registerCode = captcha.getCode();
//        request.getSession().setAttribute("registerCode",registerCode);
//        String captchaCode = session.getAttribute("registerCode") + "";
//        redisTemplate.opsForValue().set(captchaCode,captchaCode,5, TimeUnit.MINUTES);
//        System.out.println(captchaCode);
//        return R.ok();
//    }

//    图形验证码式注册账号
//    @ApiOperation("用户注册")
//    @PostMapping("register")
//    public R register(@ApiParam("注册对象") @RequestBody RegisterVO registerVO,
//                      HttpSession session){
//        String registerCode = session.getAttribute("registerCode") + "";
//        R r = infoService.register(registerVO,registerCode);
//        session.removeAttribute("registerCode");
//        return r;
//    }

    @ApiOperation("获取用户注册邮箱验证码")
    @GetMapping("email/{email}")
    public R getEmail(@ApiParam("邮箱")@PathVariable String email) {
        if (StringUtils.isEmpty(redisTemplate.opsForValue().get(email))) {
            String code = RandomUtil.randomString("0123456789", 6);
            try {
                MailUtils.sendMail(email, code);
                redisTemplate.opsForValue().set(email, code, 5, TimeUnit.MINUTES);
//                redisTemplate.delete("")
                return R.ok().message("邮箱验证码发送成功");
            } catch (Exception e) {
                ExceptionUtils.getMessage(e);
                return R.error().message("邮箱验证码发送失败");
            }
        }else {
            return R.error().message("当前验证码未过期,请五分钟后获取新的验证码");
        }
    }

    @ApiOperation("用户注册")
    @PostMapping("register")
    public R register(@ApiParam("注册对象") @RequestBody RegisterVO registerVO){
        String captchaCode = redisTemplate.opsForValue().get(registerVO.getEmail());
        R r = infoService.register(registerVO,captchaCode);
        return r;
    }

    @ApiOperation("获取用户信息")
    @PostMapping("info")
    public R getInfo(HttpServletRequest request){
        String token = request.getHeader("token");
        String username = JwtTokenUtils.getUsername(token);
        System.out.println("username"+username);
        LambdaQueryWrapper<Info> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Info::getUsername,username);
        Info info = infoService.getBaseMapper().selectOne(wrapper);
        info.setPassword(null);
        return R.ok().data("info",info);
    }

    @ApiOperation("修改信息")
    @PostMapping("update")
    public R update(@ApiParam("用户信息") @RequestBody InfoVO infoVO){
        Info info = new Info();
        BeanUtils.copyProperties(infoVO,info);
        infoService.getBaseMapper().updateById(info);
        return R.ok().message("修改信息成功");
    }

    @ApiOperation("用户添加高考成绩等信息")
    @PostMapping("save-scores")
    public R saveScores(@ApiParam(name = "gradesForm",value = "前端信息填写表") @RequestParam(required = true)GradesForm gradesForm,
                        HttpServletRequest request ){
        String token = request.getHeader("token");
        String username = JwtTokenUtils.getUsername(token);
        String user = redisTemplate.opsForValue().get("user" + username);
        InfoVO infoVO = JSON.parseObject(user, InfoVO.class);
        String id = infoVO.getId();
        Grades grades = new Grades();
        BeanUtils.copyProperties(gradesForm,grades);
        grades.setUserId(id);
        gradesService.getBaseMapper().insert(grades);
        return R.ok().message("添加成绩信息成功");
    }

    @ApiOperation("用户查询自己设置的成绩等信息")
    @PostMapping("grades-info")
    public R gradesInfo(HttpServletRequest request ){
        String token = request.getHeader("token");
        String username = JwtTokenUtils.getUsername(token);
        String user = redisTemplate.opsForValue().get("user" + username);
        InfoVO infoVO = JSON.parseObject(user, InfoVO.class);
        String id = infoVO.getId();
        LambdaQueryWrapper<Grades> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Grades::getUserId,id);
        Grades grades = gradesService.getBaseMapper().selectOne(wrapper);
        return R.ok().data("grades",grades);
    }

    @ApiOperation("修改用户成绩等信息")
    @PostMapping("update-grades-info")
    public R updateGrades(@ApiParam(name = "gradesForm",value = "前端信息填写表") @RequestBody GradesForm gradesForm,
                          HttpServletRequest request ){
        String token = request.getHeader("token");
        String username = JwtTokenUtils.getUsername(token);
        String user = redisTemplate.opsForValue().get("user" + username);
        InfoVO infoVO = JSON.parseObject(user, InfoVO.class);
        String id = infoVO.getId();
        LambdaQueryWrapper<Grades> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Grades::getUserId,id);
        Grades grades = gradesService.getBaseMapper().selectOne(wrapper);
        BeanUtils.copyProperties(gradesForm,grades);
        gradesService.getBaseMapper().updateById(grades);
        BeanUtils.copyProperties(grades,infoVO);
        infoVO.setId(id);
        String infoJson = JSON.toJSONString(infoVO);
        redisTemplate.opsForValue().set("user"+username,infoJson);
        return R.ok().message("添加成绩信息成功");
    }

    @ApiOperation("找回密码--获取通过邮箱找回密码的验证码")
    @GetMapping("find-password/code/{email}")
    public R findPasswordCode(@ApiParam(value = "用户邮箱",name = "email") @PathVariable String email){
        try {
            String oldCode = redisTemplate.opsForValue().get("find"+email);
            if(!StringUtils.isEmpty(oldCode)){
                return R.error().message("验证码还未失效,不能重复获取验证码");
            }
            //生成随机验证码
            String code = RandomUtil.randomString("0123456789", 6);
            //发送验证码
            MailUtils.sendMail(email,code);
            redisTemplate.opsForValue().set("find"+email, code, 5, TimeUnit.MINUTES);
            return R.ok().message("邮箱验证码发送成功");
        } catch (Exception e) {
            ExceptionUtils.getMessage(e);
            return R.error().message("登录  邮箱验证码发送出现错误");
        }
    }
    @ApiOperation("找回密码--根据邮箱验证码验证")
    @GetMapping("find-password/{email}/{code}")
    public R findPassword(@ApiParam(value = "验证码",name = "code")@PathVariable String code,
                          @ApiParam(value = "邮箱",name = "email")@PathVariable String email){
        String redisCode = redisTemplate.opsForValue().get("find" + email);
        if (!redisCode.equals(code)){
            return R.error().message("验证码错误");
        }
        LambdaQueryWrapper<Info> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Info::getEmail,email);
        Info info = infoService.getBaseMapper().selectOne(wrapper);
        if (info==null||info.equals("")){
            return R.error().message("无该用户");
        }
        return R.ok().data("email",email).message("验证通过,请把这个email一块封装到FindPasswordVO中");
    }

    @ApiOperation("找回密码--修改密码")
    @PutMapping("find-password/update")
    public R findPasswordUpdate(@ApiParam(value = "修改密码前端封装",name = "findPasswordVO") @RequestBody FindPasswordVO findPasswordVO){
        LambdaQueryWrapper<Info> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Info::getEmail,findPasswordVO.getEmail());
        Info info = infoService.getBaseMapper().selectOne(wrapper);
        if (!findPasswordVO.getOnePassword().equals(findPasswordVO.getTwoPassword())){
            return R.error().message("两次密码不一致");
        }
        Info newInfo = new Info();
        String password =  DigestUtils.md5DigestAsHex(findPasswordVO.getOnePassword().getBytes());
        newInfo.setPassword(password);
        newInfo.setId(info.getId());
        infoService.getBaseMapper().updateById(newInfo);
        return R.ok().message("修改密码成功");
    }

}

