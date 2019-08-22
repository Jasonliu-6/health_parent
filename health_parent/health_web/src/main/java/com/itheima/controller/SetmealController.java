package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constants.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import com.itheima.utils.QiniuUtils;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("setmeal")
public class SetmealController {

    private static Logger log = Logger.getLogger(SetmealController.class);

    @Reference
    private SetmealService setmeaService;

    /*@Autowired
    private JedisPool jedisPool;*/

    /**
     * 上传图片
     * @param imgFile
     * @return
     */
    @RequestMapping("/upload")
    public Result upload(@RequestParam("imgFile") MultipartFile imgFile){
        try{
            //获取原始文件名 3bd90d2c-4e82-42a1-a401-882c88b06a1a2.jpg
            String originalFilename = imgFile.getOriginalFilename();

            //获取最后一个 . 位置索引
            int lastIndexOf = originalFilename.lastIndexOf(".");

            //截取文件名 获取文件后缀 .jpg .png
            String suffix = originalFilename.substring(lastIndexOf);


            //使用UUID随机产生文件名称，防止同名文件覆盖
            String fileName = UUID.randomUUID().toString() + suffix;

            //通过七牛SDK上传
            QiniuUtils.upload2Qiniu(imgFile.getBytes(),fileName);

            //保存到redis的集合中 redis set
            //jedisPool.getResource().sadd(RedisConstant.SETMEAL_IMG_TMP,fileName);

            //图片上传成功
            Result result = new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS);
            result.setData(fileName);
            return result;
        }catch (Exception e){
            log.error("Upload setmeal img error.",e);
            //图片上传失败
            return new Result(false,MessageConstant.PIC_UPLOAD_FAIL);
        }
    }

    /**
     * 添加套餐
     * @param setmeal
     * @param checkgroupIds
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody Setmeal setmeal, Integer [] checkgroupIds){
        try {
            setmeaService.add(setmeal,checkgroupIds);

            return Result.success(MessageConstant.ADD_SETMEAL_SUCCESS);
        } catch (Exception e) {
            log.error("Add setmeal error.",e);
        }

        return Result.error(MessageConstant.ADD_SETMEAL_FAIL);
    }

    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        return setmeaService.pageQuery(queryPageBean.getCurrentPage(),queryPageBean.getPageSize(),queryPageBean.getQueryString());
    }
}