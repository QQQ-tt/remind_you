package com.health.remind;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author qtx
 * @since 2025/1/14 10:13
 */
@EnableScheduling
@ServletComponentScan
@SpringBootApplication
public class RemindYouApplication {

    public static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(RemindYouApplication.class, args);
    }
}
