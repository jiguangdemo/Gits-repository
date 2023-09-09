package com.sdau.nemt.service.information.feign;

import com.sdau.nemt.common.base.result.R;
import com.sdau.nemt.service.information.feign.fallback.InfoFeignServiceFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Date: 2023-08-10 15:38
 * @Author:
 * @Description:
 */
@Service
@FeignClient(value = "service-user" , fallback = InfoFeignServiceFallBack.class)
public interface InfoFeignService {

    /**
     * 获取用户信息
     * @param url
     * @return
     */
    @PostMapping("/user/info")
    R getInfo(@RequestBody String url);
}
