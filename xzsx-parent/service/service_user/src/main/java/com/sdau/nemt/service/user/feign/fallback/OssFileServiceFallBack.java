package com.sdau.nemt.service.user.feign.fallback;

/**
 * @Date: 2023-08-11
 * @Author:
 * @Description:
 */

import com.sdau.nemt.common.base.result.R;
import com.sdau.nemt.service.user.feign.OssFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OssFileServiceFallBack implements OssFileService {

    @Override
    public R removeFile(String url) {
        log.info("熔断保护");
        return R.error();
    }
}
