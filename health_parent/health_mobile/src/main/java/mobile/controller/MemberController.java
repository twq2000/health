package mobile.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.fasterxml.jackson.core.JsonProcessingException;
import common.constant.MessageConstants;
import common.protocol.Result;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.MemberService;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping("/member")
public class MemberController {

  @Reference private MemberService memberService;

  @RequestMapping("/login")
  /* loginInfo={telephone: "twq3137@qq.com", validateCode: "1111"} */
  Result login(@RequestBody Map<String, String> loginInfo, HttpServletResponse response) {
    try {
      return memberService.login(loginInfo, response);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      return new Result(false, MessageConstants.LOGIN_FAIL);
    }
  }
}
