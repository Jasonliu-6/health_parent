package com.itheima.dao;

import com.itheima.pojo.Role;

import java.util.Set;

public interface RoleDao {

    /**
     *
     * @param userId
     * @return
     */
    Set<Role> findByUserId(Integer id);
}