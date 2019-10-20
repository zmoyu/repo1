package com.zmy.service.cargo;

import com.github.pagehelper.PageInfo;
import com.zmy.domain.cargo.Export;
import com.zmy.domain.cargo.ExportExample;
import com.zmy.vo.ExportResult;


public interface ExportService {

    Export findById(String id);

    void save(Export export);

    void update(Export export);

    void delete(String id);

	PageInfo<Export> findByPage(ExportExample example, int pageNum, int pageSize);

    void updateExport(ExportResult exportResult);
}
