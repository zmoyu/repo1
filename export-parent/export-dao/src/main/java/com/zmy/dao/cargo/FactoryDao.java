package com.zmy.dao.cargo;

import com.zmy.domain.cargo.Factory;
import com.zmy.domain.cargo.FactoryExample;

import java.util.List;

public interface FactoryDao {
    int deleteByPrimaryKey(String id);

    int insert(Factory record);

    int insertSelective(Factory record);

    List<Factory> selectByExample(FactoryExample example);

    Factory selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Factory record);

    int updateByPrimaryKey(Factory record);

    //根据工厂名称查询工厂
    List<Factory> findFactoryByName(String factoryName);
}