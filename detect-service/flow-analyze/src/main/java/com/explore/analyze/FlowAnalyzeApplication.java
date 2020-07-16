package com.explore.analyze;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;

@EnableEurekaClient
@SpringBootApplication
@ComponentScan("com.explore.service")
@MapperScan({"com.explore.analyze.mappers", "com.explore.mapper"})
public class FlowAnalyzeApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlowAnalyzeApplication.class, args);
    }

}
