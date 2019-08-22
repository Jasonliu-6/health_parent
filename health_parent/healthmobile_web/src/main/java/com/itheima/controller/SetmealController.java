package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constants.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 套餐操作
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    //logback slf4j
    private static final Logger log = Logger.getLogger(SetmealController.class);

    @Reference
    private SetmealService setmealService;

    /**
     * 查询所有套餐
     * @return
     */
    @RequestMapping("/getSetmeal")
    public Result getSetmeal(){
        try {
            List<Setmeal> setmeals = setmealService.findAll();

            return Result.success(MessageConstant.GET_SETMEAL_LIST_SUCCESS, setmeals);
        } catch (Exception e) {
            log.error("Find all setmeal error.",e);
        }

        return Result.error(MessageConstant.GET_SETMEAL_LIST_FAIL);
    }

    /**
     * 根据套餐id查询 查询套餐信息
     *
     * 套餐信息包括 套餐基本信息+检查组信息+检查项信息
     *
     * @param id
     * @return
     */
    @RequestMapping("findById")
    public Result findById(Integer id){
        try {
            Setmeal setmeal = setmealService.findById(id);

            return Result.success(MessageConstant.QUERY_SETMEAL_SUCCESS, setmeal);
        } catch (Exception e) {
            log.error("Find setmeal by id error.",e);
        }

        return Result.error(MessageConstant.QUERY_SETMEAL_FAIL);
    }
}