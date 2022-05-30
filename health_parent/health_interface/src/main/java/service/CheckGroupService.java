package service;

import common.entity.CheckGroup;
import common.protocol.PageResult;
import common.protocol.QueryPageBean;

import java.util.List;

public interface CheckGroupService {

  /**
   * 向mapper层传递数据：新增一条记录checkgroup，同时需要让checkgroup关联checkitem
   * @param checkGroup controller层传过来的需要录入的数据
   * @param checkitemsIds controller层传过来的需要关联的检查项id
   */
  void add(CheckGroup checkGroup, Integer[] checkitemsIds);

  /**
   * 向mapper层传递数据：查询分页数据
   * @param requestParams controller层传过来的分页相关请求参数
   * @return mapper层传回来的分页数据对象
   */
  PageResult findPage(QueryPageBean requestParams);

  /**
   * 向mapper层传递数据：根据检查组checkgroupId，查询包含的所有检查项checkitem的id值
   * @param checkgroupId controller层传过来的检查组checkgroupId
   * @return mapper层传回来的所有包含的检查项checkitem的id值
   */
  List<Integer> findCheckItemIdsByCheckGroupId(Integer checkgroupId);

  /**
   * 向mapper层传递数据：更新一条记录
   * @param checkGroup controller层传过来的需要被更新的记录
   * @param checkitemIds 关联更新（t_checkgroup_checkitem表）的对应记录
   */
  void update(CheckGroup checkGroup, Integer[] checkitemIds);

  /**
   * 向mapper层传递数据：查询所有记录
   * @return 所有记录
   */
  List<CheckGroup> findAll();

  /**
   * 向mapper层传递数据：删除一条记录
   * @param checkgroupId controller层传过来的需要被删除的记录的id值
   */
  void deleteById(Integer checkgroupId);

}
