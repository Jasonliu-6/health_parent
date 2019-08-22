package com.itheima.dao;

import com.itheima.pojo.OrderSetting;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;
/**
 * 预约设置数据库操作
 */
public interface OrderSettingDao {

    /**
     * 根据日期查找当天 是否设置了预约数量
     * @param orderDate
     * @return
     */
    long findCountByOrderDate(Date orderDate);

    /**
     * 根据日期 更新预约数量
     * @param orderSetting
     */
    void editNumberByOrderDate(OrderSetting orderSetting);

    /**
     * 添加预约数量
     * @param orderSetting
     */
    void add(OrderSetting orderSetting);

    /**
     * 获取日历数据
     * @param beginDate
     * @param endDate
     * @return
     */
    List<OrderSetting> getOrderSettingByMonth(@Param("beginDate") String beginDate,@Param("endDate")  String endDate);

    /**
     * 根据预约日期查询 预约设置信息
     * @param date
     * @return
     */
    OrderSetting findByOrderDate(Date date);

    /**
     * 根据预约日期 更新 已预约人数
     * @param orderSetting
     */
    void editReservationsByOrderDate(OrderSetting orderSetting);
}