package com.zmy.web.shiro;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.crypto.hash.Md5Hash;

/**
 * 自定义凭证匹配器，进行加盐加密
 */
public class CustomCredentialsMatcher extends SimpleCredentialsMatcher {
    /**
     * 对用户输入的密码进行加密。加盐
     *
     * @param token 封装用户输入的信息
     * @param info  获取数据库中正确的密码
     * @return
     */
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {

        //获取用户输入的用户名
        //String email = (String) token.getPrincipal();
        String email = ((UsernamePasswordToken) token).getUsername();
        //获取用户输入的密码
        String password = new String((char[]) token.getCredentials());
        //把用户名作为盐，进行加密
        String md5Password = new Md5Hash(password, email).toString();
        //获取数据库中密码
        String dbPassword = (String) info.getCredentials();

        System.out.println(md5Password.equals(dbPassword));
        //比对
        return md5Password.equals(dbPassword);
    }
}
