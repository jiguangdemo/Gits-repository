package com.sdau.nemt.service.user.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.sdau.nemt.common.base.result.R;
import com.sdau.nemt.common.base.util.ExceptionUtils;
import com.sdau.nemt.service.user.entity.Info;
import com.sdau.nemt.service.user.entity.vo.InfoVO;
import com.sdau.nemt.service.user.entity.vo.LoginVO;
import com.sdau.nemt.service.user.service.InfoService;
import com.sdau.nemt.service.user.util.CreateJwt;
import com.sdau.nemt.service.user.util.JwtTokenUtils;
import com.sdau.nemt.service.user.util.MailUtils;
import com.sdau.nemt.service.user.util.PhoneUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Date: 2023-08-11
 * @Author:
 * @Description:
 */
@Api(tags = "用户登录")
@RestController
@RequestMapping("/user")
@CrossOrigin
public class LoginController {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private InfoService infoService;

    @ApiOperation("获取登录图形验证码")
    @GetMapping("verifyCode")
    public void loginCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        //定义图形/*56
        // 验证码的长、宽、验证码字符数、干扰线宽度
        ShearCaptcha captcha = CaptchaUtil.createShearCaptcha(150, 40, 4, 0);
        //图形验证码写出，可以写出到文件，也可以写出到流
        captcha.write(response.getOutputStream());
        //获取验证码中的文字内容
        String verifyCode = captcha.getCode();
        request.getSession().setAttribute("verifyCode",verifyCode);
        //打印到控制台
        String captchaCode = (String) session.getAttribute("verifyCode");
        System.out.println(captchaCode);
    }

    @ApiOperation("用户密码图形验证码登录")
    @PostMapping("login")
    public R login(@ApiParam(value = "登录对象",required = true) @RequestBody LoginVO loginVO,
                   HttpSession session){
        String captchaCode = session.getAttribute("verifyCode") + "";
        if(!loginVO.getCode().equals(captchaCode)){
            return R.error().message("验证码错误");
        }
        //调用验证码登录方法
        R r = infoService.loginByCaptchaCode(loginVO);
        if(!r.getSuccess()){
            return r;
        }
//        redisTemplate.delete("user"+loginVO.getUsername());
        //将用户id存入session和redis缓存中，作为用户登录判断,用户最长连续在线时间为1天
        InfoVO user = (InfoVO) r.getData().get("user");
        System.out.println(user.toString());
        String token = JwtTokenUtils.createToken(user.getUsername(),false);
        System.out.println(token);
        //将用户id存入session和redis缓存中，作为用户登录判断,用户最长连续在线时间为1天
//        session.setAttribute("user",user);
        //转换成json对象存储
        String userJson = JSON.toJSONString(user);
        //解析json对象
        //InfoVO infoVO = JSON.parseObject(userJson, InfoVO.class);
        redisTemplate.opsForValue().set("user"+user.getUsername(),userJson,1, TimeUnit.DAYS);
        redisTemplate.opsForValue().set("token"+user.getUsername(),token,1,TimeUnit.DAYS);
        return R.ok().message("登录成功").data("token",token);
    }

    @ApiOperation("获取邮箱登录验证码")
    @GetMapping("login/{email}")
    public R getEmail(@PathVariable String email){
        try {
            String oldCode = redisTemplate.opsForValue().get(email);
            if(!StringUtils.isEmpty(oldCode)){
                return R.error().message("验证码还未失效,不能重复获取验证码");
            }
            //生成随机验证码
            String code = RandomUtil.randomString("0123456789", 6);
            //发送验证码
            MailUtils.sendMail(email,code);
            redisTemplate.opsForValue().set(email, code, 5, TimeUnit.MINUTES);
            return R.ok().message("邮箱验证码发送成功");
        } catch (Exception e) {
            ExceptionUtils.getMessage(e);
            return R.error().message("登录  邮箱验证码发送出现错误");
        }
    }

    @ApiOperation("用户邮箱验证码登录")
    @PostMapping("login/email")
    public R loginByPhone(@RequestBody LoginVO loginVO,
                          HttpSession session){
        R r = infoService.loginByEmail(loginVO);
        if(!r.getSuccess()){
            return r;
        }
//        redisTemplate.delete("user"+loginVO.getUsername());
        //将用户id存入session和redis缓存中，作为用户登录判断,用户最长连续在线时间为1天
        InfoVO user = (InfoVO) r.getData().get("user");
        System.out.println(user.toString());
        String token = JwtTokenUtils.createToken(user.getUsername(),false);
        System.out.println(token);
        //将用户id存入session和redis缓存中，作为用户登录判断,用户最长连续在线时间为1天
//        session.setAttribute("user",user);
        //转换成json对象存储
        String userJson = JSON.toJSONString(user);
        //解析json对象
        //InfoVO infoVO = JSON.parseObject(userJson, InfoVO.class);
        redisTemplate.opsForValue().set("user"+user.getUsername(),userJson,1, TimeUnit.DAYS);
        redisTemplate.opsForValue().set("token"+user.getUsername(),token,1,TimeUnit.DAYS);
        return R.ok().message("登录成功").data("token",token);
    }

    @ApiOperation("用户手机验证码登录")
    @PostMapping("login/phone")
    public R loginByEmail(@RequestBody LoginVO loginVO,
                          HttpSession session){
        R r = infoService.loginByPhone(loginVO);
        if(!r.getSuccess()){
            return r;
        }
//        redisTemplate.delete("user"+loginVO.getUsername());

        //将用户id存入session和redis缓存中，作为用户登录判断,用户最长连续在线时间为1天
        InfoVO user = (InfoVO) r.getData().get("user");
        System.out.println(user.toString());
        String token = JwtTokenUtils.createToken(user.getUsername(),false);
        System.out.println(token);
        //将用户id存入session和redis缓存中，作为用户登录判断,用户最长连续在线时间为1天
//        session.setAttribute("user",user);
        //转换成json对象存储
        String userJson = JSON.toJSONString(user);
        //解析json对象
        //InfoVO infoVO = JSON.parseObject(userJson, InfoVO.class);
        redisTemplate.opsForValue().set("user"+user.getUsername(),userJson,1, TimeUnit.DAYS);
        redisTemplate.opsForValue().set("token"+user.getUsername(),token,1,TimeUnit.DAYS);
        return R.ok().message("登录成功").data("token",token);
    }

    @ApiOperation("短信验证码")
    @GetMapping("phone-code/{phone}")
    public R getPhone(@PathVariable String phone){
        //生成随机验证码
        String code = RandomUtil.randomString("0123456789", 6);
        //发送验证码
        try {
            PhoneUtils.sendPhone(phone,code);
            redisTemplate.opsForValue().set(phone,code,5,TimeUnit.MINUTES);
            return R.ok().message("短信验证发送成功");
        } catch (Exception e) {
            e.printStackTrace();
            return R.error().message("短信验证码发送失败");
        }
    }

    @ApiOperation("退出登录")
    @GetMapping("logout")
    public R logout(HttpServletRequest request){
        String token = request.getHeader("token");
//        System.out.println("token:"+token);
        String username = JwtTokenUtils.getUsername(token);
//        System.out.println("username:"+username);
        redisTemplate.delete("user"+username);
        return R.ok().message("退出登录成功");
    }
}
