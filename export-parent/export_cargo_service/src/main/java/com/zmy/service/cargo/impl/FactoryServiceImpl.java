package com.zmy.service.cargo.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zmy.dao.cargo.FactoryDao;
import com.zmy.domain.cargo.Factory;
import com.zmy.domain.cargo.FactoryExample;
import com.zmy.service.cargo.FactoryService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service(timeout = 1000000)
public class FactoryServiceImpl implements FactoryService {

    //注入dao
    @Autowired
    private FactoryDao factoryDao;

    @Override
    public PageInfo<Factory> findByPage(FactoryExample factoryExample, int pageNum, int pageSize) {
        //分页查询
        PageHelper.startPage(pageNum, pageSize);
        List<Factory> list = factoryDao.selectByExample(factoryExample);

        return new PageInfo<>(list);
    }

    @Override
    public List<Factory> findAll(FactoryExample factoryExample) {
        return factoryDao.selectByExample(factoryExample);
    }

    @Override
    public Factory findById(String id) {
        return factoryDao.selectByPrimaryKey(id);
    }

    @Override
    public void save(Factory factory) {
        factoryDao.insertSelective(factory);
    }

    @Override
    public void update(Factory factory) {
        factoryDao.updateByPrimaryKeySelective(factory);

    }

    @Override
    public void delete(String id) {
        factoryDao.deleteByPrimaryKey(id);

    }

    @Override
    public Factory findFactoryByName(String factoryName) {
        List<Factory> list = factoryDao.findFactoryByName(factoryName);
        return (list != null && list.size() > 0)? list.get(0):null;
    }
}
