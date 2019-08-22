package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.PermissionDao;
import com.itheima.dao.RoleDao;
import com.itheima.dao.UserDao;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * 用户业务实现
 */
@Service(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl implements UserService {


    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private PermissionDao permissionDao;

    /**
     * 用户基本信息+角色+权限
     *
     * @param username
     * @return
     */
    @Override
    public User findByUsername(String username) {
        //1.用户基本信息
        User user = userDao.findByUsername(username);

        if (user == null) {
            return null;
        }

        //2.角色信息 角色信息与权限有关联关系
        Set<Role> roles = roleDao.findByUserId(user.getId());

        //3.查询权限信息
        if (roles != null&&roles.size()>0) {
            //将角色封装到用户信息
            user.setRoles(roles);

            for (Role role : roles) {
                Set<Permission> permissions = permissionDao.findByRoleId(role.getId());

                if (permissions != null&&permissions.size()>0) {
                    //将权限信息 封装到 角色信息
                    role.setPermissions(permissions);
                }
            }
        }

        return user;
    }
}