package com.zmy.web.exceptions;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 统一异常处理
 */
@Component
public class CustomExceptionResolver implements HandlerExceptionResolver {
    /**
     *  1.跳转到美化了的错误页面
     *  2.携带错误信息
     */
    @Override
    public ModelAndView resolveException(HttpServletRequest request,
                                         HttpServletResponse response,
                                         Object o,
                                         Exception e) {
       e.printStackTrace();
       ModelAndView mav = new ModelAndView();
       mav.addObject("errorMsg","对不起，你错了");
       mav.addObject("e",e);
       mav.setViewName("error");

        return mav;
    }
}
