package com.zmy.service.system.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zmy.dao.system.ModuleDao;
import com.zmy.domain.system.Module;
import com.zmy.service.system.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ModuleServiceImpl implements ModuleService {

    @Autowired
    private ModuleDao moduleDao;

    //根据id查询
    @Override
    public Module findById(String id) {
        return moduleDao.findById(id);
    }

    //分页查询
    @Override
    public PageInfo<Module> findByPage(int pageNum, int pageSize) {

        PageHelper.startPage(pageNum,pageSize);
        List<Module> moduleList = moduleDao.findAll();

        return new PageInfo<>(moduleList);
    }

    //根据id删除模块
    @Override
    public void delete(String id) {

        moduleDao.delete(id);
    }

    //添加模块
    @Override
    public void save(Module module) {

        //指定id字段属性
        module.setId(UUID.randomUUID().toString());
        moduleDao.save(module);
    }

    //修改模块
    @Override
    public void update(Module module) {

        moduleDao.update(module);
    }

    //查询所有模块
    @Override
    public List<Module> findAll() {
        return moduleDao.findAll();
    }

    //根据角色id查询角色所具有的权限
    @Override
    public List<Module> findRoleModulesByRoleId(String roleId) {
        return  moduleDao.findRoleModulesByRoleId(roleId);
    }
}
