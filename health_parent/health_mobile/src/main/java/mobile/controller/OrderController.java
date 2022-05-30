package mobile.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import common.constant.MessageConstants;
import common.protocol.Result;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import service.OrderService;

import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {

  @Reference
  private OrderService orderService;

  @RequestMapping("/submit")
  Result submit(@RequestBody Map<String, String> orderInfo) {
    return orderService.submit(orderInfo);
  }

  @RequestMapping("/findById")
  Result findById(@RequestParam("id") Integer id) {
    try {
      Map<String, Object> orderInfo =  orderService.findById(id);
      return new Result(true, MessageConstants.QUERY_ORDER_SUCCESS, orderInfo);
    } catch (Exception e) {
      e.printStackTrace();
      return new Result(false, MessageConstants.QUERY_ORDER_FAIL);
    }
  }

}
