package common.constant;

public class RedisConstants {
  // 所有已上传至七牛云服务器的图片集合名称（有效+无效）
  public static final String SETMEAL_PIC_RESOURCES = "setmealPicResources";
  // 实际保存在数据库中的图片集合名称（有效）
  public static final String SETMEAL_PIC_DB_RESOURCES = "setmealPicDbResources";
  // 用于缓存体检预约时发送的验证码（后缀）
  public static final String SENDTYPE_ORDER = "001";
  // 用于缓存手机号快速登录时发送的验证码（后缀）
  public static final String SENDTYPE_LOGIN = "002";
  // 用于缓存找回密码时发送的验证码（后缀）
  public static final String SENDTYPE_GETPWD = "003";
}
