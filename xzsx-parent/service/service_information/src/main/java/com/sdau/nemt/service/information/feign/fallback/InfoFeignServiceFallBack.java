package com.sdau.nemt.service.information.feign.fallback;

import com.sdau.nemt.common.base.result.R;
import com.sdau.nemt.service.information.feign.InfoFeignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Date: 2023-08-10 15:43
 * @Author:
 * @Description:
 */
@Service
@Slf4j
public class InfoFeignServiceFallBack implements InfoFeignService {
    /**
     * 获取用户信息
     *
     * @param url
     * @return
     */
    @Override
    public R getInfo(String url) {
        log.info("熔断保护");
        return R.error();
    }
}
