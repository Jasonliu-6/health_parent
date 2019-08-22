package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.constants.MessageConstant;
import com.itheima.dao.MemberDao;
import com.itheima.dao.OrderDao;
import com.itheima.dao.OrderSettingDao;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.pojo.Order;
import com.itheima.pojo.OrderSetting;
import com.itheima.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 预约服务实现
 */
@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderSettingDao orderSettingDao;

    @Autowired
    MemberDao memberDao;

    @Autowired
    OrderDao orderDao;

    /**
     * 提交预约单信息
     * @param map
     * @return
     */
    @Override
    public Result submit(Map map) {
        //1. 判断是否已经设置了 预约设置
        //预约日期
        String orderDate = (String) map.get("orderDate");

        Date date = null;

        try {
            date = DateUtils.parseString2Date(orderDate);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //查询预约设置
        OrderSetting orderSetting = orderSettingDao.findByOrderDate(date);

        //没有预约设置 告诉用今天不能预约
        if(orderSetting == null){
            return Result.error(MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }

        //有预约设置 判断今天是否约满了
        //number<=reservations 表示预约满了

        int number = orderSetting.getNumber();//可预约人数
        int reservations = orderSetting.getReservations();//已预约人数

        if(number<=reservations){
            //预约满了 告诉用户 预约满了不能约了
            return Result.error(MessageConstant.ORDER_FULL);
        }

        //2.通过手机号 判断否已经是会员
        String telephone = (String) map.get("telephone");

        Member member = memberDao.findByTelephone(telephone);

        //套餐id
        Integer setmealId = Integer.parseInt(map.get("setmealId").toString());

        //已经注册会员
        if(member!=null){

            //3.判断当天是否已经预约 重复预约
            Order order = new Order(member.getId(),date,null,null,setmealId);

            List<Order> orders = orderDao.selectByCondition(order);
            //重复预约了
            if(orders!=null&&orders.size()>0){
                return Result.error(MessageConstant.HAS_ORDERED);
            }
        }

        //不是会员 在系统里面注册一下
        if(member==null){
            //会员信息
            member = new Member();
            member.setName((String) map.get("name"));
            member.setPhoneNumber(telephone);
            member.setIdCard((String) map.get("idCard"));
            member.setSex((String) map.get("sex"));
            member.setRegTime(new Date());

            memberDao.add(member);
        }

        orderSetting.setReservations(reservations+1);//可以预约 占一个预约名额

        //4.占一个预约名额 更新已预约人数
        orderSettingDao.editReservationsByOrderDate(orderSetting);

        //5.生成预约单信息 把预约信息保存到数据库
        Order order = new Order();
        order.setMemberId(member.getId());
        order.setOrderDate(date);
        order.setSetmealId(setmealId);
        order.setOrderType(map.get("orderType").toString());
        order.setOrderStatus(Order.ORDERSTATUS_NO);

        orderDao.add(order);

        return Result.success(MessageConstant.ORDER_SUCCESS,order);
    }

    /**
     * 根据预约单id 查询预约单信息
     * @param id
     * @return
     */
    @Override
    public Map findById(Integer id) {
        return orderDao.findById4Detail(id);
    }
}