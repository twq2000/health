package provider.service;

import com.alibaba.dubbo.config.annotation.Service;
import common.constant.MessageConstants;
import common.constant.RedisConstants;
import common.entity.Member;
import common.entity.Order;
import common.entity.OrderSetting;
import common.protocol.Result;
import common.util.DateUtils;
import common.util.MailUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import provider.mapper.MemberMapper;
import provider.mapper.OrderMapper;
import provider.mapper.OrderSettingMapper;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import service.OrderService;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

@Service(interfaceClass = OrderService.class)
@Transactional(rollbackFor = Exception.class)
public class OrderServiceImpl implements OrderService {

  @Autowired
  private OrderSettingMapper orderSettingMapper;
  @Autowired
  private MemberMapper memberMapper;
  @Autowired
  private OrderMapper orderMapper;
  @Resource(name = "jedisPool")
  private JedisPool jedisPool;

  @Override
  /*
  {"setmealId":"1","sex":"1","name":"唐文潜","telephone":"15800750637","validateCode":"1234","idCard":"310109200010121511","orderDate":"2021-12-31"}
  */
  public Result submit(Map<String, String> orderInfo) {
    // 首先，对用户输入的验证码进行校验
    String telephone = orderInfo.get("telephone");
    if (Strings.isEmpty(telephone)) {
      return new Result(false, MessageConstants.VALIDATECODE_EMPTY);
    }
    String expected = getFromRedis4Order(telephone);
    if (expected == null || !expected.equals(orderInfo.get("validateCode"))) {
      return new Result(false, MessageConstants.VALIDATECODE_ERROR);
    }
    // 至此，验证码通过校验。接下来，将用户填写的表单数据存入数据库
    orderInfo.put("orderType", Order.ORDER_TYPE_WX);
    Result result;
    try {
      result = order(orderInfo);
    } catch (Exception e) {
      e.printStackTrace();
      return new Result(false, MessageConstants.ORDER_FAIL);
    }
    // 至此，预约信息已成功录入数据库。接下来，发送预约成功的短信提示（这里使用邮箱发送验证码代替）
    if (result.isFlag()) {
      String orderDate = orderInfo.get("orderDate");
      MailUtils.sendMail(telephone, "预约成功！预约时间：" + orderDate, "预约成功");
    }
    return result;
  }

  @Override
  public Map<String, Object> findById(Integer id) throws Exception {
    Map<String, Object> orderInfo = orderMapper.findById4Detail(id).get(0);
    if (orderInfo != null) {
      // 对于日期字段，需要特殊处理它的格式
      Date orderDate = (Date) orderInfo.get("orderDate");
      orderInfo.put("orderDate", DateUtils.parseDate2String(orderDate));
      return orderInfo;
    }
    throw new Exception();
  }

  /**
   * 向mapper层传递数据：将表单数据录入表 t_ordersetting
   * @param orderInfo
   * @return
   * @throws Exception
   */
  private Result order(Map<String, String> orderInfo) throws Exception {
    // 检查用户所选择的预约日期是否接受预约，如果不接受则无法进行当前预约
    String orderDate = orderInfo.get("orderDate");
    Date date = DateUtils.parseString2Date(orderDate);
    OrderSetting orderSetting = orderSettingMapper.findByOrderDate(date);
    if (orderSetting == null) {
      return new Result(false, MessageConstants.SELECTED_DATE_CANNOT_ORDER);
    }
    // 检查用户所选择的预约日期是否已经约满，如果已约满则无法进行当前预约
    if (orderSetting.getNumber() <= orderSetting.getReservations()) {
      return new Result(false, MessageConstants.ORDER_FULL);
    }
    // 检查用户是否重复预约（同一个人在同一天预约了同一个套餐），如果重复则无法进行当前预约
    String telephone = orderInfo.get("telephone");
    Member member = memberMapper.findByTelephone(telephone);
    if (member != null) {
      int memberId = member.getId();
      int setmealId = Integer.parseInt(orderInfo.get("setmealId"));
      Order order = new Order();
      order.setMemberId(memberId);
      order.setOrderDate(date);
      order.setSetmealId(setmealId);
      long total = orderMapper.findCountByCondition(order);
      if (total > 0) {
        return new Result(false, MessageConstants.HAS_ORDERED);
      }
    } else {
      // 至此，不存在重复预约的情况。
      // 如果不是会员，自动完成注册并预约。否则直接预约即可
      member = new Member();
      member.setName(orderInfo.get("name"));
      member.setSex(orderInfo.get("sex"));
      member.setIdCard(orderInfo.get("idCard"));
      member.setPhoneNumber(telephone);
      member.setRegTime(new Date());
      memberMapper.insert(member);
    }
    // 至此，已通过所有检查。进行预约
    // 更新表 t_ordersetting （更新已预约人数+1）
    orderSetting.setReservations(orderSetting.getReservations() + 1);
    orderSettingMapper.updateReservationsByOrderDate(orderSetting);
    // 将预约数据插入表 t_order
    Order order =
        new Order(
            member.getId(),
            date,
            orderInfo.get("orderType"),
            Order.ORDER_STATUS_NO,
            Integer.parseInt(orderInfo.get("setmealId"))
        );
    orderMapper.insert(order);
    return new Result(true, MessageConstants.ORDER_SUCCESS, order.getId());
  }

  /**
   * 根据邮箱号，从redis中查找验证码
   * @param telephone
   * @return
   */
  private String getFromRedis4Order(String telephone) {
    Jedis jedis = jedisPool.getResource();
    String expected = jedis.get(telephone + RedisConstants.SENDTYPE_ORDER);
    jedis.close();
    return expected;
  }

}
