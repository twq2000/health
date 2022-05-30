package provider.mapper;

import com.github.pagehelper.Page;
import common.entity.CheckItem;

import java.util.List;

public interface CheckItemMapper {

  /**
   * 向 t_checkitem表 添加一个记录
   * @param checkItem 需要录入的数据
   */
  void insert(CheckItem checkItem);

  /**
   * 查询 t_checkitem表 的所有记录
   * @return 数据集合
   */
  List<CheckItem> findAll();

  /**
   * 按条件向 t_checkitem表 查询记录
   * @param queryString 查询条件
   * @return 分页助手对象
   */
  Page<CheckItem> findByCondition(String queryString);

  /**
   * 按id查询中间表t_checkgroup_checkitem 的记录条数
   * @param id checkitem_id
   * @return 记录条数
   */
  long findCountByCheckItemId(Integer id);

  /**
   * 根据id，删除 t_checkitem表 的记录
   * @param id 需要被删除的记录的id值
   */
  void deleteById(Integer id);

  /**
   * 更新 t_checkitem表 的记录
   * @param checkItem 需要被更新的数据信息
   */
  void update(CheckItem checkItem);
}
