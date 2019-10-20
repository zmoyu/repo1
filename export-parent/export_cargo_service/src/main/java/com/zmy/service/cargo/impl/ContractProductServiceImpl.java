package com.zmy.service.cargo.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zmy.dao.cargo.ContractProductDao;
import com.zmy.dao.cargo.ExtCproductDao;
import com.zmy.domain.cargo.*;
import com.zmy.service.cargo.ContractProductService;
import com.zmy.service.cargo.ContractService;
import com.zmy.vo.ContractProductVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

@Service(timeout = 1000000)
public class ContractProductServiceImpl implements ContractProductService {

    //注入dao
    @Autowired
    private ContractProductDao contractProductDao;
    @Autowired
    private ContractService contractService;
    @Autowired
    private ExtCproductDao extCproductDao;

    @Override
    public PageInfo<ContractProduct> findByPage(ContractProductExample contractProductExample, int pageNum, int pageSize) {
        //分页查询
        PageHelper.startPage(pageNum, pageSize);
        List<ContractProduct> list = contractProductDao.selectByExample(contractProductExample);
        return new PageInfo<>(list);
    }

    @Override
    public List<ContractProduct> findAll(ContractProductExample contractProductExample) {
        return contractProductDao.selectByExample(contractProductExample);
    }

    @Override
    public ContractProduct findById(String id) {
        return contractProductDao.selectByPrimaryKey(id);
    }

    //添加货物
    @Override
    public void save(ContractProduct contractProduct) {
        //1.设置基本属性（保存的id）
        contractProduct.setId(UUID.randomUUID().toString());
        //计算货物金额
        Double amount = 0d;
        if (contractProduct.getPrice() != null && contractProduct.getCnumber() != null) {
            amount = contractProduct.getPrice() * contractProduct.getCnumber();
            contractProduct.setAmount(amount);
        }
        //修改购销合同
        //获取购销合同对象
        Contract contract = contractService.findById(contractProduct.getContractId());
        //总金额=总金额+货物金额
        contract.setTotalAmount(contract.getTotalAmount() + amount);
        //货物数量=货物数量+1
        contract.setProNum(contract.getProNum() + 1);
        //更新购销合同
        contractService.update(contract);

        //添加货物
        contractProductDao.insertSelective(contractProduct);
    }

    //修改货物
    @Override
    public void update(ContractProduct contractProduct) {
        //计算修改后的金额
        Double amount = 0d;
        if (contractProduct.getPrice() != null && contractProduct.getCnumber() != null) {
            amount = contractProduct.getPrice() * contractProduct.getCnumber();
        }
        contractProduct.setAmount(amount);

        //获取修改之前的货物对象
        ContractProduct contractProduct1 = contractProductDao.selectByPrimaryKey(contractProduct.getId());
        Double oldAmount = contractProduct1.getAmount();

        //获取购销合同对象
        Contract contract = contractService.findById(contractProduct.getContractId());

        //修改购销合同金额
        //修改购销合同总金额 = 总金额 + 修改后 - 修改前
        contract.setTotalAmount(contract.getTotalAmount() + amount - oldAmount);
        //更新合同
        contractService.update(contract);

        //修改货物
        contractProductDao.updateByPrimaryKeySelective(contractProduct);
    }

    @Override
    public void delete(String id) {
        //1.根据货物id查询出货物金额
        ContractProduct contractProduct = contractProductDao.selectByPrimaryKey(id);
        Double amount = contractProduct.getAmount();

        //2.查询出所有附件，累计附件金额，删除附件
        ExtCproductExample extCproductExample = new ExtCproductExample();
        //B1. 根据货物id查询附件
        extCproductExample.createCriteria().andContractProductIdEqualTo(id);
        List<ExtCproduct> extCproducts = extCproductDao.selectByExample(extCproductExample);
        //遍历获取所有附件统计金额
        Double extAmount = 0d;
        if (extCproducts != null && extCproducts.size() > 0) {
            for (ExtCproduct extCproduct : extCproducts) {
                //累加附件金额
                extAmount += extCproduct.getAmount();
                //删除附件
                extCproductDao.deleteByPrimaryKey(extCproduct.getId());
            }
        }

        //3.更改购销合同
        //3.1查询购销合同
        Contract contract = contractService.findById(contractProduct.getContractId());
        //3.2购销合同金额=总金额-货物金额-附件金额
        contract.setTotalAmount(contract.getTotalAmount() - amount - extAmount);
        //3.3更改货以及附件数量;
        contract.setProNum(contract.getProNum() - 1);
        contract.setExtNum(contract.getExtNum() - extCproducts.size());
        //3.4 更新购销合同
        contractService.update(contract);

        //删除货物
        contractProductDao.deleteByPrimaryKey(id);
    }


    @Override
    public List<ContractProductVo> findByShipTime(String companyId, String shipTime) {
        return contractProductDao.findByShipTime(companyId,shipTime);
    }
}
