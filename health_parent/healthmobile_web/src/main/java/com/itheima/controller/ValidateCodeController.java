package com.itheima.controller;

import com.aliyuncs.exceptions.ClientException;
import com.itheima.constants.MessageConstant;
import com.itheima.constants.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.utils.SMSUtils;
import com.itheima.utils.ValidateCodeUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

/**
 * 验证码操作
 */
@RestController
@RequestMapping("validateCode")
public class ValidateCodeController {

    private static final Logger log = Logger.getLogger(ValidateCodeController.class);

    @Autowired
    private JedisPool jedisPool;

    /**
     * 获取预约验证码
     * @param telephone
     * @return
     */
    @RequestMapping("/send4Order")
    public Result send4Order(String telephone) {
        //发送验证码之前 首先判断一下 5 分钟内，是否已经发送。 如果已发送 告诉用户 5分钟后再试
        String sendSms = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);

        if(sendSms!=null){
            //该手机号 5 分钟内已发送过短信
            return Result.error("您的验证码已经发送到您的手机，请5分钟之后再试");
        }

        //为了防止别人恶意调用接口发送短信 需要对每一个手机号 每天 发送短信数量进行限制
        String send4orderCount = jedisPool.getResource().get("sms:send:" + telephone);

        //不存在这个变量 在redis中创建一个
        if(send4orderCount==null){
            jedisPool.getResource().setex("sms:send:" + telephone,24*60*60,"0");
            send4orderCount="0";
        }

        int send4orderCountInt = Integer.parseInt(send4orderCount);

        if(send4orderCountInt>3){
            return Result.error("对不起 您发送短信验证码太频繁了 请稍后再试");
        }

        //1.生成验证码 4位
        Integer code = ValidateCodeUtils.generateValidateCode(4);

        if (log.isDebugEnabled()) {
            log.debug("generate for order. telephone:" + telephone + "  code:" + code);
        }

        //2.发送验证码
        try {
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, code.toString());
        } catch (ClientException e) {
            log.error("Send sms for order error.", e);

            return Result.error(MessageConstant.SEND_VALIDATECODE_FAIL);
        }


        //每个手机号发送短信数量统计
        jedisPool.getResource().incr("sms:send:" + telephone);

        //3.把验证码存储到redis中， 和提交预约信息中的验证码进行比较
        //key 格式 telephone+业务代码
        //set(key,value) 存放数据到redis
        //setex  存放数据到redis 设置数据存活时间

        jedisPool.getResource().setex(telephone + RedisMessageConstant.SENDTYPE_ORDER, 5 * 60, code.toString());

        //4.返回结果
        return Result.success(MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }

    @RequestMapping("/send4Login")
    public Result send4login(String telephone){
        //1.生成验证码 6位
        Integer code = ValidateCodeUtils.generateValidateCode(6);

        if (log.isDebugEnabled()) {
            log.debug("generate for login. telephone:" + telephone + "  code:" + code);
        }

        //2.发送验证码
        try {
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, code.toString());
        } catch (ClientException e) {
            log.error("Send sms for order error.", e);

            return Result.error(MessageConstant.SEND_VALIDATECODE_FAIL);
        }


        //3.把验证码存储到redis中， 和提交预约信息中的验证码进行比较
        //key 格式 telephone+业务代码
        //set(key,value) 存放数据到redis
        //setex  存放数据到redis 设置数据存活时间
        jedisPool.getResource().setex(telephone + RedisMessageConstant.SENDTYPE_LOGIN, 5 * 60, code.toString());

        //4.返回结果
        return Result.success(MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }

}