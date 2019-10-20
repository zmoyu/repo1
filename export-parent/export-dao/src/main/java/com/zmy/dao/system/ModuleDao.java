package com.zmy.dao.system;

import com.zmy.domain.system.Module;

import java.util.List;

public interface ModuleDao {

    //根据id查询
    Module findById(String moduleId);

    //根据id删除
    void delete(String moduleId);

    //添加
    void save(Module module);

    //更新
    void update(Module module);

    //查询全部
    List<Module> findAll();

    //根据角色id查询角色所具有的权限
    List<Module> findRoleModulesByRoleId(String roleId);

    //根据belong值查找权限（菜单）
    List<Module> findByBelong(String belong);

    //根据用户Id查找用户权限
    List<Module> findUserModuleByUserId(String userId);
}