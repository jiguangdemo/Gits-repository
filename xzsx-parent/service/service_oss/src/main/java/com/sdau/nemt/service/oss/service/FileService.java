package com.sdau.nemt.service.oss.service;

import java.io.InputStream;

/**
 * @Date: 2023-8-11 6:57
 * @Author:
 * @Description:
 */
public interface FileService {

    /**
     * 阿里云oss文件上传
     * @param inputStream 输入流
     * @param module 文件夹名字
     * @param originalFilename 原始文件名字
     * @return
     */
    String upload(InputStream inputStream, String module, String originalFilename);

    /**
     * 阿里云oss删除文件
     * @param url 文件的url地址
     */
    void removeFile(String url);
}
