package provider.mapper;

import common.entity.Role;

import java.util.Set;

public interface RoleMapper {

  /**
   * 根据userId，查询对应的角色信息
   *
   * @param userId
   * @return
   */
  Set<Role> findByUserId(int userId);

}
