package service;

import common.protocol.Result;

import java.util.Map;

public interface OrderService {

  /**
   * 处理controller层传过来的前端数据：对表单数据进行校验。
   * 如果通过校验，则向mapper层传递数据：将表单数据录入表 t_ordersetting
   * @param orderInfo
   * @return
   */
  Result submit(Map<String, String> orderInfo);

  /**
   * 处理controller层传过来的前端数据：查询指定记录
   * 最终需要将查询结果展示在页面上
   * @param id
   * @return
   * @throws
   */
  Map<String, Object> findById(Integer id) throws Exception;
}
