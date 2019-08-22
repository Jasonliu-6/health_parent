package com.itheima.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HellerContoller {

    @RequestMapping("delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void delete(){
        System.out.println("delete ..");
    }

    @RequestMapping("update")
    @PreAuthorize("hasAuthority('update')")
    public void update(){
        System.out.println("update ..");
    }
}