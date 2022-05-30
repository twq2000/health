package provider.mapper;

import com.github.pagehelper.Page;
import common.entity.CheckGroup;

import java.util.List;
import java.util.Map;

public interface CheckGroupMapper {

  /**
   * 向 t_checkgroup表 添加一条记录
   * @param checkGroup 需要录入的数据
   */
  void insert(CheckGroup checkGroup);

  /**
   * 向中间表 t_checkgroup_checkitem表 添加一条记录
   * @param map 数据
   */
  void bindCheckGroupAndCheckItem(Map<String, Integer> map);

  /**
   * 按条件向 t_checkitem表 查询记录
   * @param queryString 查询条件
   * @return 分页助手对象
   */
  Page<CheckGroup> findByCondition(String queryString);

  /**
   * 按检查组的checkgroupId，向中间表 t_checkgroup_checkitem 查询包含的所有检查项checkitem的id值
   * @param checkgroupId checkgroupId
   * @return 所有包含的检查项checkitem的id值
   */
  List<Integer> findCheckItemIdsByCheckGroupId(Integer checkgroupId);

  /**
   * 更新 t_checkgroup表 的记录
   * @param checkGroup 需要被更新的数据信息
   */
  void update(CheckGroup checkGroup);

  /**
   * 根据id，删除中间表 t_checkgroup_checkitem 的相关数据
   * @param id checkgroupId
   */
  void deleteTemporaryByCheckGroupId(Integer id);

  /**
   * 根据id，删除表 t_checkgroup 的相关数据
   * @param checkgroupId checkgroupId
   */
  void deleteById(Integer checkgroupId);

  /**
   * 查询 t_checkgroup表 的所有记录
   * @return 数据集合
   */
  List<CheckGroup> findAll();
}
