package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constants.MessageConstant;
import com.itheima.constants.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.service.MemberService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * 登录操作
 */
@RestController
@RequestMapping("login")
public class LoginController {

    private static final Logger log = Logger.getLogger(LoginController.class);

    @Reference
    MemberService memberService;

    @Autowired
    private JedisPool jedisPool;

    //登录
    @RequestMapping("/check")
    public Result check(HttpServletResponse response, @RequestBody Map map){
        //1.验证登录用户的合法性
        String telephone = (String) map.get("telephone");//手机号
        String validateCode = (String) map.get("validateCode");//用户提交的验证码


        String codeInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);

        if(codeInRedis==null||!codeInRedis.equals(validateCode)){
            return Result.error(MessageConstant.VALIDATECODE_ERROR);
        }

        //2.查询该手机号是否 注册了会员。
        //如果没有注册会员 保存用户信息，注册会员
        Member member = memberService.findByTelephone(telephone);

        if(member == null){
            member = new Member();
            member.setPhoneNumber(telephone);
            member.setRegTime(new Date());

            memberService.add(member);
        }

        //3.将登录信息cookie 保存到浏览器，以后登录直接读取cookie
        Cookie cookie = new Cookie("login_member_telephone",telephone);
        cookie.setPath("/");
        cookie.setMaxAge(30*60*60*24);//cookie的存活时间

        //写到客户端 浏览器
        response.addCookie(cookie);

        return Result.success(MessageConstant.LOGIN_SUCCESS);
    }
}