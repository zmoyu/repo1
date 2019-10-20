package com.zmy.provider;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

public class StatProvider {
    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context =
                new ClassPathXmlApplicationContext("classpath*:spring/applicationContext-*.xml");

        context.start();
        System.in.read();
    }
}
