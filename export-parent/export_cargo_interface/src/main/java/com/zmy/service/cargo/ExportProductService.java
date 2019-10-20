package com.zmy.service.cargo;


import com.github.pagehelper.PageInfo;
import com.zmy.domain.cargo.ExportProduct;
import com.zmy.domain.cargo.ExportProductExample;

import java.util.List;


public interface ExportProductService {

	ExportProduct findById(String id);

	void save(ExportProduct exportProduct);

	void update(ExportProduct exportProduct);

	void delete(String id);

	PageInfo<ExportProduct> findByPage(ExportProductExample exportProductExample, int pageNum, int pageSize);

	//查询所有商品信息
    List<ExportProduct> findAll(ExportProductExample exportProductExample);
}
