package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.Setmeal;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
/**
 * 套餐数据库操作
 */
public interface SetmealDao {

    /**
     * 添加套餐
     * @param setmeal
     */
    void add(Setmeal setmeal);

    /**
     * 设置套餐与检查组关联
     * @param list
     */
    void setSetSetmealAndCheckgroup(List<Map<String, Integer>> list);

    /**
     * 套餐分页查询
     * @param queryString
     * @return
     */
    Page<Setmeal> selectByCondition(String queryString);

    /***
     * 查询套餐
     * @return
     */
    List<Setmeal> findAll();

    /**
     * 根据套餐id查询 查询套餐信息
     * @param id
     * @return
     */
    Setmeal findById(Integer id);


    List<Map<String,Object>> findSetmealCount();
}