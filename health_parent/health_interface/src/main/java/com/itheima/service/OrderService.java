package com.itheima.service;

import com.itheima.entity.Result;

import java.util.Map; /**
 * 预约服务接口
 */
public interface OrderService {
    /**
     * 提交预约信息
     * @param map
     * @return
     */
    Result submit(Map map);

    Map findById(Integer id);
}