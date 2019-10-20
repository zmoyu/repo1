package com.zmy.web.controller.system;


import com.github.pagehelper.PageInfo;
import com.zmy.domain.system.SysLog;
import com.zmy.service.system.SysLogService;
import com.zmy.web.controller.BaseController;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/system/log")
public class SysLogController extends BaseController {

    @Autowired
    private SysLogService logService;

    /**
     * 分页查询
     *
     * @return
     */
    @RequestMapping("/list.do")
    @RequiresPermissions("日志管理")
    public String getLog(@RequestParam(defaultValue = "1") Integer pageNum,
                         @RequestParam(defaultValue = "5") Integer pageSize) {

        /**
         * shiro权限校验
         */
       /* Subject subject = SecurityUtils.getSubject();
        subject.checkPermission("日志管理");*/

        //调用logService查询数据
        PageInfo<SysLog> pageInfo = logService.findByPage(getLoginCompanyId(), pageNum, pageSize);

        //将数据存储到域中
        request.setAttribute("pageInfo",pageInfo);

        //返回转发到log-list界面
        return "system/log/log-list";
    }
}
