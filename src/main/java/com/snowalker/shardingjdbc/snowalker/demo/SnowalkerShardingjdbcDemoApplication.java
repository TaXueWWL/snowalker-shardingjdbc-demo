package com.snowalker.shardingjdbc.snowalker.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.snowalker.shardingjdbc.snowalker.demo.mapper")
public class SnowalkerShardingjdbcDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SnowalkerShardingjdbcDemoApplication.class, args);
    }

}
