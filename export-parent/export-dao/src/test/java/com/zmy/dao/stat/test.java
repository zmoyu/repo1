package com.zmy.dao.stat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/applicationContext-dao.xml")
public class test {

    @Autowired
    private StatDao statDao;

    @Test
    public void selectById(){
        List<Map<String, Object>> list = statDao.factorySale();
        System.out.println("list = " + list);
    }
}
