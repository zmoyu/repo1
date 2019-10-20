package com.zmy.web.shiro;

import com.zmy.domain.system.Module;
import com.zmy.domain.system.User;
import com.zmy.service.system.UserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 自定义realm类
 * <p>
 * * shiro认证常见的异常：
 * *   用户名错误：UnknownAccountException
 * *   密码错误：  IncorrectCredentialsException
 * *   数据库中的密码没有加密：Caused by: java.lang.IllegalArgumentException: Odd number of characters.
 */
public class AuthRealm extends AuthorizingRealm {
    /**
     * 登录认证
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Autowired
    private UserService userService;

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //获取用户名
        String email = (String) token.getPrincipal();
        //根据邮箱查找用户(查询数据库)
        List<User> list = userService.findByEmail(email);

        if (list == null || list.size() == 0) {
            // 说明用户名不存在,报错：UnknownAccountException
            return null;
        }
        //3. 获取用户
        User user = list.get(0);

        //4. 返回
        //参数1：认证后的身份对象。通过subject.getPrincipal()获取的就是这里的参数1
        //参数2：数据库中正确的密码 (shiro自动校验，校验失败：IncorrectCredentialsException)
        //参数3：realm名称，可以随便写，确保唯一。getName()获取shiro默认的realm的名称
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, user.getPassword(), getName());
        return info;
    }

    /**
     * 授权访问
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        /**
         *查询用户当前所有权限并返回
         */
        //获取当前登录的用户对象
        User user = (User) principalCollection.getPrimaryPrincipal();

        //查询当前用户的所有权限
        List<Module> modules = userService.findUserModuleByUserId(user.getId());

        //参数是String类型set集合
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        //构造Authorization对象返回数据
        Set<String> permissions = new HashSet<>();

        for (Module module : modules) {
            permissions.add(module.getName());
        }

        //将所有可操作模块的名称存入到授权对象中
        info.addStringPermissions(permissions);
        return info;
    }
}
