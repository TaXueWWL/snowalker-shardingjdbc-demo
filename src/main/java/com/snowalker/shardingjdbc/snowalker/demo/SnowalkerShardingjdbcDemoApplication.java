package com.snowalker.shardingjdbc.snowalker.demo;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = {"com.snowalker.shardingjdbc.snowalker.demo.mapper",
    "com.snowalker.shardingjdbc.snowalker.demo.complex.sharding.mapper"}
)
@EnableEncryptableProperties
public class SnowalkerShardingjdbcDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SnowalkerShardingjdbcDemoApplication.class, args);
    }

}
