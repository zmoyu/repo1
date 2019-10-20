package com.zmy.service.stat;

import java.util.List;
import java.util.Map;

public interface StatService {
    /**
     * 生产销售统计
     */
    List<Map<String,Object>> factorySale();

    /**
     * 商品销售top
     */
    List<Map<String,Object>> productSale(int top);

    /**
     * 需求:系统访问压力图
     */
    List<Map<String, Object>> online();
}
