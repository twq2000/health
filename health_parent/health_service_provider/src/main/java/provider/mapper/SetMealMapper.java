package provider.mapper;

import com.github.pagehelper.Page;
import common.entity.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetMealMapper {

  /**
   * 向 t_setmeal表 添加一条记录
   * @param setmeal 需要录入的数据
   */
  void insert(Setmeal setmeal);

  /**
   * 向中间表 t_setmeal_checkgroup 添加一条记录
   * @param map 数据
   */
  void bindSetMealAndCheckGroup(Map<String, Integer> map);

  /**
   * 按条件向 t_checkitem表 查询记录
   * @param queryString 查询条件
   * @return 分页助手对象
   */
  Page<Setmeal> findByCondition(String queryString);

  /**
   * 更新 t_setmeal表 的记录
   * @param setmeal 需要被更新的数据信息
   */
  void update(Setmeal setmeal);

  /**
   * 根据id，删除中间表 t_setmeal_checkgroup 的相关数据
   * @param id setmealId
   */
  void deleteTemporaryBySetMealId(Integer id);

  /**
   * 按套餐的setmealId，向中间表 t_setmeal_checkgroup 查询包含的所有检查组checkgroup的id值
   * @param setmealId setmealId
   * @return 所有包含的检查组checkgroup的id值
   */
  List<Integer> findCheckGroupIdsBySetMealId(Integer setmealId);

  /**
   * 根据id，删除表 t_checkgroup 的相关数据
   * @param setmealId setmealId
   */
  void deleteById(Integer setmealId);

  /**
   * 查询表 t_setmeal 的所有记录
   * @return
   */
  List<Setmeal> findAll();

  /**
   * 根据id，查询表 t_setmeal 的指定记录
   * @param setmealId
   * @return
   */
  Setmeal findById(Integer setmealId);
}
