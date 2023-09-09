package com.sdau.nemt.service.volunteer.service.feign;

import com.sdau.nemt.common.base.result.R;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author
 * @since 2023-08-11
 * @Description:
 */
@Service
@FeignClient("service-oss")
public interface OssFeignClient {

    @PostMapping("/user/oss/file/upload")
    R upload(@RequestParam("file") MultipartFile file,
             @RequestParam("module") String module);

}
