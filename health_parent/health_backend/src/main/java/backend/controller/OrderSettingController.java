package backend.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import common.constant.MessageConstants;
import common.entity.OrderSetting;
import common.protocol.Result;
import common.util.POIUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import service.OrderSettingService;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {

  @Reference private OrderSettingService orderSettingService;

  @RequestMapping("/upload")
  Result upload(@RequestParam("excelFile") MultipartFile file) {
    try {
      List<String[]> list = POIUtils.readExcel(file);
      List<OrderSetting> orderSettingList = new ArrayList<>();
      DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
      for (String[] strings : list) {
        OrderSetting orderSetting =
            new OrderSetting(dateFormat.parse(strings[0]), Integer.parseInt(strings[1]));
        orderSettingList.add(orderSetting);
      }
      orderSettingService.add(orderSettingList);
      return new Result(true, MessageConstants.IMPORT_ORDERSETTING_SUCCESS);
    } catch (IOException | ParseException e) {
      e.printStackTrace();
      return new Result(false, MessageConstants.IMPORT_ORDERSETTING_FAIL);
    }
  }

  @RequestMapping("/findOrderSettingByMonth")
  Result findOrderSettingByMonth(@RequestParam("date") String date) {
    try {
      List<Map<String, Integer>> orderSettingList =
          orderSettingService.findOrderSettingByMonth(date);
      return new Result(true, MessageConstants.GET_ORDERSETTING_SUCCESS, orderSettingList);
    } catch (Exception e) {
      e.printStackTrace();
      return new Result(false, MessageConstants.GET_ORDERSETTING_FAIL);
    }
  }

  @RequestMapping("/updateNumberByOrderDate")
  Result updateNumberByOrderDate(@RequestBody OrderSetting orderSetting) {
    try {
      orderSettingService.updateNumberByOrderDate(orderSetting);
      return new Result(true, MessageConstants.ORDERSETTING_SUCCESS);
    } catch (Exception e) {
      e.printStackTrace();
      return new Result(false, MessageConstants.ORDERSETTING_FAIL);
    }
  }
}
