package common.entity;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/** 体检预约信息 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Order implements Serializable {
  public static final String ORDER_TYPE_TELEPHONE = "电话预约";
  public static final String ORDER_TYPE_WX = "微信预约";
  public static final String ORDER_STATUS_YES = "已到诊";
  public static final String ORDER_STATUS_NO = "未到诊";

  private Integer id;
  @NonNull
  private Integer memberId; // 会员id
  @NonNull
  private Date orderDate; // 预约日期
  @NonNull
  private String orderType; // 预约类型 电话预约/微信预约
  @NonNull
  private String orderStatus; // 预约状态（是否到诊）
  @NonNull
  private Integer setmealId; // 体检套餐id

}
