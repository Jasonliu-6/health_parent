package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckItem;

import java.util.List;

/**
 * 检查项数据库操作
 */
public interface CheckItemDao {

    /**
     * 添加检查项
     * @param checkItem
     */
    void add(CheckItem checkItem);

    /**
     * 分页查询
     * @param queryString
     * @return
     */
    Page<CheckItem> selectByCondition(String queryString);

    /**
     * 根据id查询检查项
     * @param id
     * @return
     */
    CheckItem queryById(Integer id);

    /**
     * 编辑检查项
     * @param checkItem
     */
    void edit(CheckItem checkItem);

    /**
     * 根据检查项id，查询与检查组关联的数量
     * @param id
     * @return
     */
    long findCountByCheckitemId(Integer id);

    /**
     * 根据检查项id，删除检查项信息
     * @param id
     */
    void deleteById(Integer id);

    /**
     * 查询所有检查项
     * @return
     */
    List<CheckItem> findAll();
}