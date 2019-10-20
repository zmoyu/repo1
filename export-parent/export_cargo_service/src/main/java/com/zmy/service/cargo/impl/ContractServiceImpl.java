package com.zmy.service.cargo.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.zmy.dao.cargo.ContractDao;
import com.zmy.domain.cargo.Contract;
import com.zmy.domain.cargo.ContractExample;
import com.zmy.service.cargo.ContractService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service(timeout = 1000000)
public class ContractServiceImpl implements ContractService {

    // 注入dao
    @Autowired
    private ContractDao contractDao;

    @Override
    public PageInfo<Contract> findByPage(ContractExample contractExample, int pageNum, int pageSize) {
        /*开启分页查询*/
        PageHelper.startPage(pageNum, pageSize);
        List<Contract> list = contractDao.selectByExample(contractExample);

        return new PageInfo<>(list);
    }

    @Override
    public List<Contract> findAll(ContractExample contractExample) {
        return contractDao.selectByExample(contractExample);
    }

    @Override
    public Contract findById(String id) {
        return contractDao.selectByPrimaryKey(id);
    }

    @Override
    public void save(Contract contract) {
        // 设置uuid作为主键
        contract.setId(UUID.randomUUID().toString());
        // 记录购销合同创建时间
        contract.setCreateTime(new Date());
        // 默认状态为草稿
        contract.setState(0);


        // 初始化： 总金额为0
        contract.setTotalAmount(0d);
        // 初始化： 货物数、附件数
        contract.setProNum(0);
        contract.setExtNum(0);

        contractDao.insertSelective(contract);
    }

    @Override
    public void update(Contract contract) {
        contractDao.updateByPrimaryKeySelective(contract);
    }

    @Override
    public void delete(String id) {
        contractDao.deleteByPrimaryKey(id);
    }

    /*根据部门id查询当前部门以及子部门所创建的购销合同*/
    @Override
    public PageInfo<Contract> selectByDeptId(String deptId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Contract> list = contractDao.selectByDeptId(deptId);

        return new PageInfo<>(list);
    }
}
