package backend.security;

import com.alibaba.dubbo.config.annotation.Reference;
import common.entity.Permission;
import common.entity.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component("springSecurityUserService")
public class SpringSecurityUserService implements UserDetailsService {

  @Reference private UserService userService;

  /**
   * 根据用户名，查询数据库的用户信息
   *
   * @param username
   * @return
   * @throws UsernameNotFoundException
   */
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    common.entity.User user = userService.findByUsername(username);
    if (user == null) {
      return null;
    }
    List<GrantedAuthority> authorityList = new ArrayList<>();
    Set<Role> roles = user.getRoles();
    for (Role role : roles) {
      // 为用户授予其角色
      authorityList.add(new SimpleGrantedAuthority(role.getKeyword()));
      Set<Permission> permissions = role.getPermissions();
      for (Permission permission : permissions) {
        // 为每个角色授予其权限
        authorityList.add(new SimpleGrantedAuthority(permission.getKeyword()));
      }
    }
    return new User(username, user.getPassword(), authorityList);
  }
}
