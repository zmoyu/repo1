package com.zmy.service.system.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zmy.dao.system.SysLogDao;
import com.zmy.domain.system.SysLog;
import com.zmy.service.system.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SysLogServiceImpl implements SysLogService {

    @Autowired
    private SysLogDao sysLogDao;

    /**
     * 分页查询
     * @param companyId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public PageInfo<SysLog> findByPage(String companyId, int pageNum, int pageSize) {
        //调用startPage方法
        PageHelper.startPage(pageNum,pageSize);
        //查询全部列表
        List<SysLog> sysLogList = sysLogDao.findAll(companyId);
        //构造pageBean(封装日志数据)
        return new PageInfo<>(sysLogList);
    }

    @Override
    public void save(SysLog sysLog) {

        //设置主键值
        sysLog.setId(UUID.randomUUID().toString());
       //调用dao保存日志信息
        sysLogDao.save(sysLog);
    }
}
