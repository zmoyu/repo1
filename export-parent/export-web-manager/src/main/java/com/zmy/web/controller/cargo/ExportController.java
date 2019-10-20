package com.zmy.web.controller.cargo;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.zmy.domain.cargo.*;
import com.zmy.service.cargo.ContractService;
import com.zmy.service.cargo.ExportProductService;
import com.zmy.service.cargo.ExportService;
import com.zmy.vo.ExportProductVo;
import com.zmy.vo.ExportResult;
import com.zmy.vo.ExportVo;
import com.zmy.web.controller.BaseController;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/cargo/export")
public class ExportController extends BaseController {

    @Reference
    private ContractService contractService;
    @Reference
    private ExportService exportService;
    @Reference
    private ExportProductService exportProductService;

    /**
     * 进入合同管理页面
     * /cargo/export/contractList.do
     */
    @RequestMapping("/contractList")
    public String contractList(@RequestParam(defaultValue = "1") Integer pageNum,
                               @RequestParam(defaultValue = "5") Integer paeSize) {
        //查询所有合同
        ContractExample example = new ContractExample();
        ContractExample.Criteria criteria = example.createCriteria();
        criteria.andCompanyIdEqualTo(getLoginCompanyId());

        //判断，如果状态为1，即已上报则分页显示
        criteria.andStateEqualTo(1);
        PageInfo<Contract> pageInfo = contractService.findByPage(example, pageNum, paeSize);
        request.setAttribute("pageInfo", pageInfo);

        return "cargo/export/export-contractList";
    }

    /**
     * 显示报运单列表
     */
    @RequestMapping("/list")
    public String list(@RequestParam(defaultValue = "1") Integer pageNum,
                       @RequestParam(defaultValue = "5") Integer pageSize) {
        ExportExample exportExample = new ExportExample();
        ExportExample.Criteria criteria = exportExample.createCriteria();
        criteria.andCompanyIdEqualTo(getLoginCompanyId());

        PageInfo<Export> pageInfo = exportService.findByPage(exportExample, pageNum, pageSize);
        request.setAttribute("pageInfo", pageInfo);

        return "cargo/export/export-list";
    }

    /**
     * 进入新增报运单页面
     * /cargo/export/toExport.do
     */
    @RequestMapping("/toExport")
    public String toExport(String id) {
        request.setAttribute("id", id);
        return "cargo/export/export-toExport";
    }

    /**
     * 新增保存报运单
     * /cargo/export/edit.do
     */
    @RequestMapping("/edit")
    public String edit(Export export) {
        //设置公司Id和名称
        export.setCompanyId(getLoginCompanyId());
        export.setCompanyName(getLoginCompanyName());
        //判断
        if (StringUtils.isEmpty(export.getId())) {
            exportService.save(export);
        } else {
            exportService.update(export);
        }

        return "redirect:/cargo/export/list.do";
    }

    /**
     * 进入修改页面
     * /cargo/export/toUpdate.do?id=${o.id}
     * 显示报运单信息，以及报运单下的所有商品
     */
    @RequestMapping("/toUpdate")
    public String toUpdate(String id) {
        //根据报运单id获取报运单信息
        Export export = exportService.findById(id);
        //根据报运单对象得到所有商品信息
        ExportProductExample exportProductExample = new ExportProductExample();
        exportProductExample.createCriteria().andExportIdEqualTo(id);
        List<ExportProduct> eps = exportProductService.findAll(exportProductExample);

        request.setAttribute("export", export);
        request.setAttribute("eps", eps);
        request.setAttribute("id", id);
        return "cargo/export/export-update";
    }

    /**
     * 删除
     * location.href="${ctx}/cargo/export/delete.do?id="+id;
     */
    @RequestMapping("/delete")
    public String delete(String id) {
        exportService.delete(id);
        return "redirect:/cargo/export/list.do";
    }

    /**
     * 提交
     * location.href="${ctx}/cargo/export/submit.do?id="+id;
     */
    @RequestMapping("/submit")
    public String submit(String id) {
        Export export = exportService.findById(id);
        export.setId(id);
        export.setState(1);

        exportService.update(export);
        return "redirect:/cargo/export/list.do";
    }

    /**
     * 取消
     * location.href="${ctx}/cargo/export/cancel.do?id="+id;
     */
    @RequestMapping("/cancel")
    public String cancel(String id) {
        Export export = exportService.findById(id);
        export.setId(id);
        export.setState(0);

        exportService.update(export);
        return "redirect:/cargo/export/list.do";
    }

    /**
     * 电子报运
     * location.href="${ctx}/cargo/export/exportE.do?id="+id
     */
    @RequestMapping("/exportE")
    public String exportE(String id) {
        //1.根据id获取报运单对象
        Export export = exportService.findById(id);
        //2.根据报运单id查询商品列表
        ExportProductExample exportProductExample = new ExportProductExample();
        exportProductExample.createCriteria().andExportIdEqualTo(id);
        List<ExportProduct> exportProducts = exportProductService.findAll(exportProductExample);

        //3.构造电子报运的vo对象，赋值
        ExportVo exportVo = new ExportVo();
        BeanUtils.copyProperties(export, exportVo);
        //设置vo对象中的报运单属性
        exportVo.setExportId(exportVo.getId());

        //4.将商品属性赋值给vo对象(封装构造报运商品属性)
        List<ExportProductVo> products = new ArrayList<>();
        for (ExportProduct exportProduct : exportProducts) {
            ExportProductVo exportProductVo = new ExportProductVo();
            BeanUtils.copyProperties(exportProduct, exportProductVo);
            //设置商品报运单vo中的商品id属性
            exportProductVo.setExportProductId(exportProduct.getId());
            products.add(exportProductVo);
        }
        //设置报运单vo中的商品集合属性
        exportVo.setProducts(products);

        //5.电子报运
        WebClient client = WebClient.create("http://localhost:9001/ws/export/user");
        client.post(exportVo);
        //6.查询报运结果
        client = WebClient.create("http://localhost:9001/ws/export/user/" + id);
        ExportResult exportResult = client.get(ExportResult.class);

        //7.调用service完成报运结果的入库
        exportService.updateExport(exportResult);

        return "redirect:/cargo/export/list.do";
    }

    /**
     * 查看
     * ${ctx }/cargo/export/toView.do?id=${o.id}
     */
    @RequestMapping("/toView")
    public String toView(String id) {
        Export export = exportService.findById(id);
        request.setAttribute("export", export);
        return "cargo/export/export-view";
    }
}
