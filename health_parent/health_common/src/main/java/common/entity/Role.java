package common.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/** 角色 */
@Data
public class Role implements Serializable {
  private Integer id;
  private String name; // 角色名称
  private String keyword; // 角色关键字，用于权限控制
  private String description; // 描述
  private Set<User> users = new HashSet<>();
  private Set<Permission> permissions = new HashSet<>();
  private LinkedHashSet<Menu> menus = new LinkedHashSet<>();
}
