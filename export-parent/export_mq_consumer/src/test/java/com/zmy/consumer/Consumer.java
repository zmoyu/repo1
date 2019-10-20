package com.zmy.consumer;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

public class Consumer {
    public static void main(String[] args) throws IOException {
        ClassPathXmlApplicationContext ctx =
                new ClassPathXmlApplicationContext("classpath:applicationContext-rabbitmq-consumer.xml");
        //ctx.start();
        System.in.read();
    }
}
