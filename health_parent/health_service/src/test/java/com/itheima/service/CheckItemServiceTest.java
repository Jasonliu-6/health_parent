package com.itheima.service;

import com.itheima.pojo.CheckItem;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)//junit与spring集成
@ContextConfiguration(locations={"classpath*:applicationContext*.xml"})//创建spring容器
public class CheckItemServiceTest {

    @Autowired
    CheckItemService checkItemService;


    @Test
    public void addTest(){
        //mock数据
        CheckItem checkItem=new CheckItem();
        checkItem.setName("aaa");
        checkItem.setCode("aaa");

        checkItemService.add(checkItem);
    }

    @Test
    public void findAllTest(){
        List<CheckItem> all = checkItemService.findAll();
        Assert.assertTrue(all.size()>0);
    }
}