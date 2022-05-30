package provider.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import common.entity.CheckItem;
import common.protocol.PageResult;
import common.protocol.QueryPageBean;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import provider.mapper.CheckItemMapper;
import service.CheckItemService;

import java.util.List;

@Service(interfaceClass = CheckItemService.class)
@Transactional(rollbackFor = Exception.class)
public class CheckItemServiceImpl implements CheckItemService {

  @Autowired private CheckItemMapper checkItemMapper;

  @Override
  public void add(CheckItem checkItem) {
    checkItemMapper.insert(checkItem);
  }

  @Override
  public PageResult findPage(QueryPageBean requestParams) {
    Integer currentPage = requestParams.getCurrentPage();
    Integer pageSize = requestParams.getPageSize();
    String queryString = requestParams.getQueryString();
    // 基于mybatis框架提供的分页助手插件，完成分页查询
    // 当进行条件查询时，自动将curPage设为1（从搜索结果的第1页开始展示）
    if (Strings.isNotEmpty(queryString)) {
      currentPage = 1;
    }
    PageHelper.startPage(currentPage, pageSize);
    Page<CheckItem> checkItemPage = checkItemMapper.findByCondition(queryString);
    return new PageResult(checkItemPage.getTotal(), checkItemPage.getResult());
  }

  @Override
  public void deleteById(Integer id) {
    // 首先需要判断当前检查项checkitem是否已经与某个检查组checkgroup相关联
    long count = checkItemMapper.findCountByCheckItemId(id);
    if (count > 0) {
      throw new RuntimeException("当前检查项checkitem已经被关联到其他检查组checkgroup，不允许删除");
    }
    checkItemMapper.deleteById(id);
  }

  @Override
  public void update(CheckItem checkItem) {
    checkItemMapper.update(checkItem);
  }

  @Override
  public List<CheckItem> findAll() {
    return checkItemMapper.findAll();
  }

}
