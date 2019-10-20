package com.zmy.web.controller.company;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.zmy.domain.company.Company;
import com.zmy.service.company.CompanyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/company")
public class CompanyController {

    @Reference
    private CompanyService companyService;

    @RequestMapping("/list")
    public ModelAndView list(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "5") Integer pageSize) {

        ModelAndView mav = new ModelAndView();

        PageInfo<Company> pageInfo = companyService.findByPage(pageNum, pageSize);

        mav.addObject("pageInfo", pageInfo);

        mav.setViewName("/company/company-list");

        return mav;
    }

    /**
     * 2. 进入添加页面
     * 功能入口： 企业列表点击新建
     * 请求地址：http://localhost:8080/company/toAdd.do
     * 响应地址：/WEB-INF/pages/company/company-add.jsp
     */
    @RequestMapping("/toAdd")
    public String toAdd() {

        return "company/company-add";
    }

    /**
     * 3. 添加企业/修改企业
     */
    @RequestMapping("/edit")
    public String edit(Company company) {

        //根据id判断添加还是修改
        if (StringUtils.isEmpty(company.getId())) {
            //如果id为空，那么保存
            companyService.save(company);
        } else {
            //如果id不为空，那么修改
            companyService.update(company);
        }

        //添加成功，返回到列表页面
        return "redirect:/company/list.do";
    }

    /**
     * /**
     * * 4. 进入修改页面
     * * 功能入口：company-list.jsp 列表点击编辑
     * * 请求地址：http://localhost:8080/company/toUpdate.do
     * * 请求参数：id  修改的企业id
     * * 响应地址：/WEB-INF/pages/company/company-update.jsp
     */

    @RequestMapping("/toUpdate")
    public String toUpdate(String id, Model model) {

        //根据id查询企业
        Company company = companyService.findById(id);

        //保存
        model.addAttribute("company", company);

        //转发
        return "company/company-update";
    }

    /**
     * 请求地址：http//localhost：8080/company/delete.do?id=1
     *
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    public String delete(String id) {

        companyService.toDelete(id);

        return "redirect:/company/list.do";
    }
}
