package com.zmy.service.cargo;

import com.github.pagehelper.PageInfo;
import com.zmy.domain.cargo.Factory;
import com.zmy.domain.cargo.FactoryExample;

import java.util.List;

/**
 * 工厂模块
 */
public interface FactoryService {

    /**
     * 分页查询
     */
    PageInfo<Factory> findByPage(
            FactoryExample factoryExample, int pageNum, int pageSize);

    /**
     * 查询所有
     */
    List<Factory> findAll(FactoryExample factoryExample);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    Factory findById(String id);

    /**
     * 新增
     */
    void save(Factory factory);

    /**
     * 修改
     */
    void update(Factory factory);

    /**
     * 删除部门
     */
    void delete(String id);

    /**
     * 根据工厂查询
     * @param factoryName
     * @return
     */
   Factory findFactoryByName(String factoryName);
}











