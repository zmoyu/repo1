package com.zmy.dao.cargo;

import com.zmy.domain.cargo.ExportProduct;
import com.zmy.domain.cargo.ExportProductExample;

import java.util.List;

public interface ExportProductDao {
	/**
	 * 根据id删除
	 */
    void deleteByPrimaryKey(String id);

	/**
	 * 保存
	 */
	void insertSelective(ExportProduct record);

	/**
	 * 条件查询
	 */
    List<ExportProduct> selectByExample(ExportProductExample example);

	/**
	 * 根据id查询
	 */
    ExportProduct selectByPrimaryKey(String id);

	/**
	 * 更新
	 */
	void updateByPrimaryKeySelective(ExportProduct record);
}