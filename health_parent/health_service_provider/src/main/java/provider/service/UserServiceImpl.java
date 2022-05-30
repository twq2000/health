package provider.service;

import com.alibaba.dubbo.config.annotation.Service;
import common.entity.Permission;
import common.entity.Role;
import common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import provider.mapper.PermissionMapper;
import provider.mapper.RoleMapper;
import provider.mapper.UserMapper;
import service.UserService;

import java.util.Set;

@Service(interfaceClass = UserService.class)
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {

  @Autowired private UserMapper userMapper;
  @Autowired private RoleMapper roleMapper;
  @Autowired private PermissionMapper permissionMapper;

  @Override
  public User findByUsername(String username) {
    User user = userMapper.findByUsername(username);
    // 找不到用户，抛出异常更好？
    if (user == null) {
      return null;
    }
    int userId = user.getId();
    Set<Role> roleSet = roleMapper.findByUserId(userId);
    if (roleSet != null && !roleSet.isEmpty()) {
      for (Role role : roleSet) {
        // 查询每个role的permission
        int roleId = role.getId();
        Set<Permission> permissionSet = permissionMapper.findByRoleId(roleId);
        if (permissionSet != null && !permissionSet.isEmpty()) {
          role.setPermissions(permissionSet);
        }
      }
      user.setRoles(roleSet);
    }
    return user;
  }

}
