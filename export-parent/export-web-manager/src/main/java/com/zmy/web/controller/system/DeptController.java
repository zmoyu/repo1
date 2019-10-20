package com.zmy.web.controller.system;

import com.github.pagehelper.PageInfo;
import com.zmy.domain.system.Dept;
import com.zmy.service.system.DeptService;
import com.zmy.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/system/dept")
public class DeptController extends BaseController {

    @Autowired
    private DeptService deptService;

    // @RequestParam 建立请求参数与方法形参的对应关系
    //http://localhost:8080/system/dept/test.do?aa=100   // aa 请求参数
    /*@RequestMapping("/test")
    public void a(@RequestParam("aa") Integer a){// a  表示方法形参
        // result = 100
    }*/

    /**
     * 1. 分页查询
     */
    @RequestMapping("list")
    public String list(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "5") Integer pageSize) {

        //企业id，先写死。后面实现完登陆后再从登陆用户那里获取用户所属企业。
        String companyId = getLoginCompanyId();
        //调用service实现分页
        PageInfo<Dept> pageInfo = deptService.findByPage(companyId, pageNum, pageSize);

        //返回
       request.setAttribute("pageInfo", pageInfo);
        return ("system/dept/dept-list");
    }

    /**
     * 2. 进入添加页面
     */
    @RequestMapping("/toAdd")
    public String insert(Model model) {
        //查询所有部门，作为添加页面的父部门下拉列表显示
        List<Dept> deptList = deptService.findAllDept(getLoginCompanyId());

        //保存
        model.addAttribute("deptList", deptList);
        return "system/dept/dept-add";
    }


    /**
     * 添加保存
     */
    @RequestMapping("/edit")
    public String edit(Dept dept) {

        //设置企业信息
        dept.setCompanyId(getLoginCompanyId());
        dept.setCompanyName(getLoginCompanyName());

        //判断
        if (StringUtils.isEmpty(dept.getId())) {
            deptService.save(dept);
        } else {
            deptService.update(dept);
        }
        // 重定向到列表
        return "redirect:/system/dept/list.do";
    }

    /**
     * /system/dept/toUpdate.do?id=${dept.id}
     */
    @RequestMapping("/toUpdate")
    public String toUpdate(String id, Model model) {

        //根据部门id查询
        Dept dept = deptService.findById(id);// 需要补充完整

        //查询所有部门
        List<Dept> deptList = deptService.findAllDept(getLoginCompanyId());

        //保存
        model.addAttribute("dept", dept);
        model.addAttribute("deptList", deptList);

        return "/system/dept/dept-update";
    }

    /**
     * 5. 删除
     * 返回数据：{message:""}
     */
    @RequestMapping("/delete")
    @ResponseBody     // 自动把方法返回的对象，转换为json格式(jackson)
    public Map<String, String> delete(String id) {

        //返回的数据
        Map<String, String> result = new HashMap<>();

        //2.删除数据
        boolean flag = deptService.delete(id);

        //3.判断
        if (flag) {
            //删除成功
            result.put("message", "删除成功");
        } else {
            result.put("message", "删除失败，当前删除的部门有子部门。请先解除关系再删除！");
        }
        return result;
    }
}
