package service;

import common.entity.CheckItem;
import common.protocol.PageResult;
import common.protocol.QueryPageBean;

import java.util.List;

public interface CheckItemService {

  /**
   * 向mapper层传递数据：新增一条记录
   * @param checkItem controller层传过来的需要录入的数据
   */
  void add(CheckItem checkItem);

  /**
   * 向mapper层传递数据：查询分页数据
   * @param requestParams controller层传过来的分页相关请求参数
   * @return mapper层传回来的分页数据对象
   */
  PageResult findPage(QueryPageBean requestParams);

  /**
   * 向mapper层传递数据：删除一条记录
   * @param id controller层传过来的需要被删除的id值
   */
  void deleteById(Integer id);

  /**
   * 向mapper层传递数据：更新一条记录
   * @param checkItem controller层传过来的需要被更新的记录
   */
  void update(CheckItem checkItem);

  /**
   * 向mapper层传递数据，查询所有记录
   * @return 所有记录
   */
  List<CheckItem> findAll();

}
