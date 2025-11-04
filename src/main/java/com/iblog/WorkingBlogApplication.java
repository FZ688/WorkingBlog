package com.iblog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author fz
 */
@SpringBootApplication
@EnableScheduling
@MapperScan(basePackages = {"com.iblog.mappers"})
public class WorkingBlogApplication {
    public static void main(String[] args) {
        SpringApplication.run(WorkingBlogApplication.class, args);
    }
}
