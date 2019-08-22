package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constants.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckGroupService;
import com.itheima.service.CheckItemService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 检查组管理
 */
@RestController //返回数据 相当于在方法上面加@responseBody注解
@RequestMapping("/checkgroup")
public class CheckGroupController {

    private static final Logger log = Logger.getLogger(CheckGroupController.class);

    @Reference
    private CheckGroupService checkGroupService;

    /**
     * 添加检查组
     *
     * @param checkItem
     * @return
     * @RequestBody将json字符串 转换为 java 对象
     */
    @RequestMapping("/add")
    public Result add(@RequestBody CheckGroup checkgroup, Integer[] checkitemIds) {
        try {
            checkGroupService.add(checkgroup, checkitemIds);
            return Result.success(MessageConstant.ADD_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            log.error("Add checkgoup error.", e);
        }

        return Result.error(MessageConstant.ADD_CHECKGROUP_FAIL);
    }

    /**
     * 分页查询
     *
     * @param queryPageBean
     * @return
     */
    @PostMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult pageResult = checkGroupService.pageQuery(queryPageBean.getCurrentPage(), queryPageBean.getPageSize(), queryPageBean.getQueryString());
        return pageResult;
    }

    /**
     * 根据检查组id，查询检查组基本信息
     *
     * @param id
     * @return
     */
    @GetMapping("/findById")
    public Result findById(Integer id){
        try {
            CheckGroup checkGroup = checkGroupService.findById(id);
            return Result.success(MessageConstant.QUERY_CHECKGROUP_SUCCESS, checkGroup);
        } catch (Exception e) {
            log.error("Find checkgroup by id error.", e);
        }

        return Result.error(MessageConstant.QUERY_CHECKGROUP_FAIL);
    }

    /**
     * 根据检查组id，查询关联检查项信息
     * @param id
     * @return
     */
    @RequestMapping("/findCheckitemIdsById")
    public Result findCheckitemIdsById(Integer id){
        try {
            List<Integer> checkitemIds = checkGroupService.findCheckitemIdsById(id);
            return Result.success(MessageConstant.QUERY_CHECKITEM_SUCCESS, checkitemIds);
        } catch (Exception e) {
            log.error("Find checkitem ids by checkgroupId error.", e);
        }

        return Result.error(MessageConstant.QUERY_CHECKITEM_FAIL);
    }

    /**
     * 查询所有检查组
     * @return
     */
    @RequestMapping("findAll")
    public Result findAll(){
        try {
            List<CheckGroup> checkGroups = checkGroupService.findAll();
            return Result.success(MessageConstant.QUERY_CHECKGROUP_SUCCESS, checkGroups);
        } catch (Exception e) {
            log.error("Find all checkgroups error.",e);
        }

        return Result.error(MessageConstant.QUERY_CHECKGROUP_FAIL);
    }

}