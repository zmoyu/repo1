package com.zmy.web.controller;

import com.zmy.domain.system.User;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class BaseController {

    //注入request
    @Autowired
    protected HttpServletRequest request;
    @Autowired
    protected HttpSession session;
    @Autowired
    protected HttpServletResponse response;

    /**
     * 返回登陆用户企业d的id
     */
    public String getLoginCompanyId() {
        return getLoginUser().getCompanyId();
    }

    /**
     * 返回登录用户企业的名称
     */
    public String getLoginCompanyName() {
        return getLoginUser().getCompanyName();
    }

    /**
     * 从session中获取登陆用户
     */
    public User getLoginUser() {
        return (User) session.getAttribute("loginUser");
    }
}
