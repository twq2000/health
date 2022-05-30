package service;

import common.entity.OrderSetting;

import java.util.List;
import java.util.Map;

public interface OrderSettingService {

  /**
   * 向mapper层传递数据：批量添加预约记录
   * @param orderSettingList controller层传过来的预约记录集合
   */
  void add(List<OrderSetting> orderSettingList);

  /**
   * 向mapper层传递数据：根据提供的月份，查询该月的所有预约数据，最终展示在页面的日历上
   * @param date 年-月
   * @return
   */
  List<Map<String, Integer>> findOrderSettingByMonth(String date);

  /**
   * 向mapper层传递数据：修改该日的可预约人数
   * @param orderSetting
   */
  void updateNumberByOrderDate(OrderSetting orderSetting);
}
