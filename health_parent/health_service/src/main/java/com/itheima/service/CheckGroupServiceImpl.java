package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CheckGroupDao;
import com.itheima.entity.PageResult;
import com.itheima.pojo.CheckGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 检查组业务实现
 */
@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {

    @Autowired
    private CheckGroupDao checkGroupDao;

    /**
     * 保存检查组信息
     * @param checkgroup
     * @param checkitemIds
     */
    @Override
    public void add(CheckGroup checkgroup, Integer[] checkitemIds) {
        //保存检查组基本信息
        checkGroupDao.add(checkgroup);

        //保存检查组与检查项关联信息 存入中间表 checkitem_id and checkgroup_id
        setCheckGroupAndCheckItem(checkgroup.getId(),checkitemIds);
    }

    /**
     * 设置检查组与检查项关联信息
     * @param id 检查组id
     * @param checkitemIds 检查项id
     *  批量插入数据 思考一下
     */
    private void setCheckGroupAndCheckItem(Integer id, Integer[] checkitemIds) {
        if (checkitemIds != null&&checkitemIds.length>0) {

            List<Map<String,Integer>> list = new ArrayList<>();

            for (Integer checkitemId : checkitemIds) {
                Map<String,Integer> map = new HashMap<>();
                map.put("checkgroup_id",id);
                map.put("checkitem_id",checkitemId);

                //保存到t_checkgroup_checkitem关联中间表中
                //checkGroupDao.setCheckGroupAndCheckItem(map);

                list.add(map);
            }

            checkGroupDao.setCheckGroupAndCheckItemBatch(list);
        }
    }

    /**
     * 分页查询
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    @Override
    public PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage,pageSize); //TheadLocal

        Page page = (Page) checkGroupDao.selectByCondition(queryString);

        //1. mybaties 代理首先查询有多少条纪录 count(*)>0 执行分页查询；0<=count(0) 不执行分页 返回

        //2. select * from table where queryString="" limit 10,10

        //3.分页插件拿到 list，封装到Page
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @Override
    public CheckGroup findById(Integer id) {
        return checkGroupDao.findById(id);
    }

    /**
     * 根据检查组id ,查询关联的检查项ids
     * @param id
     * @return
     */
    @Override
    public List<Integer> findCheckitemIdsById(Integer id) {
        return checkGroupDao.findCheckitemIdsById(id);
    }

    /**
     * 查询所有检查组
     * @return
     */
    @Override
    public List<CheckGroup> findAll() {
        return checkGroupDao.findAll();
    }
}