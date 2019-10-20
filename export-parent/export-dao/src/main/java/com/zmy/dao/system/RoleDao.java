package com.zmy.dao.system;

import com.zmy.domain.system.Role;

import java.util.List;


public interface RoleDao {

    //根据id查询
    Role findById(String id);

    //查询全部
    List<Role> findAll(String companyId);

	//根据id删除
    void delete(String id);

	//添加
    void save(Role role);

	//更新
    void update(Role role);

    //解除角色权限关系
    void deleteRoleModuleByRoleId(String roleId);

    //添加角色权限
    void saveRoleModule(String roleId, String module);

    //根据用户id查询当前用户已有的角色
    List<Role> findUserRole(String userId);
}