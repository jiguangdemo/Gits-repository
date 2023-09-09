package com.sdau.nemt.service.information;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Date: 2023/8/10 11:17
 * @Author:
 * @Description:
 */
@SpringBootApplication
@ComponentScan("com.sdau.nemt")
@EnableDiscoveryClient
public class ServiceInformationApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceInformationApplication.class,args);
    }
}
