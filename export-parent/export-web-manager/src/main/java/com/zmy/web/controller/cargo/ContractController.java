package com.zmy.web.controller.cargo;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.zmy.domain.cargo.Contract;
import com.zmy.domain.cargo.ContractExample;
import com.zmy.domain.system.User;
import com.zmy.service.cargo.ContractService;
import com.zmy.web.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/cargo/contract")
public class ContractController extends BaseController {

    // 注入购销合同的服务接口
    @Reference
    private ContractService contractService;

    /*列表分页查询*/
    @RequestMapping("/list")
    public String list(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "5") Integer pageSize) {

        //1.1 构造查询条件
        ContractExample example = new ContractExample();
        // 根据create_time进行降序
        example.setOrderByClause("create_time desc");

        //1.2 查询条件对象
        ContractExample.Criteria criteria = example.createCriteria();
        //1.3 查询条件: 企业id
        criteria.andCompanyIdEqualTo(getLoginCompanyId());

        /**
         * 构造条件-细粒度权限控制。根据登陆用户显示购销合同
         * 用户级别：
         *   0-saas管理员
         *   1-企业管理员
         *   2-管理所有下属部门和人员
         *   3-管理本部门
         *   4-普通员工
         */
        //获取登陆用户对象
        User loginUser = getLoginUser();
        //用户等级
        //判断
        if (loginUser.getDegree() == 4) {
            //普通用户
            criteria.andCreateByEqualTo(loginUser.getId());
        } else if (loginUser.getDegree() == 3) {
            //管理本部门
            criteria.andCreateDeptEqualTo(loginUser.getDeptId());
        }else if(loginUser.getDegree() == 2){
            //-- 3) 大部门经理登陆，degree=2，能查看本部门及所有子部门员工创建的购销合同
            //SELECT * FROM co_contract WHERE FIND_IN_SET(create_dept,getDeptChild('100'))
            PageInfo<Contract> pageInfo =
                    contractService.selectByDeptId(loginUser.getDeptId(), pageNum, pageSize);
            request.setAttribute("pageInfo",pageInfo);
            return "cargo/contract/contract-list";
        }

        //1.2 调用service查询
        PageInfo<Contract> pageInfo =
                contractService.findByPage(example, pageNum, pageSize);
        request.setAttribute("pageInfo", pageInfo);
        //返回
        return "cargo/contract/contract-list";

    }


    /*进入添加页面*/
    @RequestMapping("toAdd")
    public String toAdd() {

        return "cargo/contract/contract-add";
    }

    /**
     * 3. 添加/修改
     */
    @RequestMapping("/edit")
    public String edit(Contract contract) {
        // 设置所属企业id、名称
        contract.setCompanyId(getLoginCompanyId());
        contract.setCompanyName(getLoginCompanyName());
        // 判断
        if (StringUtils.isEmpty(contract.getId())) {
            /*需求：需要根据登陆用户显示当前登陆用户创建的购销合同。记录*/
            //设置创建人
            contract.setCreateBy(getLoginUser().getId());
            //设置创建者部门
            contract.setCreateDept(getLoginUser().getDeptId());

            // 添加
            contractService.save(contract);
        } else {
            // 修改
            contractService.update(contract);
        }
        return "redirect:/cargo/contract/list.do";
    }


    /**
     * 4. 进入修改页面
     */
    @RequestMapping("/toUpdate")
    public String toUpdate(String id) {
        // 根据id查询
        Contract contract = contractService.findById(id);
        // 保存
        request.setAttribute("contract", contract);
        // 转发到页面
        return "cargo/contract/contract-update";
    }

    /**
     * 5. 删除
     */
    @RequestMapping("/delete")
    public String delete(String id) {
        contractService.delete(id);
        return "redirect:/cargo/contract/list.do";
    }

    /**
     * 查看：http://localhost:8080/cargo/contract/toView.do?id=4
     * 提交：http://localhost:8080/cargo/contract/submit.do?id=4
     * 取消：http://localhost:8080/cargo/contract/cancel.do?id=4
     */
    @RequestMapping("toView")
    public String toView(String id) {
        Contract contract = contractService.findById(id);
        request.setAttribute("contract", contract);
        return "cargo/contract/contract-view";
    }

    @RequestMapping("submit")
    public String submit(String id) {
        /*提交： state状态改为1*/
        Contract contract = new Contract();
        contract.setState(1);
        contract.setId(id);

        //动态更新
        contractService.update(contract);
        return "redirect:/cargo/contract/list.do";
    }

    @RequestMapping("cancel")
    public String cancel(String id) {
        /*取消： state状态改为0*/
        Contract contract = new Contract();
        contract.setId(id);
        contract.setState(0);

        //动态更新
        contractService.update(contract);
        return "redirect:/cargo/contract/list.do";
    }

}
