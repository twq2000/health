package common.protocol;

import lombok.*;

import java.io.Serializable;

/** 封装返回结果 */
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class Result implements Serializable {
  @NonNull
  private Boolean flag; // 反映执行结果
  @NonNull
  private String message; // 返回结果信息
  private Object data; // 返回数据

  public boolean isFlag() {
    return flag;
  }

}
