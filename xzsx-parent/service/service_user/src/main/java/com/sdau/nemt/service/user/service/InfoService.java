package com.sdau.nemt.service.user.service;

import com.sdau.nemt.common.base.result.R;
import com.sdau.nemt.service.user.entity.Info;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sdau.nemt.service.user.entity.vo.LoginVO;
import com.sdau.nemt.service.user.entity.vo.RegisterVO;

/**
 * <p>
 * 用户信息 服务类
 * </p>
 *
 * @author
 * @since 2023-08-11
 */
public interface InfoService extends IService<Info> {

    /**
     * 用户注册账号
     * @param registerVO
     * @param captchaCode
     * @return
     */
    R register(RegisterVO registerVO, String captchaCode);

    /**
     * 通过图形验证码登录账号
     * @param loginVO
     * @return
     */
    R loginByCaptchaCode(LoginVO loginVO);

    /**
     * 通过邮箱登录账号
     * @param loginVO
     * @return
     */
    R loginByEmail(LoginVO loginVO);

    /**
     * 根据用户名获取用户
     * @param username
     * @return
     */
    Info getInfoByUsername(String username);

    /**
     * 短信登录
     * @param loginVO
     * @return
     */
    R loginByPhone(LoginVO loginVO);
}
