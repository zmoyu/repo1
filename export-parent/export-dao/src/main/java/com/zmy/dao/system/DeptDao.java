package com.zmy.dao.system;

import com.zmy.domain.system.Dept;

import java.util.List;

public interface DeptDao {

    /**
     * 通过企业id查询所有部门
     * @param companyId
     * @return
     */
    List<Dept> findAllDept(String companyId);

    /**
     * 根据deptId查询部门
     */
    Dept findById(String id);

    /**
     * //保存
     * @param dept
     */
    void save(Dept dept);

    /**
     *   //修改
     * @param dept
     */
    void update(Dept dept);

    /**
     * 删除
     * @param id
     * @return
     */
    void delete(String id);

    /**
     * 查询部门的子部门
     * @param parentId
     * @return
     */
    List<Dept> findByParentId(String parentId);
}
