package com.zmy.web.controller.cargo;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.zmy.domain.cargo.ContractProduct;
import com.zmy.domain.cargo.ContractProductExample;
import com.zmy.domain.cargo.Factory;
import com.zmy.domain.cargo.FactoryExample;
import com.zmy.service.cargo.ContractProductService;
import com.zmy.service.cargo.FactoryService;
import com.zmy.web.controller.BaseController;
import com.zmy.web.utils.FileUploadUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/cargo/contractProduct")
public class ContractProductController extends BaseController {

    @Reference
    private ContractProductService contractProductService;
    @Reference
    private FactoryService factoryService;
    @Autowired
    private FileUploadUtil fileUploadUtil;

    //进入货物列表页面
    @RequestMapping("list")
    public ModelAndView list(String contractId, @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "3") Integer pageSize) {

        //A根据购销合同id查询货物
        ContractProductExample cpExample = new ContractProductExample();
        ContractProductExample.Criteria criteria = cpExample.createCriteria();
        criteria.andContractIdEqualTo(contractId);
        //分页查询所有货物
        PageInfo<ContractProduct> pageInfo = contractProductService.findByPage(cpExample, pageNum, pageSize);

        //B查询厂家
        FactoryExample factoryExample = new FactoryExample();
        FactoryExample.Criteria factoryCriteria1 =
                factoryExample.createCriteria().andCtypeEqualTo("货物");
        List<Factory> factoryList = factoryService.findAll(factoryExample);

        //保存
        ModelAndView mav = new ModelAndView();
        mav.addObject("pageInfo", pageInfo);
        mav.addObject("factoryList", factoryList);
        mav.addObject("contractId", contractId);
        mav.setViewName("cargo/product/product-list");
        //返回
        return mav;
    }

    //添加或修改货物
    @RequestMapping("edit")
    public String edit(ContractProduct contractProduct, MultipartFile productPhoto) {
        //设置登陆用户的公司id和名称
        contractProduct.setCompanyId(getLoginCompanyId());
        contractProduct.setCompanyName(getLoginCompanyName());

        //判断是否具有id属性
        if (StringUtils.isEmpty(contractProduct.getId())) {
            /*货物图片上传到七牛云*/
            try {
                if (productPhoto != null) {
                    String imgUrl = "http://" + fileUploadUtil.upload(productPhoto);
                    contractProduct.setProductImage(imgUrl);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //没有id，保存
            contractProductService.save(contractProduct);
        } else {
            //有id,修改
            contractProductService.update(contractProduct);
        }
        return "redirect:/cargo/contractProduct/list.do?contractId=" + contractProduct.getContractId();
    }

    //进入修改页面
    @RequestMapping("toUpdate")
    public String toUpdate(String id) {
        //根据货物id查询货物信息
        ContractProduct contractProduct = contractProductService.findById(id);

        //查询厂家
        FactoryExample example = new FactoryExample();
        FactoryExample.Criteria criteria = example.createCriteria();
        criteria.andCtypeEqualTo("货物");
        List<Factory> factoryList = factoryService.findAll(example);

        request.setAttribute("contractProduct", contractProduct);
        request.setAttribute("factoryList", factoryList);

        return "cargo/product/product-update";
    }

    /**
     * 删除货物
     * /cargo/contractProduct/delete.do?id=${o.id}&contractId=${o.contractId}
     */
    @RequestMapping("delete")
    public String delete(String id, String contractId) {
        contractProductService.delete(id);
        return "redirect:/cargo/contractProduct/list.do?contractId=" + contractId;
    }

    /**
     * 进入货物上传页面
     * http://localhost:6080/cargo/contractProduct/toImport.do?contractId=9
     */
    @RequestMapping("/toImport")
    public String toImport(String contractId) {
        request.setAttribute("contractId", contractId);
        return "cargo/product/product-import";
    }

    /**
     * 上传货物
     * /cargo/contractProduct/import.do
     */
    @RequestMapping("/import")
    public String importExcel(MultipartFile file, String contractId) throws Exception {
        //根据创建的文件创建工作簿
        Workbook workbook = new XSSFWorkbook(file.getInputStream());

        //获取工作表
        Sheet sheet = workbook.getSheetAt(0);
        //获取总行数
        int numberOfRows = sheet.getPhysicalNumberOfRows();
        //获取企业名称和id
        String companyId = getLoginCompanyId();
        String companyName = getLoginCompanyName();

        //获取每一行
        for (int i = 1; i < numberOfRows; i++) {
            //获取每一行
            Row row = sheet.getRow(i);
            //获取每一行的每一列设置到属性当中
            ContractProduct contractProduct = new ContractProduct();
            //生产厂家  货号  数量  包装单位  装率  箱数  单价  货物描述 要求
            contractProduct.setFactoryName(row.getCell(1).getStringCellValue());
            contractProduct.setProductNo(row.getCell(2).getStringCellValue());
            contractProduct.setCnumber((int) row.getCell(3).getNumericCellValue());
            contractProduct.setPackingUnit(row.getCell(4).getStringCellValue());
            contractProduct.setLoadingRate(row.getCell(5).getNumericCellValue()+"");
            contractProduct.setBoxNum((int) row.getCell(6).getNumericCellValue());
            contractProduct.setPrice(row.getCell(7).getNumericCellValue());
            contractProduct.setProductDesc(row.getCell(8).getStringCellValue());
            contractProduct.setProductRequest(row.getCell(9).getStringCellValue());
            //设置购销合同id
            contractProduct.setContractId(contractId);
            //设置企业
            contractProduct.setCompanyId(companyId);
            contractProduct.setCompanyName(companyName);
            //设置工厂id
            Factory factory =
                    factoryService.findFactoryByName(contractProduct.getFactoryName());
            if (factory != null) {
                contractProduct.setFactoryId(factory.getId());
            }

            //调用service保存货物
            contractProductService.save(contractProduct);
        }

        return "cargo/product/product-import";
    }

}
