package provider.service;

import com.alibaba.dubbo.config.annotation.Service;
import common.entity.OrderSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import provider.mapper.OrderSettingMapper;
import service.OrderSettingService;

import java.util.*;

@Service(interfaceClass = OrderSettingService.class)
@Transactional(rollbackFor = Exception.class)
public class OrderSettingServiceImpl implements OrderSettingService {

  @Autowired
  private OrderSettingMapper orderSettingMapper;

  @Override
  public void add(List<OrderSetting> orderSettingList) {
    for (OrderSetting orderSetting : orderSettingList) {
      // 根据该预约日期，判断是否已经存在关于该日期的记录
      long count = orderSettingMapper.findCountByOrderDate(orderSetting.getOrderDate());
      // 如果已有，那么执行的是更新操作
      if (count > 0) {
        orderSettingMapper.updateNumberByOrderDate(orderSetting);
      } else {
        // 如果没有，那么执行的是新增操作
        orderSettingMapper.insert(orderSetting);
      }
    }
  }

  @Override
  public List<Map<String, Integer>> findOrderSettingByMonth(String date) {
    List<Map<String, Integer>> data = new ArrayList<>();
    Map<String, String> map = new HashMap<>(2);
    // 例：date==2021-10
    // begin==2021-10-01
    map.put("begin", date + "-1");
    // end==2021-10-31
    map.put("end", date + getLastDay(date));
    List<OrderSetting> orderSettingList = orderSettingMapper.findOrderSettingByMonth(map);
    Calendar calendar = Calendar.getInstance();
    for (OrderSetting orderSetting : orderSettingList) {
      Map<String, Integer> jsonMap = new HashMap<>(3);
      calendar.setTime(orderSetting.getOrderDate());
      jsonMap.put("date", calendar.get(Calendar.DAY_OF_MONTH));
      jsonMap.put("number", orderSetting.getNumber());
      jsonMap.put("reservations", orderSetting.getReservations());
      data.add(jsonMap);
    }
    return data;
  }

  @Override
  public void updateNumberByOrderDate(OrderSetting orderSetting) {
    long count = orderSettingMapper.findCountByOrderDate(orderSetting.getOrderDate());
    if (count > 0) {
      orderSettingMapper.updateNumberByOrderDate(orderSetting);
    } else {
      orderSettingMapper.insert(orderSetting);
    }
  }

  /**
   * 根据 年-月 判断当月共有几天
   * @param date 年-月
   * @return
   */
  private String getLastDay(String date) {
    String month = date.substring(5);
    if ("02".equals(month)) {
      return Integer.parseInt(date.substring(0, 5)) % 4 == 0 ? "29" : "28";
    }
    switch (month) {
      case "1":
      case "3":
      case "5":
      case "7":
      case "8":
      case "10":
      case "12":
        return "-31";
      case "4":
      case "6":
      case "9":
      case "11":
        return "-30";
      default: return "";
    }
  }

}
