package com.itheima.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserService implements UserDetailsService {

    @Autowired
    BCryptPasswordEncoder encoder;

    //模拟数据库中的用户数据
    public  static  Map<String, com.itheima.pojo.User> map = new HashMap<>();
    static {
        com.itheima.pojo.User user1 = new com.itheima.pojo.User();
        user1.setUsername("admin");
        user1.setPassword("admin");

        com.itheima.pojo.User user2 = new com.itheima.pojo.User();
        user2.setUsername("xiaoming");
        user2.setPassword("1234");

        map.put(user1.getUsername(),user1);
        map.put(user2.getUsername(),user2);
    }
    /**
     * 根据用户名加载用户信息
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("username:" + username);
        com.itheima.pojo.User userInDb = map.get(username);//模拟根据用户名查询数据库

        if(userInDb == null){
            //根据用户名没有查询到用户
            return null;
        }

 /*       //模拟数据库中的密码，后期需要查询数据库
        String passwordInDb = "{noop}" + userInDb.getPassword();*/

        userInDb.setPassword(encoder.encode(userInDb.getPassword()));

        List<GrantedAuthority> list = new ArrayList<>();
        //授权，后期需要改为查询数据库动态获得用户拥有的权限和角色
        list.add(new SimpleGrantedAuthority("add"));
        list.add(new SimpleGrantedAuthority("delete"));
        list.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

        UserDetails user = new User(username,userInDb.getPassword(),list);
        return user;
    }

  /*  public static void main(String[] args) {
        String a ="aa";

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        *//*System.out.println(encoder.encode(a));
        System.out.println(encoder.encode(a));
        System.out.println(encoder.encode(a));*//*


        System.out.println(encoder.matches(a,"$2a$10$LtaESnlzcsMAerW11DSdT.fJz36OL73IyHOCjz6nLgSrpPCZm9PkG"));
    }*/
}