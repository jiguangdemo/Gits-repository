package com.sdau.nemt.service.volunteer.util;
//import com.alibaba.easyexcel.test.util.TestFileUtil;
import com.alibaba.excel.EasyExcel;
import com.sdau.nemt.common.base.result.R;
import com.sdau.nemt.service.volunteer.entity.export.MockExport;
import com.sdau.nemt.service.volunteer.service.feign.OssFeignClient;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
//import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * @author
 * @since 2023-08-11
 * @Description:
 */
public class ExcelUtil {

    @Autowired
    private static OssFeignClient ossFeignClient;

    public static String mockExcelWrite(String name, List list){

        //1、创建一个文件对象
        File excelFile = new File("志愿表.xlsx");
        //2、判断文件是否存在，不存在则创建一个Excel文件
        if (!excelFile.exists()) {
            try {
                excelFile.createNewFile();//创建一个新的文件
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        String fileName = System.currentTimeMillis() + name + ".xlsx";
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        EasyExcel.write(excelFile, MockExport.class).sheet(name).doWrite(list);
        MultipartFile file = getMultipartFile(excelFile);
        System.out.println("file:"+file);
        R excel = ossFeignClient.upload(file, "Excel");
        Map<String, Object> data = excel.getData();
        String url = (String) data.get("url");

        return url;
    }

    public static MultipartFile getMultipartFile(File file) {
        FileItem item = new DiskFileItemFactory().createItem("file"
                , MediaType.MULTIPART_FORM_DATA_VALUE
                , true
                , file.getName());
        try (InputStream input = new FileInputStream(file);
             OutputStream os = item.getOutputStream()) {
            // 流转移
            IOUtils.copy(input, os);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid file: " + e, e);
        }

        return new CommonsMultipartFile(item);
    }

}
