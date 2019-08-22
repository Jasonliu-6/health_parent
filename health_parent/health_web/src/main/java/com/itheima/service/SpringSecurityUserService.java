package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.pojo.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.HashSet;
import java.util.Set;

/**
 * 从数据库 获取用户信息 +角色+权限信息
 */
public class SpringSecurityUserService implements UserDetailsService{

    @Reference
    private UserService userService;

    /**
     * 通过用户名获取  用户信息 +角色+权限信息
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //1.查询用户信息
        User user = userService.findByUsername(username);

        //未查询到用户 直接返回
        if (user == null) {
            return null;
        }

        //获取用户角色信息
        Set<Role> roles = user.getRoles();

        //权限信息
        Set<GrantedAuthority> set = new HashSet<>();

        if (roles != null&&roles.size()>0) {
            for (Role role : roles) {

                //从用户权限信息中 获取权限信息
                Set<Permission> permissions = role.getPermissions();

                if (permissions != null&&permissions.size()>0) {
                    //遍历权限 封装数据
                    for (Permission permission : permissions) {
                        set.add(new SimpleGrantedAuthority(permission.getKeyword()));
                    }
                }
            }
        }

        //框架需要的数据
        // 用户名 密码 权限信息
        org.springframework.security.core.userdetails.User userDetails =  new
                org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),set);

        return userDetails;
    }
}