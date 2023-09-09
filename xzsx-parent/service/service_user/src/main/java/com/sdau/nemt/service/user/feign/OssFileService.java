package com.sdau.nemt.service.user.feign;


import com.sdau.nemt.common.base.result.R;
import com.sdau.nemt.service.user.feign.fallback.OssFileServiceFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Date: 2023-08-11
 * @Author:
 * @Description:
 */
@Service
@FeignClient(value = "service-oss", fallback = OssFileServiceFallBack.class)
public interface OssFileService {

    /**
     * 删除文件
     * @param url
     * @return
     */
    @DeleteMapping("/user/oss/file/remove")
    R removeFile(@RequestBody String url);

}
