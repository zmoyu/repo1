package com.zmy.dao.system;


import com.zmy.domain.system.Module;
import com.zmy.domain.system.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserDao {

	//根据企业id查询全部
	List<User> findAll(String companyId);

	//根据id查询
    User findById(String userId);

	//根据id删除
	void delete(String userId);

	//保存
	void save(User user);

	//更新
	void update(User user);

	//根据用户查询用户角色中间表
    long findUserRoleByUserId(String userId);

    //根据用户id,和角色id保存用户和角色（建立关系）,即向中间表存入数据
    void saveUserRole(@Param("userId") String userId, @Param("roleId123") String roleId);

    //根据用户id删除中间表数据
	void deleteUserRoleByUserId(String userId);

	//根据用户输入的邮箱查找用户
	List<User> findByEmail(String email);

}