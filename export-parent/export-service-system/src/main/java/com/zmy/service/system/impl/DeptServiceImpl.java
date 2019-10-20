package com.zmy.service.system.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zmy.dao.system.DeptDao;
import com.zmy.domain.system.Dept;
import com.zmy.service.system.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DeptServiceImpl implements DeptService {

    @Autowired
    private DeptDao deptDao;

    /**
     * @param companyId 根据企业id
     * @param pageNum   当前页
     * @param pageSize  页大小
     * @return
     */
    @Override
    public PageInfo<Dept> findByPage(String companyId, int pageNum, int pageSize) {

        //开始分页
        PageHelper.startPage(pageNum, pageSize);
        //调用dao
        List<Dept> list = deptDao.findAllDept(companyId);

        return new PageInfo<>(list);
    }

    /**
     * 根据部门id查询部门
     *
     * @param id
     * @return
     */
    @Override
    public Dept findById(String id) {
        return deptDao.findById(id);
    }

    /**
     * 根据公司id查询所有部门
     *
     * @param companyId
     * @return
     */
    @Override
    public List<Dept> findAllDept(String companyId) {
        return deptDao.findAllDept(companyId);
    }

    /**
     * //保存
     *
     * @param dept
     */
    @Override
    public void save(Dept dept) {
        dept.setId(UUID.randomUUID().toString());
        deptDao.save(dept);
    }

    /**
     * //修改
     *
     * @param dept
     */
    @Override
    public void update(Dept dept) {
        deptDao.update(dept);
    }

    @Override
    public boolean delete(String id) {
        //删除部门，先根据删除的部门id查询看是否有子部门，如果有就不能删除。给与提示。
        //SELECT * FROM pe_dept WHERE parent_id='100'  -- 有子部门，不能删除
        //DELETE FROM pe_dept WHERE dept_id='100'

        //1. 先查询删除的部门下是否有子部门
        List<Dept> list = deptDao.findByParentId(id);

        if (list == null || list.size() == 0) {
            //3. 当前部门没有子部门，可以删除
            deptDao.delete(id);
            return true;
        }
        return false;
    }
}
