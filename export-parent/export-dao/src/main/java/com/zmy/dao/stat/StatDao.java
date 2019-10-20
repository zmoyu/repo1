package com.zmy.dao.stat;

import java.util.List;
import java.util.Map;

public interface StatDao {
    /**
     * 需求生产厂家销售统计
     */
    List<Map<String,Object>> factorySale();

    /**
     * 商品销售排行榜
     */
    List<Map<String,Object>> productSale(int top);

    /**
     * 需求:系统访问压力图
     */
    List<Map<String, Object>> online();
}
