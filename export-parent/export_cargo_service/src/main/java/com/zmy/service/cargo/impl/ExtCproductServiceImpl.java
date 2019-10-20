package com.zmy.service.cargo.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zmy.dao.cargo.ContractDao;
import com.zmy.dao.cargo.ExtCproductDao;
import com.zmy.domain.cargo.Contract;
import com.zmy.domain.cargo.ExtCproduct;
import com.zmy.domain.cargo.ExtCproductExample;
import com.zmy.service.cargo.ExtCproductService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

@Service(timeout = 1000000)
public class ExtCproductServiceImpl implements ExtCproductService {

    @Autowired
    private ExtCproductDao extCproductDao;
    @Autowired
    private ContractDao contractDao;

    @Override
    public PageInfo<ExtCproduct> findByPage(ExtCproductExample extCproductExample, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<ExtCproduct> list = extCproductDao.selectByExample(extCproductExample);

        return new PageInfo<>(list);
    }

    @Override
    public List<ExtCproduct> findAll(ExtCproductExample extCproductExample) {
        return extCproductDao.selectByExample(extCproductExample);
    }

    @Override
    public ExtCproduct findById(String id) {
        return extCproductDao.selectByPrimaryKey(id);
    }

    @Override
    public void save(ExtCproduct extCproduct) {
        //设置主键
        extCproduct.setId(UUID.randomUUID().toString());
        //1.计算附件金额
        Double amount = 0d;
        if (extCproduct.getPrice() != null && extCproduct.getCnumber() != null) {
            amount = extCproduct.getCnumber() * extCproduct.getPrice();
        }
        //1.1添加附件的总金额
        extCproduct.setAmount(amount);
        //1.2保存附件
        extCproductDao.insertSelective(extCproduct);

        //2.计算修改购销合同
        //2.1获购销合同对象
        Contract contract = contractDao.selectByPrimaryKey(extCproduct.getContractId());
        //2.2设置附件数量
        contract.setExtNum(contract.getExtNum() + 1);
        //2.3设置总金额,购销合同总金额=总金额+附件金额
        contract.setTotalAmount(contract.getTotalAmount() + amount);
        //2.4更新购销合同
        contractDao.updateByPrimaryKeySelective(contract);


    }

    @Override
    public void update(ExtCproduct extCproduct) {
        //1.查询数据库，得到修改之前的货物金额
        ExtCproduct extCp = extCproductDao.selectByPrimaryKey(extCproduct.getId());

        Double oldExtCpAmount = extCp.getAmount();
        //2.计算修改之后附件金额
        Double amount = 0d;
        if (extCproduct.getCnumber() != null && extCproduct.getPrice() != null) {
            amount = extCproduct.getCnumber() * extCproduct.getPrice();
        }
        //设置修改后的金额
        extCproduct.setAmount(amount);

        //3.修改购销合同
        Contract contract = contractDao.selectByPrimaryKey(extCproduct.getContractId());
        //3.1更新购销合同金额
        contract.setTotalAmount(contract.getTotalAmount() + amount - oldExtCpAmount);
        //3.2更新购销合同
        contractDao.updateByPrimaryKeySelective(contract);

        //修改附件
        extCproductDao.updateByPrimaryKeySelective(extCproduct);
    }

    @Override
    public void delete(String id) {
        //得到附件金额
        ExtCproduct extCproduct = extCproductDao.selectByPrimaryKey(id);
        Double amount = extCproduct.getAmount();

        //修改合同
        Contract contract = contractDao.selectByPrimaryKey(extCproduct.getContractId());
        //修改金额
        contract.setTotalAmount(contract.getTotalAmount()-amount);
        //设置附件数量
        contract.setExtNum(contract.getExtNum()-1);
        //更新合同
        contractDao.updateByPrimaryKeySelective(contract);

        //删除附件
        extCproductDao.deleteByPrimaryKey(id);
    }
}
