package com.tw.practise;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by azhu on 27/05/2017.
 */
@EnableAutoConfiguration
@ComponentScan("com.tw.practise")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
