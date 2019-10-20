package com.zmy.service.company.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zmy.dao.company.CompanyDao;
import com.zmy.domain.company.Company;
import com.zmy.service.company.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

@Service(timeout = 100000)//超时时间设置为100秒
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyDao companyDao;

    @Override
    public PageInfo<Company> findByPage(int pageNum, int pageSize) {

        //开始分页
        PageHelper.startPage(pageNum,pageSize);//----关键代码，气候第一条查询自动分页
        //调用dao
        List<Company> list = companyDao.findAll();
        return new PageInfo<>(list);
    }

    @Override
    //查询所有公司
    public List<Company> findAll() {
        return companyDao.findAll();
    }

    @Override
    //保存公司
    public void save(Company company) {

        // 设置主键值
        company.setId(UUID.randomUUID().toString());
        companyDao.save(company);
    }

    @Override
    //修改公司
    public void update(Company company) {

        companyDao.update(company);
    }

    @Override
    public Company findById(String id) {
        return companyDao.findById(id);
    }

    @Override
    public void toDelete(String id) {
        companyDao.toDelete(id);
    }
}
