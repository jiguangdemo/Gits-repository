package com.sdau.nemt.service.user.service.impl;

import cn.hutool.crypto.digest.MD5;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sdau.nemt.common.base.result.R;
import com.sdau.nemt.common.base.util.ExceptionUtils;
import com.sdau.nemt.service.user.entity.Grades;
import com.sdau.nemt.service.user.entity.Info;
import com.sdau.nemt.service.user.entity.vo.InfoVO;
import com.sdau.nemt.service.user.entity.vo.LoginVO;
import com.sdau.nemt.service.user.entity.vo.RegisterVO;
import com.sdau.nemt.service.user.mapper.InfoMapper;
import com.sdau.nemt.service.user.service.GradesService;
import com.sdau.nemt.service.user.service.InfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sdau.nemt.service.user.util.MailUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 用户信息 服务实现类
 * </p>
 *
 * @author
 * @since 2023-08-11
 */
@Service
public class InfoServiceImpl extends ServiceImpl<InfoMapper, Info> implements InfoService {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private GradesService gradesService;

    /**
     * 用户注册账号
     * @param registerVO
     * @param captchaCode
     * @returnb 返回注册结果  用R来封装
     */
    @Override
    public R register(RegisterVO registerVO, String captchaCode) {
        //封装邮箱条件，查询邮箱是否以及被注册
        LambdaQueryWrapper<Info> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Info::getEmail,registerVO.getEmail());
        Info selectInfo = baseMapper.selectOne(wrapper);
        if(!StringUtils.isEmpty(selectInfo)){
            return R.error().message("该邮箱已被注册,请直接登录或更换邮箱");
        }
        /**
         * 获取传递过来的参数，进行空值判断
         */
        String code = registerVO.getCode();
        String nickName = registerVO.getNickName();
        String password = registerVO.getPassword();
        String rePassword = registerVO.getRePassword();
        String phone = registerVO.getPhone();
        String realName = registerVO.getRealName();
        String email = registerVO.getEmail();
        if(StringUtils.isEmpty(captchaCode)){
            return R.error().message("验证码超时,请重新获取");
        }
        if(StringUtils.isEmpty(code)){
            return R.error().message("验证码不能为空");
        }
        if(!captchaCode.equals(code)){
            return R.error().message("验证码错误");
        }
        if(StringUtils.isEmpty(nickName)||StringUtils.isEmpty(realName)||StringUtils.isEmpty(phone)||StringUtils.isEmpty(email)){
            return R.error().message("请输入完整信息");
        }
        if(!password.equals(rePassword)){
            return R.error().message("两次密码不一致");
        }

        //密码加密    MD5加密   便于简便   此处未设置盐值
        password =  DigestUtils.md5DigestAsHex(password.getBytes());
        Info info = new Info();
        //强制用户账号为邮箱

