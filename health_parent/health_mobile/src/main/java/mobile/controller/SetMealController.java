package mobile.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import common.constant.MessageConstants;
import common.entity.Setmeal;
import common.protocol.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import service.SetMealService;

import java.util.List;

@RestController
@RequestMapping("/setmeal")
public class SetMealController {

  @Reference
  private SetMealService setMealService;

  @RequestMapping("/findAll")
  Result findAll() {
    try {
      List<Setmeal> setmealList = setMealService.findAll();
      return new Result(true, MessageConstants.GET_SETMEAL_LIST_SUCCESS, setmealList);
    } catch (Exception e) {
      return new Result(false, MessageConstants.GET_SETMEAL_LIST_FAIL);
    }
  }

  @RequestMapping("/findById")
  Result findById(@RequestParam("id") Integer setmealId) {
    try {
      Setmeal setmeal = setMealService.findById(setmealId);
      return new Result(true, MessageConstants.QUERY_SETMEAL_SUCCESS, setmeal);
    } catch (Exception e) {
      e.printStackTrace();
      return new Result(false, MessageConstants.QUERY_SETMEAL_FAIL);
    }
  }

}
