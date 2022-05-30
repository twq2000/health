package service;

import com.fasterxml.jackson.core.JsonProcessingException;
import common.entity.Member;
import common.protocol.Result;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface MemberService {

  /**
   * 处理controller层传过来的前端数据：根据邮箱以及其输入的验证码，判断登录是否通过
   * @param loginInfo 登录相关表单数据（包含邮箱号与输入的验证码）
   * @param response  如果登录成功，将用户信息写入浏览器的Cookie中，跟踪行为
   * @return
   */
  Result login(Map<String, String> loginInfo, HttpServletResponse response) throws JsonProcessingException;

  /**
   * 向mapper层传递数据：根据邮箱号，查找会员
   * @param telephone
   * @return
   */
  Member findByTelephone(String telephone);

  /**
   * 向mapper层传递数据：完成用户注册
   * @param member
   */
  void register(Member member);

}
