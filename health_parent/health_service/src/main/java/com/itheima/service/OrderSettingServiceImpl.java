package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.OrderSettingDao;
import com.itheima.pojo.OrderSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 预约设置服务实现
 */
@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    private OrderSettingDao orderSettingDao;

    /**
     * 添加预约设置
     * @param orderSettings
     */
    @Override
    public void add(List<OrderSetting> orderSettings) {
        //判断某一日期是否设置了 预约设置
        //如果设置了 更新已经设置预约设置
        //如果没有设置 新增一条预约设置

        if (orderSettings != null&&orderSettings.size()>0) {
            //拿到每一行数据
            for (OrderSetting orderSetting : orderSettings) {
                //1判断是否设置 count>0已设置， count=0 未设置
                long count = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());

                if(count>0){
                    //2更新已经设置的数据
                    orderSettingDao.editNumberByOrderDate(orderSetting);
                }else{
                    //3未设置 添加一条记录
                    orderSettingDao.add(orderSetting);
                }
            }
        }
    }

    /**
     * 获取日历数据
     * @param date
     * @return
     */
    @Override
    public List<Map> getOrderSettingByMonth(String date) {
        //date 2019-08
        String beginDate = date+"-1";
        String endDate = date+"-31";

        //预约数据
        List<OrderSetting> orderSettings = orderSettingDao.getOrderSettingByMonth(beginDate,endDate);

        List<Map> result = new ArrayList<>();//返回结果

        //封装 前端 需要的数据格式  { date: 1, number: 120, reservations: 1 },
        if (orderSettings != null&&orderSettings.size()>0) {

            for (OrderSetting orderSetting : orderSettings) {
                Map map = new HashMap();
                map.put("date",orderSetting.getOrderDate().getDate());
                map.put("number",orderSetting.getNumber());
                map.put("reservations",orderSetting.getReservations());

                result.add(map);
            }
        }

        return result;
    }

    /**
     * 根据日期更新预约人数
     * @param orderSetting
     */
    @Override
    public void editNumberByDate(OrderSetting orderSetting) {
        //1判断是否已经设置预约人数 count>0代表已设置， count=0未设置

        long count = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());

        if(count>0){
            //更新
            orderSettingDao.editNumberByOrderDate(orderSetting);
        }else {
            //新增
            orderSettingDao.add(orderSetting);
        }
    }
}