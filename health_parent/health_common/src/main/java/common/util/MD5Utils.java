package common.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {
  /** 使用md5的算法进行加密 */
  public static String md5(String plainText) {
    byte[] secretBytes;
    try {
      secretBytes = MessageDigest.getInstance("md5").digest(plainText.getBytes());
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("没有md5这个算法！");
    }
    // 16进制数字
    String md5code = new BigInteger(1, secretBytes).toString(16);
    // 如果生成数字未满32位，需要在前面补0
    if (md5code.length() < 32) {
      StringBuilder builder = new StringBuilder();
      // 补32-len个 0
      while (builder.length() < 32 - md5code.length()) {
        builder.append(0);
      }
      builder.append(md5code);
      md5code = builder.toString();
    }
    return md5code;
  }

  public static void main(String[] args) {
    System.out.println(md5("1234"));
  }
}
