package mobile.controller;

import common.constant.MessageConstants;
import common.constant.RedisConstants;
import common.protocol.Result;
import common.util.MailUtils;
import common.util.ValidateCodeUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;

@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {

  @Resource(name = "jedisPool")
  private JedisPool jedisPool;

  /**
   * 向目标邮箱发送验证码
   * 场景：提交预约时
   * @param telephone
   * @return
   */
  @RequestMapping("/sendCode4Order")
  Result sendCode4Order(@RequestParam("telephone") String telephone) {
    int validateCode = ValidateCodeUtils.generateValidateCode(4);
    //      SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, String.valueOf(validateCode));
    if (MailUtils.sendMail(telephone, String.valueOf(validateCode), "验证码")) {
      // 将验证码保存到redis（5分钟内有效），以备校验
      setIntoRedis4Order(telephone, validateCode);
      return new Result(true, MessageConstants.SEND_VALIDATECODE_SUCCESS);
    }
    return new Result(false, MessageConstants.SEND_VALIDATECODE_FAIL);
  }

  /**
   * 向目标邮箱发送验证码
   * 场景：使用邮箱号进行快速登录时
   * @param telephone
   * @return
   */
  @RequestMapping("/sendCode4Login")
  Result sendCode4Login(@RequestParam("telephone") String telephone) {
    int validateCode = ValidateCodeUtils.generateValidateCode(6);
    if (MailUtils.sendMail(telephone, String.valueOf(validateCode), "验证码")) {
      setIntoRedis4Login(telephone, validateCode);
      return new Result(true, MessageConstants.SEND_VALIDATECODE_SUCCESS);
    }
    return new Result(false, MessageConstants.SEND_VALIDATECODE_FAIL);
  }

  /**
   * 将随机生成的验证码保存到redis中，以备校验
   * 场景：提交预约信息时
   * @param telephone
   * @param validateCode
   */
  private void setIntoRedis4Order(String telephone, int validateCode) {
    Jedis jedis = jedisPool.getResource();
    jedis.setex(telephone + RedisConstants.SENDTYPE_ORDER, 300, String.valueOf(validateCode));
    jedis.close();
  }

  /**
   * 将随机生成的验证码保存到redis中，以备校验
   * 场景：使用邮箱快速登录时
   * @param telephone
   * @param validateCode
   */
  private void setIntoRedis4Login(String telephone, int validateCode) {
    Jedis jedis = jedisPool.getResource();
    jedis.setex(telephone + RedisConstants.SENDTYPE_LOGIN, 300, String.valueOf(validateCode));
    jedis.close();
  }

}
