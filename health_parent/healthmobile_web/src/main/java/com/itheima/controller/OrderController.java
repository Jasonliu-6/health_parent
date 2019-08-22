package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.aliyuncs.exceptions.ClientException;
import com.itheima.constants.MessageConstant;
import com.itheima.constants.RedisMessageConstant;
import com.itheima.controller.vo.OrderVO;
import com.itheima.entity.Result;
import com.itheima.pojo.Order;
import com.itheima.service.OrderService;
import com.itheima.utils.SMSUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.RedisPipeline;

import java.util.Map;

/**
 * 提交预约
 */
@RestController
@RequestMapping("order")
public class OrderController {

    private static final Logger log = Logger.getLogger(OrderController.class);

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private OrderService orderService;

    /**
     * 提交预约
     * @param id
     * @param map
     * @return
     */
    @RequestMapping("submit")
    public Result submit(Integer id, @RequestBody Map map){
        //1.验证用户提交数据的合法性

        //拿到手机号
        String telephone = (String) map.get("telephone");
        String validateCode = (String) map.get("validateCode");//用户前台提交的验证码

        //2拿到redis中，保存的验证码。 和 用户提交的验证码 进行比对
        // redis中验证码不存在 直接告诉用户 数据不合法
        //redis中存在 如果不一致 验证码不正确 直接告诉用户 数据不合法

        /**
         * key=telephone+业务代码
         */

        //redis中的验证码
        String codeInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);

        //不存在的情况
        if(codeInRedis==null || !validateCode.equals(codeInRedis)){
            return Result.error(MessageConstant.VALIDATECODE_ERROR);
        }

        //3.调用预约服务 处理预约逻辑
        map.put("orderType", Order.ORDERTYPE_WEIXIN);

        Result result =orderService.submit(map);

        //4.判断预约成功了还是失败了
        if(result.isFlag()){
            //成功 发送短信通知用户
            try {
                SMSUtils.sendShortMessage(SMSUtils.ORDER_NOTICE,telephone,map.get("orderDate").toString());
            } catch (ClientException e) {
               log.error("Send order success notice error.",e);
            }
        }

        return result;
    }

    /**
     * 查询预约单详情
     * @param id
     * @return
     */
    @RequestMapping("findById")
    public Result findById(Integer id){
        try {
            Map map = orderService.findById(id);

            return Result.success(MessageConstant.QUERY_ORDER_SUCCESS,map);
        } catch (Exception e) {
            log.error("Find order info by id error.",e);
        }

        return Result.error(MessageConstant.QUERY_ORDER_FAIL);
    }
}