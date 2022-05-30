package backend.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import common.constant.MessageConstants;
import common.constant.RedisConstants;
import common.entity.Setmeal;
import common.protocol.PageResult;
import common.protocol.QueryPageBean;
import common.protocol.Result;
import common.util.QiniuUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import service.SetMealService;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/setmeal")
public class SetMealController {

  @Reference private SetMealService setMealService;

  @Resource(name = "jedisPool")
  private JedisPool jedisPool;

  @RequestMapping("/upload")
  Result upload(@RequestParam("imgFile") MultipartFile imgFile) {
    String fileType = getFileType(Objects.requireNonNull(imgFile.getOriginalFilename()));
    String fileName = UUID.randomUUID() + fileType;
    try {
      // 将图片文件上传至七牛云服务器保存
      QiniuUtils.upload2Qiniu(imgFile.getBytes(), fileName);
      // 将图片的文件名保存入redis
      setImgIntoRedis(fileName);
      return new Result(true, MessageConstants.PIC_UPLOAD_SUCCESS, fileName);
    } catch (IOException e) {
      e.printStackTrace();
      return new Result(false, MessageConstants.PIC_UPLOAD_FAIL);
    }
  }

  @RequestMapping("/add")
  Result add(@RequestBody Setmeal setmeal, @RequestParam("checkgroupIds") Integer[] checkgroupIds) {
    try {
      setMealService.add(setmeal, checkgroupIds);
      return new Result(true, MessageConstants.ADD_SETMEAL_SUCCESS);
    } catch (Exception e) {
      e.printStackTrace();
      return new Result(false, MessageConstants.ADD_SETMEAL_FAIL);
    }
  }

  @RequestMapping("/findPage")
  PageResult findPage(@RequestBody QueryPageBean requestParams) {
    return setMealService.findPage(requestParams);
  }

  @RequestMapping("/update")
  Result update(
      @RequestBody Setmeal setmeal, @RequestParam("checkgroupIds") Integer[] checkgroupIds) {
    try {
      setMealService.update(setmeal, checkgroupIds);
      return new Result(true, MessageConstants.UPDATE_SETMEAL_SUCCESS);
    } catch (Exception e) {
      e.printStackTrace();
      return new Result(false, MessageConstants.UPDATE_SETMEAL_FAIL);
    }
  }

  @RequestMapping("/findCheckGroupIdsBySetMealId")
  Result findCheckGroupIdsBySetMealId(@RequestParam("id") Integer setmealId) {
    try {
      List<Integer> checkgroupIds = setMealService.findCheckGroupIdsBySetMealId(setmealId);
      return new Result(true, MessageConstants.QUERY_SETMEAL_SUCCESS, checkgroupIds);
    } catch (Exception e) {
      e.printStackTrace();
      return new Result(false, MessageConstants.QUERY_SETMEAL_FAIL);
    }
  }

  @RequestMapping("/delete")
  Result delete(@RequestParam("id") Integer setmealId) {
    try {
      setMealService.deleteById(setmealId);
      return new Result(true, MessageConstants.DELETE_SETMEAL_SUCCESS);
    } catch (Exception e) {
      e.printStackTrace();
      return new Result(false, MessageConstants.DELETE_SETMEAL_FAIL);
    }
  }

  /**
   * 将图片的文件名保存入redis
   *
   * @param fileName 图片文件名
   */
  private void setImgIntoRedis(String fileName) {
    Jedis jedis = jedisPool.getResource();
    jedis.sadd(RedisConstants.SETMEAL_PIC_RESOURCES, fileName);
    jedis.close();
  }

  /**
   * 根据fileName，判断文件类型
   *
   * @param filename 文件名
   * @return 文件类型
   */
  private String getFileType(String filename) {
    for (int i = filename.length() - 1; i >= 0; i--) {
      if (filename.charAt(i) == '.') {
        return filename.substring(i);
      }
    }
    return "";
  }
}
