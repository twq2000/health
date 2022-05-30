package provider.mapper;

import common.entity.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderSettingMapper {

  /**
   * 根据预约日期，查询 表t_ordersetting 的记录条数
   * @param orderDate
   * @return
   */
  long findCountByOrderDate(Date orderDate);

  /**
   * 更新 表t_ordersetting 的一条记录
   * @param orderSetting
   */
  void updateNumberByOrderDate(OrderSetting orderSetting);

  /**
   * 新增 表t_ordersetting 的一条记录
   * @param orderSetting
   */
  void insert(OrderSetting orderSetting);

  /**
   * 查询某一个月的预约记录
   * @param map {begin: 2021-10-01, end: 2021-10-31}
   * @return
   */
  List<OrderSetting> findOrderSettingByMonth(Map<String, String> map);

  /**
   * 根据日期，查询该日的预约设置数据
   * @param date
   * @return
   */
  OrderSetting findByOrderDate(Date date);

  /**
   * 根据日期，更新表 t_ordersetting 的一条记录
   * @param orderSetting
   */
  void updateReservationsByOrderDate(OrderSetting orderSetting);

}
