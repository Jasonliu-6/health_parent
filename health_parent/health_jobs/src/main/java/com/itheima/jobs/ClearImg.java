package com.itheima.jobs;

import com.itheima.constants.RedisConstant;
import com.itheima.utils.QiniuUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Iterator;
import java.util.Set;

/**
 * 清理垃圾图片
 */
public class ClearImg {

    private static final Logger log = Logger.getLogger(ClearImg.class);

    @Autowired
    JedisPool jedisPool;

    /**
     *
     * 由于套餐缩略图是先传到 七牛云 上去的，表单数据并未提交到数据库。
     * 提交表单的时候保存的是缩略图的文件名到数据库
     *
     * 用户操作情景：
     *  情景一
     *
     * 用户上传了 缩略图到七牛云，关闭了窗口，那么，这张图片只存在七牛云上，并不存在与数据库中，业务系统就无法引用到这张图片，
     * 那么，这张图片就成了 垃圾图片 我们需要清理
     *
     * 情景二
     *
     * 用户上传了 缩略图到七牛云,并且提交了表单数据到后台，那么，七牛云上和数据库中都保存了该图片地址信息，我们就可以在业务系统
     * 中使用
     *
     *
     * 通过将图片地址记录在 redis的两个 set 集合中，并且找到两个set的差值，就是我们需要清理的图片
     *
     * 示例：
     * 集合redis 七牛云 [1,2,3,4]
     *
     * 集合redis 数据库 [1,2]
     *
     * 差值：除去两个集合中都存在的元素，剩下的就是差值。
     * 七牛云和数据库两个集合中，都存在的是[1,2], 那么差值就是[3,4]
     *
     *
     * 分析： 3,4两个元素只存在 七牛云上，不存在数据库中，那么，业务系统就引用不到 我们需要清理掉。
     *
     */
    public void clear(){
        //找出2个set中的 差值
        //sdiff
        Set<String> set = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_IMG_TMP,RedisConstant.SETMEAL_IMG_DB);

        Iterator<String> iterator = set.iterator();

        while(iterator.hasNext()){

            String pic = iterator.next();

            if(log.isDebugEnabled()){
                log.debug("=====>file name:"+pic);
            }

            //删除图片服务器中的图片文件
            QiniuUtils.deleteFileFromQiniu(pic);

            //删除redis中的数据
            jedisPool.getResource().srem(RedisConstant.SETMEAL_IMG_TMP,pic);
        }
    }
}