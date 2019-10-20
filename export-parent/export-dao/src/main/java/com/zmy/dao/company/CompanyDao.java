package com.zmy.dao.company;

import com.zmy.domain.company.Company;

import java.util.List;

public interface CompanyDao {

    //查询所有企业
    List<Company> findAll();

    //添加企业
    void save(Company company);

    //修改企业
    void update(Company company);

    /**
     * 根据id查询企业
     * @param id
     * @return
     */
    Company findById(String id);

    /**
     * 根据id删除企业
     * @param id
     */
    void toDelete(String id);
}
