package com.itheima.service;

import com.itheima.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

/**
 * 预约设置服务接口
 */
public interface OrderSettingService {

    /**
     * 添加预约设置
     * @param orderSettings
     */
    void add(List<OrderSetting> orderSettings);

    /**
     * 查询预约数据
     */
    List<Map> getOrderSettingByMonth(String date);

    /**
     * 根据日期更新预约人数
     * @param orderSetting
     */
    void editNumberByDate(OrderSetting orderSetting);
}