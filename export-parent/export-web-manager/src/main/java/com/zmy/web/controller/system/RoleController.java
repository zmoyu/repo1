package com.zmy.web.controller.system;

import com.github.pagehelper.PageInfo;
import com.zmy.domain.system.Module;
import com.zmy.domain.system.Role;
import com.zmy.service.system.ModuleService;
import com.zmy.service.system.RoleService;
import com.zmy.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/system/role")
public class RoleController extends BaseController {

    @Autowired
    private RoleService roleService;
    @Autowired
    private ModuleService moduleService;

    /**
     * 角色列表分页
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("/list")
    public String page(@RequestParam(defaultValue = "1") Integer pageNum,
                       @RequestParam(defaultValue = "5") Integer pageSize) {

        //调用service查询角色列表
        PageInfo<Role> pageInfo = roleService.findByPage(getLoginCompanyId(), pageNum, pageSize);

        //保存
        request.setAttribute("pageInfo", pageInfo);

        //返回
        return "system/role/role-list";
    }

    /**
     * 进入新增角色页面/system/role/toAdd.do
     */
    @RequestMapping("/toAdd")
    public String toAdd(String id) {

        return "system/role/role-add";
    }


    /**
     * 新增角色
     */
    @RequestMapping("/edit")
    public String edit(Role role) {

        role.setCompanyId(getLoginCompanyId());
        role.setCompanyName(getLoginCompanyId());
        //判断如果没有输入角色，及角色为空返回角色列表界面
        if (role == null || "".equals(role)) {
            return "redirect:/system/role/list.do";
        }

        //判断
        if (StringUtils.isEmpty(role.getId())) {
            //没有id，添加保存
            roleService.save(role);
        } else {
            //已有id，修改保存
            roleService.update(role);
        }
        return "redirect:/system/role/list.do";
    }

    /**
     * 进入到修改界面
     * 1.获取到id，根据id进行查询
     * 2.保存查询结果到request域中
     * 3.跳转到修改界面
     */
    @RequestMapping("/toUpdate")
    public String toUpdate(String id) {

        //根据id查询角色
        Role role = roleService.findById(id);

        //保存
        request.setAttribute("role", role);

        //返回
        return "system/role/role-update";
    }

    /**
     * 删除角色
     */
    @RequestMapping("/delete")
    public String delete(String id) {
        roleService.delete(id);

        //重定向到列表界面
        return "redirect:/system/role/list.do";
    }

    /**
     * j进入权限页面
     * /system/role/roleModule.do?roleId="+id;
     */
    @RequestMapping("/roleModule")
    public String roleModule(String roleId) {

        //通过id寻找用户，看权限
        Role role = roleService.findById(roleId);

        request.setAttribute("role", role);
        return "system/role/role-module";
    }

    /**
     * 6. 角色分配权限（2） role-module.jsp发送异步请求，返回ztree格式的数据
     * 功能入口：role-module.jsp 页面的异步请求
     * 请求地址：/system/role/getZtreeNodes.do
     * 请求参数：roleId
     * 返回格式：json
     * 返回数据：[
     * { id:2, pId:0, name:"随意勾选 2", checked:true, open:true}
     * ]
     */
    @RequestMapping("/getZtreeNodes")
    @ResponseBody
    //权限树的数据是一个数组【{ id:2, pId:0, name:"随意勾选 2", checked:true, open:true}，……】
    //是一个节点数组，每个节点的数据是个键值对集合，用map存储接收
    public List<Map<String, Object>> getZtreeNodes(String roleId) {

        List<Map<String, Object>> zNodes = new ArrayList<>();

        //查看所有权限
        List<Module> moduleList = moduleService.findAll();

        //2. 查询当前角色（roleId）已经拥有的权限
        List<Module> CurrentRoleModules = moduleService.findRoleModulesByRoleId(roleId);

        //3. 构造返回的数据
        for (Module module : moduleList) {
            //3.1 构造map集合
            //{ id:2, pId:0, name:"", checked:true, open:true}
            Map<String, Object> map = new HashMap<>();
            map.put("id", module.getId());
            map.put("pId", module.getParentId());
            map.put("name", module.getName());
            map.put("open", true);

            //判断是否选中，如果当前角色已有此权限（module对象），默认选中
            if (CurrentRoleModules.contains(module)) {
                map.put("checked", true);
            }
            //3.3 把封装好的map数据添加到集合
            zNodes.add(map);
        }
        //返回数据
        return zNodes;
    }

    /**
     * 6. 角色分配权限（3） 角色分配权限
     * 请求地址：/system/role/updateRoleModule.do
     * 请求参数：
     * roleId  角色id
     * moduleIds 多个权限id，用逗号隔开
     */
    @RequestMapping("/updateRoleModule")
    public String updateRoleModule(String roleId, String moduleIds) {
        roleService.updateRoleModule(roleId, moduleIds);

        return "redirect:/system/role/list.do";
    }

}
