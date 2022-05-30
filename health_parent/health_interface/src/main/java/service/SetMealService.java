package service;

import common.entity.Setmeal;
import common.protocol.PageResult;
import common.protocol.QueryPageBean;

import java.util.List;

public interface SetMealService {

  /**
   * 向mapper层传递数据：新增一条记录，同时需要让setmeal关联checkgroup
   * @param setmeal controller层传过来的需要录入的数据
   * @param checkgroupIds controller层传过来的需要关联的检查组id
   */
  void add(Setmeal setmeal, Integer[] checkgroupIds);

  /**
   * 向mapper层传递数据：查询分页数据
   * @param requestParams controller层传过来的分页相关请求参数
   * @return mapper层传回来的分页数据对象
   */
  PageResult findPage(QueryPageBean requestParams);

  /**
   * 向mapper层传递数据：更新一条记录
   * @param setmeal controller层传过来的需要被更新的记录
   * @param checkgroupIds 关联更新（t_setmeal_checkgroup表）的对应记录
   */
  void update(Setmeal setmeal, Integer[] checkgroupIds);

  /**
   * 向mapper层传递数据：根据套餐的setmealId，查询包含的所有检查组checkgroup的id值
   * @param setmealId controller层传过来的套餐setmealId
   * @return mapper层传回来的所有包含的检查组checkgroup的id值
   */
  List<Integer> findCheckGroupIdsBySetMealId(Integer setmealId);

  /**
   * 向mapper层传递数据：删除一条记录
   * @param setmealId controller层传过来的需要被删除的记录的id值
   */
  void deleteById(Integer setmealId);

  /**
   * 向mapper层传递数据：查询所有记录
   * @return
   */
  List<Setmeal> findAll();

  /**
   * 向mapper层传递数据：根据id查询记录
   * @param setmealId
   * @return
   */
  Setmeal findById(Integer setmealId);
}
