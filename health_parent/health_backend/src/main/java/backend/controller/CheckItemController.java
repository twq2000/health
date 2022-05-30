package backend.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import common.constant.MessageConstants;
import common.entity.CheckItem;
import common.protocol.PageResult;
import common.protocol.QueryPageBean;
import common.protocol.Result;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import service.CheckItemService;

import java.util.List;

@RestController
@RequestMapping("/checkitem")
public class CheckItemController {

  @Reference private CheckItemService checkItemService;

  @PreAuthorize("hasAuthority('CHECKITEM_ADD')")
  @RequestMapping("/add")
  Result add(@RequestBody CheckItem checkItem) {
    try {
      checkItemService.add(checkItem);
      return new Result(true, MessageConstants.ADD_CHECKITEM_SUCCESS);
    } catch (Exception e) {
      e.printStackTrace();
      return new Result(false, MessageConstants.ADD_CHECKITEM_FAIL);
    }
  }

  @PreAuthorize("hasAuthority('CHECKITEM_EDIT')")
  @RequestMapping("/update")
  Result update(@RequestBody CheckItem checkItem) {
    try {
      checkItemService.update(checkItem);
      return new Result(true, MessageConstants.UPDATE_CHECKITEM_SUCCESS);
    } catch (Exception e) {
      e.printStackTrace();
      return new Result(false, MessageConstants.UPDATE_CHECKITEM_FAIL);
    }
  }

  @PreAuthorize("hasAuthority('CHECKITEM_QUERY')")
  @RequestMapping("/findPage")
  PageResult findPage(@RequestBody QueryPageBean requestParams) {
    return checkItemService.findPage(requestParams);
  }

  @PreAuthorize("hasAuthority('CHECKITEM_DELETE')")
  @RequestMapping("/delete")
  Result delete(@RequestParam("id") Integer checkitemId) {
    try {
      checkItemService.deleteById(checkitemId);
      return new Result(true, MessageConstants.DELETE_CHECKITEM_SUCCESS);
    } catch (Exception e) {
      e.printStackTrace();
      return new Result(false, MessageConstants.DELETE_CHECKITEM_FAIL);
    }
  }

  @RequestMapping("/findAll")
  Result findAll() {
    try {
      List<CheckItem> checkItems = checkItemService.findAll();
      return new Result(true, MessageConstants.QUERY_CHECKITEM_SUCCESS, checkItems);
    } catch (Exception e) {
      e.printStackTrace();
      return new Result(false, MessageConstants.QUERY_CHECKITEM_FAIL);
    }
  }
}
