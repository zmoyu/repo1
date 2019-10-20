package com.zmy.service.stat.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.zmy.dao.stat.StatDao;
import com.zmy.service.stat.StatService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
@Service(timeout = 1000000)
public class StatServiceImpl implements StatService {

    @Autowired
    private StatDao statDao;
    @Override
    public List<Map<String, Object>> factorySale() {

        return statDao.factorySale();
    }

    @Override
    public List<Map<String, Object>> productSale(int top) {
        return statDao.productSale(top);
    }

    @Override
    public List<Map<String, Object>> online() {
        return statDao.online();
    }
}
