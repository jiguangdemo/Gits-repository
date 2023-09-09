package com.sdau.nemt.service.user.service.impl;

import cn.hutool.core.date.DateTime;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.CannedAccessControlList;
import com.sdau.nemt.service.user.service.OssService;
import com.sdau.nemt.service.user.util.ConstantPropertiesUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

/**
 * @Date: 2022/7/17 17:42
 * @author
 * @since 2023-08-11
 */
@Service
public class OssServiceImpl implements OssService {
    @Override
    public String uploadFileAvatar(MultipartFile file) {
        //用工具类获取
        String endpoint = ConstantPropertiesUtils.END_POINT;
        String accessKeyId = ConstantPropertiesUtils.ACCESS_KEY_ID;
        String accessKeySecret = ConstantPropertiesUtils.ACCESS_KEY_SECRET;
        String bucketName = ConstantPropertiesUtils.BUCKET_NAME;

        try {
            // 创建OSSClient实例。
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

            //获取文件上传输入流
            InputStream inputStream = file.getInputStream();
            //获取文件名称

            String fileName = file.getOriginalFilename();
            ///当文件名重复的时候，再oss中会覆盖掉原有的文件

            //解决1：用uuid---------------replace是为了将随机生成的数字的“-”去掉
            String uuid= UUID.randomUUID().toString().replace("-","");
//            //拼接
//             fileName=uuid+fileName;

            //解决2：把文件按日期分类
            //2021//11/12/01.jpg
            //获取当前日期，用依赖中的工具类
            String datePath = new DateTime().toString("yyyy/MM/dd");
            //拼接
            fileName=datePath+fileName;

            //调用oss方法实现上传
            //参数1：Bucket名称
            //参数2：上传到oss的文件路径和文件名
            //参数3：上传文件输入流
            ossClient.putObject(bucketName, fileName, inputStream);
            // 关闭OSSClient。
            ossClient.shutdown();

            //把上传之后的文件路径返回
            //需要把上传到阿里云oss路径手动拼接出来
            //根据路径名‘
            String url = "https://" + bucketName + "." + endpoint + "/" + fileName;
            return url;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String upload(InputStream inputStream, String module, String originalFilename) {
        //用工具类获取
        String endpoint = ConstantPropertiesUtils.END_POINT;
        String accessKeyId = ConstantPropertiesUtils.ACCESS_KEY_ID;
        String accessKeySecret = ConstantPropertiesUtils.ACCESS_KEY_SECRET;
        String bucketName = ConstantPropertiesUtils.BUCKET_NAME;
        //创建实例
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        if(ossClient.doesBucketExist(bucketName)){
            ossClient.createBucket(bucketName);
            ossClient.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
        }
        //构建objectName，文件路径
        String floder = new DateTime().toString("yyyy/MM/dd");
        String fileName = UUID.randomUUID().toString();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String key = module + "/" + floder + "/" + fileName + fileExtension;

        // 创建PutObject请求。
        ossClient.putObject(bucketName, key, inputStream);
        ossClient.shutdown();

        //返回url
        return "https://" + bucketName + "." + endpoint + "/" + key;
    }

}
