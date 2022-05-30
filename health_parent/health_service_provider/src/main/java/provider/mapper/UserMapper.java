package provider.mapper;

import common.entity.User;

public interface UserMapper {

  /**
   * 根据用户名，查询对应的用户信息
   *
   * @param username
   * @return
   */
  User findByUsername(String username);

}
