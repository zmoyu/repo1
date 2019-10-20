package com.zmy.service.system;

import com.github.pagehelper.PageInfo;
import com.zmy.domain.system.Module;

import java.util.List;

public interface ModuleService {

    //根据id查询
    Module findById(String id);

    //分页查询
    PageInfo<Module> findByPage(int pageNum,int pageSize);

    //根据id删除模块
    void delete(String id);

    //添加模块
    void save(Module module);

    //修改模块
    void update(Module module);

    //查询所有模块
    List<Module> findAll();

    //根据角色id查询角色所具有的权限
    List<Module> findRoleModulesByRoleId(String roleId);
}
