package com.zmy.web.controller.stat;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zmy.service.stat.StatService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/stat")
public class StatController {

    @Reference
    private StatService statService;
    /**
     * 进入统计分析页面
     * 请求地址:
     *     http://localhost:8080/stat/toCharts.do?chartsType=factory
     *     http://localhost:8080/stat/toCharts.do?chartsType=sell
     *     http://localhost:8080/stat/toCharts.do?chartsType=online
     * 响应页面：
     *     stat-factory.jsp
     *     stat-sell.jsp
     *     stat-online.jsp
     */
    @RequestMapping("/toCharts")
    public String toChars(String chartsType){

        return "stat/stat-"+chartsType;
    }

    /**
     * 需求：统计生产厂家销售金额（货物、附件）
     */
    @RequestMapping("/factorySale")
    @ResponseBody  //自动把方法返回结果转为json
    public List<Map<String, Object>> factoryCharts(){
        List<Map<String, Object>> result = statService.factorySale();
        return result;
    }

    /**
     * 需求：统计商品销售排行top
     */
    @RequestMapping("/productSale")
    @ResponseBody
    public List<Map<String,Object>> productSale(){
        List<Map<String, Object>> result = statService.productSale(5);
        return result;
    }

    /**
     * 需求：时段在线压力统计
     */
    @RequestMapping("/online")
    @ResponseBody
    public List<Map<String,Object>> online(){
        List<Map<String,Object>> result = statService.online();
        return result;
    }
}
