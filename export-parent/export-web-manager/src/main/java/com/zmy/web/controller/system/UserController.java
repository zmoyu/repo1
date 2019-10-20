package com.zmy.web.controller.system;

import com.github.pagehelper.PageInfo;
import com.zmy.domain.system.Dept;
import com.zmy.domain.system.Role;
import com.zmy.domain.system.User;
import com.zmy.service.system.DeptService;
import com.zmy.service.system.RoleService;
import com.zmy.service.system.UserService;
import com.zmy.web.controller.BaseController;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/system/user")
public class UserController extends BaseController {

    @Autowired
    private DeptService deptService;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 分页查询
     */
    @RequestMapping("list")
    public String list(@RequestParam(defaultValue = "1") Integer pageNum,
                       @RequestParam(defaultValue = "5") Integer pageSize) {

        PageInfo<User> pageInfo = userService.findByPage(getLoginCompanyId(), pageNum, pageSize);

        //返回
        request.setAttribute("pageInfo", pageInfo);

        return "system/user/user-list";
    }

    /**
     * 进入添加页面
     */
    @RequestMapping("/toAdd")
    public String toAdd() {

        //根据公司id查询所有部门
        List<Dept> deptList = deptService.findAllDept(getLoginCompanyId());

        //保存
        request.setAttribute("deptList", deptList);

        return "system/user/user-add";
    }

    /**
     * 添加保存
     * /system/user/edit.do
     */
    @RequestMapping("/edit")
    public String edit(User user) {
        // 企业id、名称先写死。后面从登陆用户中获取。(我们是靠企业来查询的)
        user.setCompanyId(getLoginCompanyId());
        user.setCompanyName(getLoginCompanyName());

        //根据id判断修改还是添加
        if (StringUtils.isEmpty(user.getId())) {
            userService.save(user);
            //添加用户成功，发送邮件
            if (user.getEmail() != null && !"".equals(user.getEmail())) {
                String to = user.getEmail();
                //处理发送邮件的业务
                String subject = "新员工入职通知";
                String content = "欢迎你来到SaasExport大家庭，我们是一个充满激情的团队，不是996哦！";
                Map<String, Object> map = new HashMap<>();
                map.put("to", to);
                map.put("subject", subject);
                map.put("content", content);
                rabbitTemplate.convertAndSend("msg.email", map);
            }
        } else {
            userService.update(user);
        }

        // 添加成功，重定向到列表
        return "redirect:/system/user/list.do";
    }

    /**
     * 修改页面
     * location.href="/system/user/toUpdate.do?id=${item.id}
     */
    @RequestMapping("toUpdate")
    public String toUpdate(String id) {
        //根据企业id查询用户
        User user = userService.findById(id);

        // // 查询所有部门，作为修改页面的父部门下拉列表显示。
        List<Dept> deptList = deptService.findAllDept(getLoginCompanyId());

        //保存
        request.setAttribute("deptList", deptList);
        request.setAttribute("user", user);

        //转发
        return "/system/user/user-update";
    }

    /**
     * 删除
     * location.href="/system/user/delete.do?id="+id;
     */
    @RequestMapping("/delete")
    @ResponseBody
    public Map<String, String> delete(String id) {

        Map<String, String> result = new HashMap<>();

        boolean flag = userService.delete(id);

        if (flag) {
            // 删除成功
            result.put("message", "删除成功!");
        } else {
            result.put("message", "删除失败，当前删除的用户有对应角色。请先解除关系再删除！");
        }
        // 添加成功，重定向到列表
        return result;
    }

    /**
     * 进入角色列表页面
     * location.href="/system/user/roleList.do?id="+id;
     */
    @RequestMapping("/roleList")
    public String roleList(String id) {
        //1.根据id查找用户
        User user = userService.findById(id);

        //2.通过company_id可以查询出查所有角色（角色列表）
        List<Role> roleList = roleService.findAll(getLoginCompanyId());

        //3.根据用户id查询用户已经具有的所有的角色集合
        List<Role> userRoleList = roleService.findUserRole(id);
        //定义一个角色字符串，保存用户的所有角色id
        String userRoleStr = ""; // userRoleIds="1,2,";userRoleIds.contains("1") 选中
        if (userRoleList != null && userRoleList.size() > 0) {
            for (Role role : userRoleList) {
                userRoleStr += role.getId() + ",";
            }
        }
        //保存查询结果
        request.setAttribute("user", user);
        request.setAttribute("roleList", roleList);
        request.setAttribute("userRoleStr", userRoleStr);
        //转发到user-role页面
        return "system/user/user-role";
    }

    /**
     * 修改保存用户角色信息,实现给用户分配角色信息
     * document.icform.action="/system/user/changeRole.do";
     */
    @RequestMapping("/changeRole")
    public String changeRole(String userId, String[] roleIds) {

        //调用userService分配角色
        userService.updateUserRoles(userId, roleIds);
        //重定向到列表页面
        return "redirect:/system/user/list.do";
    }
}
