package com.itheima.service;

import com.itheima.pojo.User;

/**
 * 用户接口
 */
public interface UserService {

    /**
     * 根据用户名查询用户信息 用户基本信息+角色+权限
     * @param username
     * @return
     */
    User findByUsername(String username);
}