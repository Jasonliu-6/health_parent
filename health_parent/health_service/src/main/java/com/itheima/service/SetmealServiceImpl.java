package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.SetmealDao;
import com.itheima.entity.PageResult;
import com.itheima.pojo.Setmeal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 套餐业务实现
 */
@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealDao setmealDao;

   /* @Autowired
    private JedisPool jedisPool;*/

    @Override
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        //1.保存套餐基本信息 拿到套餐id
        setmealDao.add(setmeal);

        //2.设置检查组和套餐关联信息
        setSetSetmealAndCheckgroup(setmeal.getId(), checkgroupIds);

        //save2redis
        //save2redis(setmeal.getImg());
    }

    private void save2redis(String img) {
        //jedisPool.getResource().sadd(RedisConstant.SETMEAL_IMG_DB,img);
    }

    /**
     * 设置检查组和套餐关联信息
     *
     * @param id
     * @param checkgroupIds
     */
    private void setSetSetmealAndCheckgroup(Integer id, Integer[] checkgroupIds) {
        if (checkgroupIds != null && checkgroupIds.length > 0) {
            List<Map<String, Integer>> list = new ArrayList<>();

            for (Integer checkgroupId : checkgroupIds) {
                Map<String, Integer> map = new HashMap<>();
                map.put("setmeal_id", id);//套餐id
                map.put("checkgroup_id", checkgroupId);//检查组id

                list.add(map);
            }

            setmealDao.setSetSetmealAndCheckgroup(list);
        }
    }

    /**
     * 套餐分页查询
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    @Override
    public PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage, pageSize);

        Page page = setmealDao.selectByCondition(queryString);

        PageResult result = new PageResult(page.getTotal(),page.getResult());

        return result;
    }

    /**
     * 查询所有套餐
     * @return
     */
    @Override
    public List<Setmeal> findAll() {
        return setmealDao.findAll();
    }

    /**
     * 根据套餐id查询 查询套餐信息
     * @param id
     * @return
     */
    @Override
    public Setmeal findById(Integer id) {
        return setmealDao.findById(id);
    }

    /**
     *  套餐统计
     * @return
     */
    @Override
    public List<Map<String, Object>> findSetmealCount() {
    /*
    数据结构
        {value:435, name:'入职套餐'},
        {value:310, name:'邮件营销'},
        {value:234, name:'联盟广告'},
        {value:135, name:'视频广告'},
        {value:1548, name:'搜索引擎'}*/
        return setmealDao.findSetmealCount();
    }
}