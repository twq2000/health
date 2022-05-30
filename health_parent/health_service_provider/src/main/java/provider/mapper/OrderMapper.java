package provider.mapper;

import common.entity.Order;
import org.apache.ibatis.annotations.MapKey;

import java.util.List;
import java.util.Map;

public interface OrderMapper {

  /**
   * 统计满足指定条件的记录条数
   * @param order
   * @return
   */
  long findCountByCondition(Order order);

  /**
   * 根据条件，在表 t_order 查询满足该条件的记录
   * @param order
   * @return
   */
  List<Order> findByCondition(Order order);

  /**
   * 向表 t_order 新增一条记录
   * @param order
   */
  void insert(Order order);

  /**
   * 根据id，查询预约详情信息（体检人信息、套餐信息、体检日期、预约类型）
   * @param orderId
   * @return
   */
  @MapKey("id")
  List<Map<String, Object>> findById4Detail(Integer orderId);

}
