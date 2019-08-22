package com.itheima.dao;

import com.itheima.pojo.CheckGroup;

import java.util.List;
import java.util.Map;

/**
 * 检查组数据库操作
 */
public interface CheckGroupDao {

    /**
     * 添加检查组基本信息
     *
     * @param checkgroup
     */
    void add(CheckGroup checkgroup);

    /**
     * 添加检查组与检查项关联信息
     *
     * @param map
     */
    void setCheckGroupAndCheckItem(Map<String, Integer> map);

    /**
     * 分页查询
     *
     * @param queryString
     * @return
     */
    List<CheckGroup> selectByCondition(String queryString);

    /**
     * 根据检查组id，查询检查组基本信息
     *
     * @param id 检查组id
     * @return
     */
    CheckGroup findById(Integer id);

    /**
     * 根据检查组id ,查询关联的检查项ids
     *
     * @param id
     * @return
     */
    List<Integer> findCheckitemIdsById(Integer id);

    /**
     * 批量保存 查组与检查项关联信息
     *
     * @param list
     */
    void setCheckGroupAndCheckItemBatch(List<Map<String, Integer>> list);

    /**
     * 查询所有检查组
     *
     * @return
     */
    List<CheckGroup> findAll();
}