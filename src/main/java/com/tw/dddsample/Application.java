package com.tw.dddsample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by azhu on 27/05/2017.
 */
@EnableAutoConfiguration
@ComponentScan("com.tw.dddsample")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
