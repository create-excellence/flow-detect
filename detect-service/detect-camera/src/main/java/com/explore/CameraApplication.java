package com.explore;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
@MapperScan("com.explore.mapper")
public class CameraApplication {

    public static void main(String[] args) {
        SpringApplication.run(CameraApplication.class);
    }
}
