package com.zmy.provider;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

public class ContractProvider {

    public static void main(String[] args) throws IOException {
        ClassPathXmlApplicationContext cac =
                new ClassPathXmlApplicationContext("classpath*:spring/applicationContext-*.xml");

        cac.start();
        System.in.read();
    }
}
