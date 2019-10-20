package com.zmy.service.cargo.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zmy.dao.cargo.*;
import com.zmy.domain.cargo.*;
import com.zmy.service.cargo.ExportService;
import com.zmy.vo.ExportProductResult;
import com.zmy.vo.ExportResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@Service(timeout = 1000000)
public class ExportServiceImpl implements ExportService {

    @Autowired
    private ExportDao exportDao;
    @Autowired
    private ContractDao contractDao;
    @Autowired
    private ContractProductDao contractProductDao;
    @Autowired
    private ExportProductDao exportProductDao;
    @Autowired
    private ExtCproductDao extCproductDao;
    @Autowired
    private ExtEproductDao extEproductDao;

    @Override
    public Export findById(String id) {
        return exportDao.selectByPrimaryKey(id);
    }

    @Override
    public void save(Export export) {
        //1. 设置报运单参数：id、制单时间、创建时间、状态（合同号、商品数量、附件数量）
        export.setId(UUID.randomUUID().toString());
        export.setInputDate(new Date());
        export.setCreateTime(new Date());
        export.setState(0);

        //获取多个报运合同id
        String contractIds = export.getContractIds();
        //获取多个合同号，用空格隔开;修改购销合同状态
        String customerContract = "";
        String[] contractIdArray = contractIds.split(",");
        for (String contractId : contractIdArray) {
            Contract contract = contractDao.selectByPrimaryKey(contractId);
            String contractNo = contract.getContractNo();
            customerContract += contractNo + " ";
            contract.setState(2);
            contractDao.updateByPrimaryKeySelective(contract);
        }
        //设置报运单合同号
        export.setCustomerContract(customerContract);

        //根据购销合同id集合，查询货物
        ContractProductExample contractProductExample = new ContractProductExample();
        contractProductExample.createCriteria().andContractIdIn(Arrays.asList(contractIdArray));
        List<ContractProduct> cpList = contractProductDao.selectByExample(contractProductExample);

        /**
         * 定义一个map集合，存储key是购销合同的货物id，对应的value是出口报运商品的id
         *             因为后面保存商品附件，需要根据货物id拿到报运商品id
         */
        Map<String, String> map = new HashMap<>();
        //遍历货物集合,作为出口报运的商品，并保存商品
        if (cpList != null && cpList.size() > 0) {
            for (ContractProduct contractProduct : cpList) {
                //创建货运商品对象
                ExportProduct exportProduct = new ExportProduct();
                //把货物属性设置到商品中
                BeanUtils.copyProperties(contractIdArray, exportProduct);
                exportProduct.setId(UUID.randomUUID().toString());
                exportProduct.setExportId(export.getId());

                //保存商品
                exportProductDao.insertSelective(exportProduct);
                //把货物id和商品id存储到map中
                map.put(contractProduct.getId(), exportProduct.getId());
            }
        }

        //根据购销合同id查询所有货物附件
        ExtCproductExample extCproductExample = new ExtCproductExample();
        extCproductExample.createCriteria().andContractIdIn(Arrays.asList(contractIdArray));
        List<ExtCproduct> eCpList = extCproductDao.selectByExample(extCproductExample);

        //遍历货物附件集合，保存到商品附件中
        for (ExtCproduct extCproduct : eCpList) {
            //创建商品附件对象
            ExtEproduct extEproduct = new ExtEproduct();
            //对象属性拷贝
            BeanUtils.copyProperties(extCproduct, extEproduct);
            //设置主键id和报运单id
            extEproduct.setId(UUID.randomUUID().toString());
            extEproduct.setExportId(export.getId());
            /**
             * 已知： 货物id   extCproduct.getContractProductId()
             * 缺少： 商品id   map.get(extCproduct.getContractProductId())
             */
            // 根据货物id，获取map中的商品id
            String exportProductId = map.get(extCproduct.getContractProductId());
            //设置商品id
            extEproduct.setExportProductId(exportProductId);

            //保存商品附件
            extEproductDao.insertSelective(extEproduct);
        }

        //设置报运单中商品数量和附件数量
        export.setProNum(cpList.size());
        export.setExtNum(eCpList.size());
        //保存报运单
        exportDao.insertSelective(export);

    }

    @Override
    public void update(Export export) {
        //更新报运单主体
        exportDao.updateByPrimaryKeySelective(export);
        //循环更新商品信息
        if (export.getExportProducts() != null && export.getExportProducts().size() > 0) {
            for (ExportProduct exportProduct : export.getExportProducts()) {
                exportProductDao.updateByPrimaryKeySelective(exportProduct);
            }
        }
    }

    @Override
    public void delete(String id) {
        //通过id得到export对象
        Export export = exportDao.selectByPrimaryKey(id);
        String contractIds = export.getContractIds();
        //将报运单id分割成购销合同id数组，
        String[] contractIdsArray = contractIds.split(",");
        //循环购销合同数组
        for (String contractId : contractIdsArray) {
            //通过购销合同id得到购销合同
            Contract contract = contractDao.selectByPrimaryKey(contractId);
            //设置状态为1
            contract.setState(1);
            //更新合同
            contractDao.updateByPrimaryKeySelective(contract);
        }

        //删除商品
        //得到商品,已知报运单id和购销合同id
        ExportProductExample exportProductExample = new ExportProductExample();
        exportProductExample.createCriteria().andExportIdEqualTo(id);
        List<ExportProduct> exportProducts = exportProductDao.selectByExample(exportProductExample);
        //判断
        if (exportProducts != null && exportProducts.size() > 0) {
            for (ExportProduct exportProduct : exportProducts) {
                exportProductDao.deleteByPrimaryKey(exportProduct.getId());
            }
        }

        //删除商品附件
        ExtEproductExample extEproductExample = new ExtEproductExample();
        extEproductExample.createCriteria().andExportIdEqualTo(id);
        List<ExtEproduct> extEproducts = extEproductDao.selectByExample(extEproductExample);
        //判断
        if (extEproducts != null && extEproducts.size() > 0) {
            for (ExtEproduct extEproduct : extEproducts) {
                extEproductDao.deleteByPrimaryKey(extEproduct.getId());
            }
        }

        //删除报运单
        exportDao.deleteByPrimaryKey(id);
    }

    @Override
    public PageInfo<Export> findByPage(ExportExample example, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Export> list = exportDao.selectByExample(example);
        return new PageInfo<>(list);
    }

    @Override
    public void updateExport(ExportResult exportResult) {
        //根据返回的结果查询报运单
        Export export = exportDao.selectByPrimaryKey(exportResult.getExportId());
        //设置报运单状态和说明
        export.setRemark(exportResult.getRemark());
        export.setState(exportResult.getState());
        //更新报运单
        exportDao.updateByPrimaryKeySelective(export);

        //循环处理商品
        for (ExportProductResult epr : exportResult.getProducts()) {
            ExportProduct exportProduct = exportProductDao.selectByPrimaryKey(epr.getExportProductId());
            //对商品的税收的修改
            exportProduct.setTax(epr.getTax());
            exportProductDao.updateByPrimaryKeySelective(exportProduct);
        }
    }
}
