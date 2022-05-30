package backend.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import common.constant.MessageConstants;
import common.entity.CheckGroup;
import common.protocol.PageResult;
import common.protocol.QueryPageBean;
import common.protocol.Result;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import service.CheckGroupService;

import java.util.List;

@RestController
@RequestMapping("/checkgroup")
public class CheckGroupController {

  @Reference private CheckGroupService checkGroupService;

  @RequestMapping("/add")
  Result add(
      @RequestBody CheckGroup checkGroup, @RequestParam("checkitemIds") Integer[] checkitemIds) {
    try {
      checkGroupService.add(checkGroup, checkitemIds);
      return new Result(true, MessageConstants.ADD_CHECKGROUP_SUCCESS);
    } catch (Exception e) {
      e.printStackTrace();
      return new Result(false, MessageConstants.ADD_CHECKGROUP_FAIL);
    }
  }

  @RequestMapping("/update")
  Result update(@RequestBody CheckGroup checkGroup, Integer[] checkitemIds) {
    try {
      checkGroupService.update(checkGroup, checkitemIds);
      return new Result(true, MessageConstants.UPDATE_CHECKGROUP_SUCCESS);
    } catch (Exception e) {
      e.printStackTrace();
      return new Result(false, MessageConstants.UPDATE_CHECKGROUP_FAIL);
    }
  }

  @RequestMapping("/findPage")
  PageResult findPage(@RequestBody QueryPageBean requestParams) {
    return checkGroupService.findPage(requestParams);
  }

  @RequestMapping("/findCheckItemIdsByCheckGroupId")
  Result findCheckItemIdsByCheckGroupId(@RequestParam("id") Integer checkgroupId) {
    try {
      List<Integer> checkitemIds = checkGroupService.findCheckItemIdsByCheckGroupId(checkgroupId);
      return new Result(true, MessageConstants.QUERY_CHECKITEM_SUCCESS, checkitemIds);
    } catch (Exception e) {
      e.printStackTrace();
      return new Result(false, MessageConstants.QUERY_CHECKITEM_FAIL);
    }
  }

  @RequestMapping("/findAll")
  Result findAll() {
    try {
      List<CheckGroup> checkGroupList = checkGroupService.findAll();
      return new Result(true, MessageConstants.QUERY_CHECKGROUP_SUCCESS, checkGroupList);
    } catch (Exception e) {
      e.printStackTrace();
      return new Result(false, MessageConstants.QUERY_CHECKGROUP_FAIL);
    }
  }

  @RequestMapping("/delete")
  Result delete(@RequestParam("id") Integer checkgroupId) {
    try {
      checkGroupService.deleteById(checkgroupId);
      return new Result(true, MessageConstants.DELETE_CHECKGROUP_SUCCESS);
    } catch (Exception e) {
      e.printStackTrace();
      return new Result(false, MessageConstants.DELETE_CHECKGROUP_FAIL);
    }
  }
}
