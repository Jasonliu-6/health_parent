package com.itheima.dao;

import com.itheima.pojo.Permission;

import java.util.Set;

public interface PermissionDao {

    /**
     * 根据roleid 查询 权限信息
     * @param id
     * @return
     */
    Set<Permission> findByRoleId(Integer id);
}