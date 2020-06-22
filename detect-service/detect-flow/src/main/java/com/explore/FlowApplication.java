package com.explore;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author PinTeh
 * @date 2020/6/22
 */
@EnableEurekaClient
@SpringBootApplication
@MapperScan("com.explore.mappers")
public class FlowApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlowApplication.class);
    }
}