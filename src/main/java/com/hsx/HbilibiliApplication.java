package com.hsx;

import com.hsx.utils.SpringUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement // 开启支持事务注解
@EnableAsync // 开启异步功能
//@EnableScheduling 允许开启定时任务功能
public class HbilibiliApplication {

    public static void main(String[] args) {
        SpringApplication.run(HbilibiliApplication.class, args);
    }

    @Bean
    public SpringUtil springUtil() {
        return new SpringUtil();
    }

}
