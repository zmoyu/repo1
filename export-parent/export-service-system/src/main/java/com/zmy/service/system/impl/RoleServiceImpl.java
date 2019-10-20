package com.zmy.service.system.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zmy.dao.system.RoleDao;
import com.zmy.domain.system.Role;
import com.zmy.service.system.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;
    @Override
    public PageInfo<Role> findByPage(String companyId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);

        //调用dao查询全部
        List<Role> list = roleDao.findAll(companyId);

        return new PageInfo<>(list);
    }

    @Override
    public Role findById(String id) {
        Role role = roleDao.findById(id);
        return role;
    }

    @Override
    public void delete(String id) {

        roleDao.delete(id);
    }

    @Override
    public void save(Role role) {
        //为主键设置随机数
        role.setId(UUID.randomUUID().toString());
        roleDao.save(role);
    }

    @Override
    public void update(Role role) {

        roleDao.update(role);
    }

    @Override
    public void updateRoleModule(String roleId, String moduleIds) {
        //-- 1）解除角色权限关系
        //DELETE FROM pe_role_module WHERE role_id=''
        //-- 2) 角色添加权限
        //INSERT INTO pe_role_module(role_id,module_id) VALUES('','')

        //解除角色权限关系
        roleDao.deleteRoleModuleByRoleId(roleId);

        //添加角色权限
        if(moduleIds!=null && !"".equals(moduleIds)){
            //分割字符串
            String[] modules = moduleIds.split(",");
            //遍历获取每个权限对象
            for (String module : modules) {
                roleDao.saveRoleModule(roleId,module);
            }
        }
    }

    //根据公司id查询出所有角色
    @Override
    public List<Role> findAll(String CompanyId) {
        return roleDao.findAll(CompanyId);
    }

    //根据用户id查询用户当前具有的所有角色
    @Override
    public List<Role> findUserRole(String userId) {
        return roleDao.findUserRole(userId);
    }

}
