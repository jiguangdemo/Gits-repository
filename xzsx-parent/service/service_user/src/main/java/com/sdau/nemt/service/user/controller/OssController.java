package com.sdau.nemt.service.user.controller;

import com.sdau.nemt.common.base.result.R;
import com.sdau.nemt.service.user.feign.OssFileService;
import com.sdau.nemt.service.user.service.OssService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * @Date: 2023-08-11
 * @Author:
 * @Description:
 */
@Api(tags = "文件上传到阿里云OSS")
@RestController
@CrossOrigin
@RequestMapping("/user/info/oss")
public class OssController {
    @Autowired
    private OssService ossservice;
    @Autowired
    private OssFileService ossFileService;
    //上传头像的方法
    @ApiOperation("头像上传")
    @PostMapping("avatar")
    public R uploadOssFile(    @ApiParam(value = "文件", required = true)
                               @RequestParam("file") MultipartFile file,
                               @ApiParam(value = "模板", required = true)
                               @RequestParam("module") String module) throws IOException {
        //获取上传文件
        //返回上传到oss的路径
        InputStream inputStream = file.getInputStream();
        String originalFilename = file.getOriginalFilename();
        String uploadUrl = ossservice.upload(inputStream, module, originalFilename);
        return R.ok().message("文件上传成功").data("url",uploadUrl);
    }

    @ApiOperation("文件删除")
    @DeleteMapping()
    public R deleteFile(){
        R r = ossFileService.removeFile("https://edu-www-1010.oss-cn-beijing.aliyuncs.com/avatar/2022/07/31/310e1118-bc9c-4474-9d72-29ca82bf3379.jpg");
        return r;
    }


}
