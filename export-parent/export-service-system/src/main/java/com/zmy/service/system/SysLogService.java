package com.zmy.service.system;

import com.github.pagehelper.PageInfo;
import com.zmy.domain.system.SysLog;

import java.util.List;

public interface SysLogService {

    //分页查询
    PageInfo<SysLog> findByPage(String companyId,int pageNum,int pageSize);

    //保存日志
    void save(SysLog log);
}
