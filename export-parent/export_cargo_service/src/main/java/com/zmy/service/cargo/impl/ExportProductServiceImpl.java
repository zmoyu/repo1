package com.zmy.service.cargo.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zmy.dao.cargo.ExportProductDao;
import com.zmy.domain.cargo.ExportProduct;
import com.zmy.domain.cargo.ExportProductExample;
import com.zmy.service.cargo.ExportProductService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service(timeout = 1000000)
public class ExportProductServiceImpl implements ExportProductService {

    @Autowired
    private ExportProductDao exportProductDao;

    @Override
    public ExportProduct findById(String id) {
        return exportProductDao.selectByPrimaryKey(id);
    }

    @Override
    public void save(ExportProduct exportProduct) {
        exportProductDao.insertSelective(exportProduct);
    }

    @Override
    public void update(ExportProduct exportProduct) {
        exportProductDao.updateByPrimaryKeySelective(exportProduct);
    }

    @Override
    public void delete(String id) {
        exportProductDao.deleteByPrimaryKey(id);
    }

    @Override
    public PageInfo<ExportProduct> findByPage(ExportProductExample exportProductExample, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<ExportProduct> list = exportProductDao.selectByExample(exportProductExample);

        return new PageInfo<>(list);
    }

    @Override
    public List<ExportProduct> findAll(ExportProductExample exportProductExample) {
        return exportProductDao.selectByExample(exportProductExample);
    }
}
