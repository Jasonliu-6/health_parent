package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.pojo.CheckGroup;

import java.util.List;

/**
 * 检查组服务接口
 */
public interface CheckGroupService {

    /**
     * 添加检查组信息
     * @param checkgroup
     * @param checkitemIds
     */
    void add(CheckGroup checkgroup, Integer[] checkitemIds);

    /**
     * 分页查询
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString);

    /**
     * 根据检查组id 查询检查组基本信息
     * @param id
     * @return
     */
    CheckGroup findById(Integer id);

    /**
     * 根据检查组id查询 关联的检查项信息
     * @param id
     * @return
     */
    List<Integer> findCheckitemIdsById(Integer id);

    List<CheckGroup> findAll();
}