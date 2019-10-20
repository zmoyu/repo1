package com.zmy.web.controller;


import com.zmy.domain.system.Module;
import com.zmy.domain.system.Role;
import com.zmy.domain.system.User;
import com.zmy.service.system.ModuleService;
import com.zmy.service.system.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * 登录
 */
@Controller
public class LoginController extends BaseController {

    @Autowired
    private UserService userService;

    /**
     * 执行流程
     * 1. 访问首页
     * http://localhost:8080/index.jsp
     * 2. index.jsp
     * location.href = "login.do";
     * 3. 转发到main.jsp
     */
   /* @RequestMapping("/login")
    public String login(String email, String password) {
        //判断用户输入数据是否为空
        if (StringUtils.isEmpty(email) || StringUtils.isEmpty(password)) {
            //返回登陆页面
            return "forward:/login.jsp";
        }
        //根据用户输入的邮箱查找用户（方便以后一个邮箱可以注册多个用户的情况，扩展性强，所以用list）
        List<User> list = userService.findByEmail(email);

        //判断
        if (list != null && list.size() > 0) {
            //等到用户
            User user = list.get(0);
            //判断密码
            if (password.equals(user.getPassword())) {
                //保存到域中
                session.setAttribute("loginUser", user);

                //登陆成功，获取当前用户所拥有的权限（菜单）
                List<Module> modules = userService.findUserModuleByUserId(user.getId());
                session.setAttribute("modules",modules);

                // 登陆成功, 跳转到主页main.jsp
                return "home/main";
            }
        }
        //登陆失败，返回登陆页面
        request.setAttribute("error", "用户名或密码错误");
        return "forward:/login.jsp";
    }*/

    /**
     * 通过shiro实现登陆认证
     */
    @RequestMapping("/login")
    public String login(String email, String password) {
        //A.判断
        if (StringUtils.isEmpty(email) && StringUtils.isEmpty(password)) {
            //跳转到登陆页面
            return "forward:/login.jsp";
        }
        try {
            // B. shiro登陆认证
            // B1. 创建Subject对象
            Subject subject = SecurityUtils.getSubject();
            // B2. 创建token对象，封装用户名、密码
            UsernamePasswordToken token = new UsernamePasswordToken(email, password);
            // B3  登陆认证 (自动去到realm中的认证方法)
            subject.login(token);

            // C. 登陆认证成功
            // 获取用户的身份对象（对应realm中认证方法返回的对象的构造函数的第一个参数）
            User user = (User) subject.getPrincipal();
            //将用户存储到session域中
            session.setAttribute("loginUser", user);

            //查询当前登录用户所拥有的权限（菜单数据）
            List<Module> modules = userService.findUserModuleByUserId(user.getId());
            session.setAttribute("modules", modules);
            // 登陆成功, 跳转到主页main.jsp
            return "home/main";
        } catch (AuthenticationException e) {
            e.printStackTrace();
            // 登陆失败, 跳转到主页login.jsp
            request.setAttribute("error", "用户名或密码错误！");
            return "forward:/login.jsp";
        }
    }

    /**
     * 用户注销登陆
     */
    @RequestMapping("/logout")
    public String logout() {
        //shiro也提供了退出方法。（清除shiro验证信息）
        SecurityUtils.getSubject().logout();
        //删除session中用户
        session.removeAttribute("loginUser");
        //销毁session
        // session.invalidate();

        //返回登陆页面
        return "forward:/login.jsp";
    }

    /**
     * 4. main.jsp 中
     * <iframe src="/home.do">
     */
    @RequestMapping("/home")
    public String home() {

        return "home/home";
    }


}
