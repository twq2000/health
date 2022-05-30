package provider.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.constant.MessageConstants;
import common.constant.RedisConstants;
import common.entity.Member;
import common.protocol.Result;
import common.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import provider.mapper.MemberMapper;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import service.MemberService;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

@Service(interfaceClass = MemberService.class)
@Transactional(rollbackFor = Exception.class)
public class MemberServiceImpl implements MemberService {

  @Autowired
  private MemberMapper memberMapper;
  @Resource(name = "jedisPool")
  private JedisPool jedisPool;
  // 时长30天
  private static final int ONE_MONTH = 60 * 60 * 24 * 30;

  @Override
  public Result login(Map<String, String> loginInfo, HttpServletResponse response) throws JsonProcessingException {
    String telephone = loginInfo.get("telephone");
    String expected = getFromRedis4Login(telephone);
    if (expected == null || !expected.equals(loginInfo.get("validateCode"))) {
      return new Result(false, MessageConstants.VALIDATECODE_ERROR);
    }
    Member member = findByTelephone(telephone);
    // 如果当前用户还不是会员，则自动将他注册为会员
    if (member == null) {
      member = new Member();
      member.setPhoneNumber(telephone);
      member.setRegTime(new Date());
      register(member);
    }
    // 写入Cookie，负责跟踪该用户的行为
    Cookie cookie = new Cookie("login_member_telephone", telephone);
    cookie.setPath("/");
    cookie.setMaxAge(ONE_MONTH);
    response.addCookie(cookie);
    // 将用户信息存入redis中（模拟session）
    setIntoRedis(member);
    return new Result(true, MessageConstants.LOGIN_SUCCESS);
  }

  private void setIntoRedis(Member member) throws JsonProcessingException {
    String telephone = member.getPhoneNumber();
    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(member);
    Jedis jedis = jedisPool.getResource();
    jedis.setex(telephone, 60 * 30, json);
  }

  @Override
  public Member findByTelephone(String telephone) {
    return memberMapper.findByTelephone(telephone);
  }

  @Override
  public void register(Member member) {
    if (member.getPassword() != null) {
      // 对密码进行加密
      member.setPassword(MD5Utils.md5(member.getPassword()));
    }
    memberMapper.insert(member);
  }

  /**
   * 根据邮箱号，从redis中查找验证码
   * @param telephone
   * @return
   */
  private String getFromRedis4Login(String telephone) {
    Jedis jedis = jedisPool.getResource();
    String expected = jedis.get(telephone + RedisConstants.SENDTYPE_LOGIN);
    jedis.close();
    return expected;
  }

}
