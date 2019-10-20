package com.zmy.web.controller.cargo;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.zmy.domain.cargo.ExtCproduct;
import com.zmy.domain.cargo.ExtCproductExample;
import com.zmy.domain.cargo.Factory;
import com.zmy.domain.cargo.FactoryExample;
import com.zmy.service.cargo.ExtCproductService;
import com.zmy.service.cargo.FactoryService;
import com.zmy.web.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/cargo/extCproduct")
public class ExtCproductController extends BaseController {

    @Reference
    private ExtCproductService extCproductService;
    @Reference
    private FactoryService factoryService;

    //进入附件列表和附件添加页面
    @RequestMapping("/list")
    public String list(String contractId, String contractProductId,
                       @RequestParam(defaultValue = "1") Integer pageNum,
                       @RequestParam(defaultValue = "5") Integer pageSize) {
        //查询附件的生产厂家
        FactoryExample factoryExample = new FactoryExample();
        factoryExample.createCriteria().andCtypeEqualTo("附件");
        List<Factory> factoryList = factoryService.findAll(factoryExample);

        //根据货物id查询出所有附件
        ExtCproductExample extCproductExample = new ExtCproductExample();
        extCproductExample.createCriteria().andContractProductIdEqualTo(contractProductId);

        PageInfo<ExtCproduct> pageInfo =
                extCproductService.findByPage(extCproductExample, pageNum, pageSize);

        request.setAttribute("factoryList", factoryList);
        request.setAttribute("pageInfo", pageInfo);
        request.setAttribute("contractId", contractId);
        request.setAttribute("contractProductId",contractProductId);

        //返回
        return "cargo/extc/extc-list";
    }

    /**
     * 修改附件
     * /cargo/extCproduct/edit.do
     * /cargo/extCproduct/list.do?contractId=${o.contractId}&contractProductId=${o.id}
     */
    @RequestMapping("/edit")
    public String edit(ExtCproduct extCproduct) {
        //设置附件所属的企业name和id
       // extCproduct.setContractId(getLoginCompanyId());
       // extCproduct.setCompanyName(getLoginCompanyName());

        //判断
        if(StringUtils.isEmpty(extCproduct.getId())){
            //没有id，保存
            extCproductService.save(extCproduct);
        }else {
            //有id，修改
            extCproductService.update(extCproduct);
        }
        //返回列表页面
        return "redirect:/cargo/extCproduct/list.do?contractId="+extCproduct.getContractId()+"&contractProductId="+extCproduct.getContractProductId();
    }

    /**
     * 进入修改页面
     * /cargo/extCproduct/toUpdate.do?
     * id=${o.id}&contractId=${contractId}&contractProductId=${o.contractProductId}
     */
    @RequestMapping("/toUpdate")
    public String toUpdate(String id,String contractId,String contractProductId){
        //查询附件
        ExtCproduct extCproduct = extCproductService.findById(id);
        request.setAttribute("extCproduct",extCproduct);

        //查询附件生产厂家
        FactoryExample factoryExample = new FactoryExample();
        factoryExample.createCriteria().andCtypeEqualTo("附件");
        List<Factory> factoryList = factoryService.findAll(factoryExample);
        request.setAttribute("factoryList",factoryList);

        request.setAttribute("contractId",contractId);
        request.setAttribute("contractProductId",contractProductId);

        return "cargo/extc/extc-update";
    }

    /**
     * 删除附件
     */
    @RequestMapping("/delete")
    public String delete(String id,String contractId,String contractProductId){
        extCproductService.delete(id);
        return "redirect:/cargo/extCproduct/list.do?contractId="+contractId+"&contractProductId="+contractProductId;
    }
}
