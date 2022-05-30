package provider.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import common.entity.CheckGroup;
import common.protocol.PageResult;
import common.protocol.QueryPageBean;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import provider.mapper.CheckGroupMapper;
import service.CheckGroupService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = CheckGroupService.class)
@Transactional(rollbackFor = Exception.class)
public class CheckGroupServiceImpl implements CheckGroupService {

  @Autowired
  private CheckGroupMapper checkGroupMapper;

  @Override
  public void add(CheckGroup checkGroup, Integer[] checkitemIds) {
    // 向 t_checkgroup表 添加记录
    checkGroupMapper.insert(checkGroup);
    // 向中间表 t_checkgroup_checkitem 添加关联记录
    bindCheckGroupAndCheckItem(checkGroup.getId(), checkitemIds);
  }

  @Override
  public PageResult findPage(QueryPageBean requestParams) {
    Integer currentPage = requestParams.getCurrentPage();
    Integer pageSize = requestParams.getPageSize();
    String queryString = requestParams.getQueryString();
    // 基于mybatis框架提供的分页助手插件，完成分页查询
    if (Strings.isNotEmpty(queryString)) {
      currentPage = 1;
    }
    PageHelper.startPage(currentPage, pageSize);
    Page<CheckGroup> checkGroupPage = checkGroupMapper.findByCondition(queryString);
    return new PageResult(checkGroupPage.getTotal(), checkGroupPage.getResult());
  }

  @Override
  public List<Integer> findCheckItemIdsByCheckGroupId(Integer checkgroupId) {
    return checkGroupMapper.findCheckItemIdsByCheckGroupId(checkgroupId);
  }

  @Override
  public void update(CheckGroup checkGroup, Integer[] checkitemIds) {
    // 更新 t_checkgroup表 的记录
    checkGroupMapper.update(checkGroup);
    // 关联操作：更新中间表 t_checkgroup_checkitem 的相关记录
    // 具体实现：先将中间表的相关记录全部删除，然后再重新添加关联记录
    checkGroupMapper.deleteTemporaryByCheckGroupId(checkGroup.getId());
    bindCheckGroupAndCheckItem(checkGroup.getId(), checkitemIds);
  }

  @Override
  public List<CheckGroup> findAll() {
    return checkGroupMapper.findAll();
  }

  @Override
  public void deleteById(Integer checkgroupId) {
    // 首先删除中间表t_checkgroup_checkitem 的关联记录
    checkGroupMapper.deleteTemporaryByCheckGroupId(checkgroupId);
    // 然后删除 t_checkgroup 的对应记录
    checkGroupMapper.deleteById(checkgroupId);
  }

  /**
   * 向中间表 t_checkgroup_checkitem 新增记录，实现关联添加
   * @param checkGroupId checkGroupId
   * @param checkitemIds checkitemIds
   */
  private void bindCheckGroupAndCheckItem(Integer checkGroupId, Integer[] checkitemIds) {
    if (checkitemIds != null && checkitemIds.length > 0) {
      Map<String, Integer> map = new HashMap<>(2);
      for (Integer checkitemId : checkitemIds) {
        map.put("checkgroup_id", checkGroupId);
        map.put("checkitem_id", checkitemId);
        checkGroupMapper.bindCheckGroupAndCheckItem(map);
      }
    }
  }

}
