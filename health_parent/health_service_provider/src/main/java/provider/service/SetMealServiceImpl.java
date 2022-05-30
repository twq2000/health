package provider.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import common.constant.RedisConstants;
import common.entity.Setmeal;
import common.protocol.PageResult;
import common.protocol.QueryPageBean;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import provider.mapper.SetMealMapper;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import service.SetMealService;

import javax.annotation.Resource;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service(interfaceClass = SetMealService.class)
@Transactional(rollbackFor = Exception.class)
public class SetMealServiceImpl implements SetMealService {

  @Autowired
  private SetMealMapper setMealMapper;

  @Resource(name = "jedisPool")
  private JedisPool jedisPool;

  @Resource(name = "freemarkerConfigurer")
  private FreeMarkerConfigurer freeMarkerConfigurer;

  @Value("${outputPath}")
  private String outputPath;

  private static final String MOBILE_SETMEAL_TEMPLATE = "mobile_setmeal.ftl";
  private static final String MOBILE_SETMEAL_DETAIL_TEMPLATE = "mobile_setmeal_detail.ftl";

  @Override
  public void add(Setmeal setmeal, Integer[] checkgroupIds) {
    // 向表 t_setmeal 添加记录
    setMealMapper.insert(setmeal);
    // 向中间表 t_setmeal_checkgroup 添加关联记录
    bindSetMealAndCheckGroup(setmeal.getId(), checkgroupIds);
    // 将图片文件名保存入redis
    setImgIntoJedis(setmeal);
    // 刷新静态页面（setmeal和setmeal_detail）
    generateMobileStaticHtml();
  }

  @Override
  public PageResult findPage(QueryPageBean requestParams) {
    Integer currentPage = requestParams.getCurrentPage();
    Integer pageSize = requestParams.getPageSize();
    String queryString = requestParams.getQueryString();
    PageHelper.startPage(currentPage, pageSize);
    Page<Setmeal> setmealPage = setMealMapper.findByCondition(queryString);
    return new PageResult(setmealPage.getTotal(), setmealPage.getResult());
  }

  @Override
  public void update(Setmeal setmeal, Integer[] checkgroupIds) {
    // 更新
    setMealMapper.update(setmeal);
    // 更新中间表
    setMealMapper.deleteTemporaryBySetMealId(setmeal.getId());
    bindSetMealAndCheckGroup(setmeal.getId(), checkgroupIds);
  }

  @Override
  public List<Integer> findCheckGroupIdsBySetMealId(Integer setmealId) {
    return setMealMapper.findCheckGroupIdsBySetMealId(setmealId);
  }

  @Override
  public void deleteById(Integer setmealId) {
    setMealMapper.deleteById(setmealId);
  }

  @Override
  public List<Setmeal> findAll() {
    return setMealMapper.findAll();
  }

  @Override
  public Setmeal findById(Integer setmealId) {
    return setMealMapper.findById(setmealId);
  }

  /**
   * 向中间表 t_setmeal_checkgroup 新增记录，实现关联添加
   * @param setmealId setmealId
   * @param checkgroupIds checkgroupIds
   */
  private void bindSetMealAndCheckGroup(Integer setmealId, Integer[] checkgroupIds) {
    if (checkgroupIds != null && checkgroupIds.length > 0) {
      Map<String, Integer> map = new HashMap<>(2);
      for (Integer checkgroupId : checkgroupIds) {
        map.put("setmeal_id", setmealId);
        map.put("checkgroup_id", checkgroupId);
        setMealMapper.bindSetMealAndCheckGroup(map);
      }
    }
  }

  /**
   * 将图片文件名保存入redis
   * @param setmeal 数据
   */
  private void setImgIntoJedis(Setmeal setmeal) {
    Jedis jedis = jedisPool.getResource();
    jedis.sadd(RedisConstants.SETMEAL_PIC_DB_RESOURCES, setmeal.getImg());
    jedis.close();
  }

  /**
   * 生成移动端的静态页面
   */
  private void generateMobileStaticHtml() {
    List<Setmeal> setmealList = findAll();
    // 生成setmeal.html对应的静态页面
    generateMobileSetmealHtml(setmealList);
    // 生成setmeal_detail.html对应的静态页面
    generateMobileSetmealDetailHtml(setmealList);
  }

  /**
   * 生成setmeal.html对应的静态页面
   * @param setmealList
   */
  private void generateMobileSetmealHtml(List<Setmeal> setmealList) {
    Map<String, Object> dataMap = new HashMap<>(1);
    dataMap.put("setmealList", setmealList);
    generateHtml(MOBILE_SETMEAL_TEMPLATE, "m_setmeal.html", dataMap);
  }

  /**
   * 生成setmeal_detail.html对应的静态页面
   * @param setmealList
   */
  private void generateMobileSetmealDetailHtml(List<Setmeal> setmealList) {
    Map<String, Object> dataMap = new HashMap<>(1);
    for (Setmeal setmeal : setmealList) {
      dataMap.put("setmeal", findById(setmeal.getId()));
      generateHtml(MOBILE_SETMEAL_DETAIL_TEMPLATE,
          "setmeal_detail_" + setmeal.getId() + ".html",
          dataMap);
    }
  }

  /**
   * 使用FreeMarker技术，生成静态html页面
   * @param templateName
   * @param htmlName
   * @param dataMap
   */
  private void generateHtml(String templateName, String htmlName, Map<String, Object> dataMap) {
    Configuration configuration = freeMarkerConfigurer.getConfiguration();
    BufferedWriter writer = null;
    try {
      Template template = configuration.getTemplate(templateName);
      File htmlFile = new File(outputPath + "/" + htmlName);
      writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(htmlFile)));
      template.process(dataMap, writer);
    } catch (IOException | TemplateException e) {
      e.printStackTrace();
    } finally {
      try {
        // 将缓存区中的内容输出
        Objects.requireNonNull(writer).flush();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

}
