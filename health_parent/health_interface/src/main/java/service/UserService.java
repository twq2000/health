package service;

import common.entity.User;

public interface UserService {

  /**
   * 根据用户名，查询数据库的用户信息
   *
   * @param username
   * @return
   */
  User findByUsername(String username);

}
