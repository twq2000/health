package backend.controller;

import common.constant.MessageConstants;
import common.protocol.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

  @RequestMapping("/findUsername")
  Result findUsername() {
    // SpringSecurity框架的底层是由session技术实现的。当完成用户认证后，会自动将当前用户信息保存到context上下文对象中
    User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (user != null) {
      return new Result(true, MessageConstants.GET_USERNAME_SUCCESS, user.getUsername());
    }
    return new Result(false, MessageConstants.GET_USERNAME_FAIL);
  }
}
