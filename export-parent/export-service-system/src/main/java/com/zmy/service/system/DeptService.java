package com.zmy.service.system;

import com.github.pagehelper.PageInfo;
import com.zmy.domain.system.Dept;

import java.util.List;

public interface DeptService {

    /**
     * 分页查询
     * @param companyId 根据企业id
     * @param pageNum  当前页
     * @param pageSize   页大小
     * @return
     */
    PageInfo<Dept> findByPage(String companyId,int pageNum,int pageSize);

    /**
     *根据id查询部门
     * @param id
     * @return
     */
    Dept findById(String id);

    //根据公司id查询所有部门
    List<Dept> findAllDept(String companyId);

    //保存
    void save(Dept dept);

    //修改
    void update(Dept dept);

    //删除
    boolean delete(String id);
}
