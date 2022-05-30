package jobs;

import common.constant.RedisConstants;
import common.util.QiniuUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.util.Set;

/** 定时任务类，实现定时清理无效图片 */
public class ClearImgJob {

  @Resource(name = "jedisPool")
  private JedisPool jedisPool;

  public void clearImg() {
    Jedis jedis = jedisPool.getResource();
    Set<String> garbageSet =
        jedis.sdiff(RedisConstants.SETMEAL_PIC_RESOURCES, RedisConstants.SETMEAL_PIC_DB_RESOURCES);
    for (String imgName : garbageSet) {
      QiniuUtils.deleteFileFromQiniu(imgName);
      jedis.srem(RedisConstants.SETMEAL_PIC_RESOURCES, imgName);
    }
  }

}
