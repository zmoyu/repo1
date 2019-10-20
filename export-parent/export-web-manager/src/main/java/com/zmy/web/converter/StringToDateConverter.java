package com.zmy.web.converter;

import org.springframework.core.convert.converter.Converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 日期转化器
 *      实现Converter接口
 */
public class StringToDateConverter implements Converter<String, Date> {
//    @Override
//    public Date convert(String s) {
//        return null;
//    }
    @Override
    public Date convert(String s) {

        Date result =null;
        try {
            result = new SimpleDateFormat("yyyy-MM-dd").parse(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
