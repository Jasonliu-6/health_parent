package com.itheima.dao;

import com.itheima.pojo.Order;

import java.util.List;
import java.util.Map;

/**
 * 预约单数据库操作
 */
public interface OrderDao {

    /**
     * 根据预约单信息 查询重复预约单信息
     * @param order
     * @return
     */
    List<Order> selectByCondition(Order order);

    /**
     * 新增预约单
     * @param order
     */
    void add(Order order);

    /**
     * 根据预约单id 查询预约单详细信息
     * @param id
     * @return
     */
    Map findById4Detail(Integer id);

    Integer findOrderCountByDate(String today);

    Integer findOrderCountAfterDate(String thisWeekMonday);

    Integer findVisitsCountByDate(String today);

    Integer findVisitsCountAfterDate(String thisWeekMonday);

    List<Map> findHotSetmeal();
}