        BeanUtils.copyProperties(registerVO,info);
        info.setUsername(info.getEmail());
        info.setPassword(password);
        info.setAvatar("https://picx.zhimg.com/80/v2-6afa72220d29f045c15217aa6b275808_720w.jpg?source=1940ef5c");
        baseMapper.insert(info);
        LambdaQueryWrapper<Info> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Info::getEmail,info.getEmail());
        Info selectOne = baseMapper.selectOne(queryWrapper);
        String id = selectOne.getId();
        Grades grades = new Grades();
        grades.setUserId(id);
        gradesService.getBaseMapper().insert(grades);
        return R.ok().message("注册成功");
    }

    /**
     * 通过图形验证码登录账号
     * @param loginVO
     * @return
     */
    @Override
    public R loginByCaptchaCode(LoginVO loginVO) {
        //获取账号和密码
        String username = loginVO.getUsername();
        String password = loginVO.getPassword();
        if(StringUtils.isEmpty(username)||StringUtils.isEmpty(password)){
            return R.error().message("请输入账号或密码");
        }
        //将该密码进行MD5加密
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        //通过账号、密码查询用户
        //先将账号、密码封装wrapper
        LambdaQueryWrapper<Info> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Info::getUsername,username);
        wrapper.eq(Info::getPassword,password);
        Info info = baseMapper.selectOne(wrapper);
        String id = info.getId();
        LambdaQueryWrapper<Grades> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Grades::getUserId,id);
        Grades grades = gradesService.getBaseMapper().selectOne(queryWrapper);
        if(StringUtils.isEmpty(info)){
            return R.error().message("账号或密码不正确");
        }
        InfoVO infoVO = new InfoVO();
        if (grades!=null) {
            BeanUtils.copyProperties(grades, infoVO);
        }
        BeanUtils.copyProperties(info,infoVO);
        return R.ok().message("登录成功").data("user",infoVO);
    }

    /**
     * 通过邮箱登录账号
     * @param loginVO
     */
    @Override
    public R loginByEmail(LoginVO loginVO) {
        String username = loginVO.getUsername();
        String code = loginVO.getCode();
        if(StringUtils.isEmpty(username)||StringUtils.isEmpty(code)){
            return R.error().message("未输入邮箱或验证码");
        }
        String emailCode = redisTemplate.opsForValue().get(username);
        if(StringUtils.isEmpty(emailCode)){
            return R.error().message("验证码失效,请重新获取验证码");
        }
        if(!emailCode.equals(code)){
            return R.error().message("验证码错误");
        }
        //封装条件查询用户信息
        LambdaQueryWrapper<Info> wrapper = new LambdaQueryWrapper();
        wrapper.eq(Info::getEmail,username);
        Info info = baseMapper.selectOne(wrapper);
        if(StringUtils.isEmpty(info)){
            return R.error().message("验证码不正确");
        }
        String id = info.getId();
        LambdaQueryWrapper<Grades> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Grades::getUserId,id);
        Grades grades = gradesService.getBaseMapper().selectOne(queryWrapper);
        InfoVO infoVO = new InfoVO();
        if (grades!=null) {
            BeanUtils.copyProperties(grades, infoVO);
        }
        BeanUtils.copyProperties(info,infoVO);
        return R.ok().message("登录成功").data("user",infoVO);
    }

    /**
     * 根据用户名获取用户
     * @param username
     * @return
     */
    @Override
    public Info getInfoByUsername(String username) {
        LambdaQueryWrapper<Info> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Info::getUsername,username);
        Info info = baseMapper.selectOne(wrapper);
        return info;
    }

    /**
     * 手机号登录
     * @param loginVO
     * @return
     */
    @Override
    public R loginByPhone(LoginVO loginVO) {
        String username = loginVO.getUsername();
        String code = loginVO.getCode();
        if(StringUtils.isEmpty(username)||StringUtils.isEmpty(code)){
            return R.error().message("未输入手机号或验证码");
        }
        String phoneCode = redisTemplate.opsForValue().get(username);
        if(StringUtils.isEmpty(phoneCode)){
            return R.error().message("验证码失效,请重新获取验证码");
        }
        if(!phoneCode.equals(code)){
            return R.error().message("验证码错误");
        }
        //封装条件查询用户信息
        LambdaQueryWrapper<Info> wrapper = new LambdaQueryWrapper();
        wrapper.eq(Info::getPhone,username);
        Info info = baseMapper.selectOne(wrapper);
        if(StringUtils.isEmpty(info)){
            return R.error().message("验证码不正确");
        }
        String id = info.getId();
        LambdaQueryWrapper<Grades> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Grades::getUserId,id);
        Grades grades = gradesService.getBaseMapper().selectOne(queryWrapper);
        InfoVO infoVO = new InfoVO();
        if (grades!=null) {
            BeanUtils.copyProperties(grades, infoVO);
        }
        BeanUtils.copyProperties(info,infoVO);
        return R.ok().message("登录成功").data("user",infoVO);
    }
}
