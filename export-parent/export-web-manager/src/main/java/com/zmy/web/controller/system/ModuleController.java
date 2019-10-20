package com.zmy.web.controller.system;

import com.github.pagehelper.PageInfo;
import com.zmy.domain.system.Module;
import com.zmy.service.system.ModuleService;
import com.zmy.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/system/module")
public class ModuleController extends BaseController {

    @Autowired
    private ModuleService moduleService;

    /**
     * 分页查询
     */
    @RequestMapping("/list")
    public ModelAndView list(@RequestParam(defaultValue = "1")Integer pageNum,
                             @RequestParam(defaultValue = "5")Integer pageSize ){
        //1.调用service查询部门列表并进行分页
        PageInfo<Module> pageInfo = moduleService.findByPage(pageNum, pageSize);

        //保存
        ModelAndView mav = new ModelAndView();
        mav.addObject("pageInfo",pageInfo);
        mav.setViewName("/system/module/module-list");
        //返回
        return mav;
    }

    /**
     * 进入添加页面
     */
    @RequestMapping("/toAdd")
    public String toAdd(){
        //1.查询所用模块数据，为了构造下拉框数据
        List<Module> moduleList = moduleService.findAll();

        //保存
        request.setAttribute("menus",moduleList);

        //返回
        return "system/module/module-add";
    }

    /**
     * 添加保存
     */
    @RequestMapping("/edit")
    public String edit(Module module){

        //判断
        if(StringUtils.isEmpty(module.getId())){
            //没有id，添加保存
            moduleService.save(module);
        }else {
            //已有id，修改保存
            moduleService.update(module);
        }

        //重定向到列表页面
        return "redirect:/system/module/list.do";
    }

    /**
     * 进入到修改界面
     *  1.获取到id
     *  2.根据id进行查询
     *  3.查询所有的模块
     *  4.保存到request域中
     *  5.跳转到修改界面
     */
    @RequestMapping("/toUpdate")
    public String toUpdate(String id){

        //查询所有数据，显示出下拉框数据
        List<Module> moduleList = moduleService.findAll();
        //根据id进行查询
        Module module = moduleService.findById(id);

        //保存
        request.setAttribute("module",module);
        request.setAttribute("menus",moduleList);

        //返回到system/module/module-update页面
        return  "system/module/module-update";
    }

    /**
     * 删除模块
     */
    @RequestMapping("/delete")
    public String delete(String id){
        moduleService.delete(id);

        //跳转到
        return "redirect:/system/module/list.do";
    }
}
