package common.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/** 预约设置 */
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class OrderSetting implements Serializable {
  private Integer id;
  @NonNull
  private Date orderDate; // 预约设置日期
  @NonNull
  private int number; // 可预约人数
  private int reservations; // 已预约人数
}
