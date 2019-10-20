package com.zmy.provider;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

public class CompanyProvider {
    public static void main(String[] args) throws IOException {
        //加载配置文件
        ClassPathXmlApplicationContext ac =
                new ClassPathXmlApplicationContext("classpath*:spring/applicationContext-*.xml");
        //启动
        ac.start();

        //输入后停止
        System.in.read();

    }
}
