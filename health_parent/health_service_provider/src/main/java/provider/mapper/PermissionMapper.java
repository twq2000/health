package provider.mapper;

import common.entity.Permission;

import java.util.Set;

public interface PermissionMapper {

  /**
   * 根据roleId，查询对应的权限信息
   *
   * @param roleId
   * @return
   */
  Set<Permission> findByRoleId(int roleId);

}
