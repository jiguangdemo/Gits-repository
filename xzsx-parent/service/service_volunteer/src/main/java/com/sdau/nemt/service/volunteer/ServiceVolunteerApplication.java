package com.sdau.nemt.service.volunteer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author
 * @since 2023-08-11
 * @Description:
 */

@EnableFeignClients
@SpringBootApplication
@ComponentScan("com.sdau.nemt")
@EnableDiscoveryClient
public class ServiceVolunteerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceVolunteerApplication.class,args);
    }
}
