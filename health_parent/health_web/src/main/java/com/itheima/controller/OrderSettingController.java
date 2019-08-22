package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constants.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderSettingService;
import com.itheima.utils.POIUtils;
import org.apache.log4j.Logger;
import org.aspectj.weaver.ast.Or;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 预约设置管理
 */
@RestController
@RequestMapping("ordersetting")
public class OrderSettingController {

    private static final Logger log = Logger.getLogger(OrderSettingController.class);

    @Reference
    private OrderSettingService orderSettingService;


    /**
     * 上传excel
     * @param file
     * @return
     */
    @RequestMapping("/upload")
    public Result upload(@RequestParam("excelFile") MultipartFile file){
        //拿出excel中的预约数据

        try {
            //list
            //row1 [0] [1]
            //row2 [0] [1]
            //row3 [0] [1]

            List<String[]> data = POIUtils.readExcel(file);

            if (data != null&&data.size()>0) {
                //所有预约数据
                List<OrderSetting> orderSettings = new ArrayList<>();

                //封装预约数据
                for (String[] row : data) {
                    //row[0] 拿到表格中的日期单元格中的值
                    //row[1]
                    OrderSetting orderSetting = new OrderSetting(new Date(row[0]),Integer.parseInt(row[1]));
                    orderSettings.add(orderSetting);
                }

                orderSettingService.add(orderSettings);
            }

            return Result.success(MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
        } catch (IOException e) {
            log.error("Upload excel case error.",e);
        }catch (Exception e){
            log.error("Upload orderSetting error.",e);
        }

        return Result.error(MessageConstant.IMPORT_ORDERSETTING_FAIL);
    }

    //获取日历数据

    /**
     *
     * @param date
     * @return
     */
    @RequestMapping("/getOrderSettingByMonth")
    public Result getOrderSettingByMonth(String date){//date 格式 2019-8
        try {
            List<Map> ordersetings = orderSettingService.getOrderSettingByMonth(date);

            return Result.success(MessageConstant.GET_ORDERSETTING_SUCCESS, ordersetings);
        } catch (Exception e) {
           log.error("Get OrderSetting By Month error.",e);
        }

        return Result.error(MessageConstant.GET_ORDERSETTING_FAIL);
    }

    /**
     * 根据日期更新预约人数
     * @param orderSetting
     * @return
     */
    @RequestMapping("/editNumberByDate")
    public Result editNumberByDate(@RequestBody  OrderSetting orderSetting){
        try {
            orderSettingService.editNumberByDate(orderSetting);

            return Result.success(MessageConstant.ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            log.error("Edit order setting error.",e);
        }

        return Result.error(MessageConstant.ORDERSETTING_FAIL);
    }
}