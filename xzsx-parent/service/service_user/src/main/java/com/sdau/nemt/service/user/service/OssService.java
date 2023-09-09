package com.sdau.nemt.service.user.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * @author
 * @since 2023-08-11
 * @Description:
 */
public interface OssService {
    //上传头像到oss
    String uploadFileAvatar(MultipartFile file);
    //上传头像到oss
    String upload(InputStream inputStream, String module, String originalFilename);
}
