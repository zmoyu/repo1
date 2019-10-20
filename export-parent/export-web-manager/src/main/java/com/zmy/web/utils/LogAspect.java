package com.zmy.web.utils;

import com.zmy.domain.system.SysLog;
import com.zmy.domain.system.User;
import com.zmy.service.system.SysLogService;
import org.apache.http.HttpRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.aop.PointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 日期切面类,希望在执行controller方法之前自动记录日志
 */
@Component
@Aspect
public class LogAspect {

    //注入日志service
    @Autowired
    private SysLogService sysLogService;
    //注入request获取session中的登录用户的数据
    @Autowired
    private HttpServletRequest request;

    /**
     * 录日志: 在执行controller所有方法时候记录日志
     * * @param ProceedingJoinPoint(执行的连接点)
     * pjp 获取当前执行的方法参数、方法名称、方法所在的类
     */
    @Around(value = "execution(* com.zmy.web.controller.*.*Controller.*(..))")
    public Object insertLog(ProceedingJoinPoint pjp) {
        //创建log对象
        SysLog log = new SysLog();

        //获取当前执行的方法名称
        String methodName = pjp.getSignature().getName();
        //获取目标对象类全名
        String className = pjp.getTarget().getClass().getName();

        log.setTime(new Date());
        log.setMethod(methodName);
        log.setAction(className);//设置目标对象类全名

        //获取登陆用户
        User user = (User) request.getSession().getAttribute("loginUser");
        log.setCompanyId(user.getCompanyId());
        log.setCompanyName(user.getCompanyName());
        log.setUserName(user.getUserName());

        //获取登录用户的ip getRemoterUser:获取远程用户ip
        log.setIp(request.getRemoteUser());

        try {
            //调用service记录日志
            sysLogService.save(log);
            //放行,执行控制器方法
            Object retV = pjp.proceed();
            return retV;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        }
    }
}
