package common.util;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;

public class QiniuUtils {
  // 账户与密钥
  private static final String ACCESS_KEY = "TeScKkSG40nHQVnCk9slW_SGMYaPBmviS6L778KD";
  private static final String SECRET_KEY = "X0Aw0AYG6_WwEnxoMXyEM7S_BrywejyqnpATQYlV";
  // 工作空间名
  private static final String BUCKET = "twq-health-space";
  // region0() 代表的是华东地区
  private static final Configuration CONFIGURATION = new Configuration(Region.region0());

  /**
   * 将本地文件上传至云服务器
   * @param filePath 本地文件路径
   * @param fileName 文件名
   */
  public static void upload2Qiniu(String filePath, String fileName) {
    UploadManager uploadManager = new UploadManager(CONFIGURATION);
    // 鉴权操作
    Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
    String upToken = auth.uploadToken(BUCKET);
    try {
      // 将本地文件上传至云服务器
      Response response = uploadManager.put(filePath, fileName, upToken);
      // 解析上传成功的结果
      DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
    }
    catch (QiniuException ex1) {
      Response response = ex1.response;
      try {
        System.err.println(response.bodyString());
      }
      catch (QiniuException ex2) {
        // ignore
      }
    }
  }

  /**
   * 将本地文件上传至云服务器
   * @param file 本地文件的字节数组形式
   * @param fileName 文件名
   */
  public static void upload2Qiniu(byte[] file, String fileName) {
    UploadManager uploadManager = new UploadManager(CONFIGURATION);
    // 鉴权操作
    Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
    String upToken = auth.uploadToken(BUCKET);
    try {
      // 将本地文件上传至云服务器
      Response response = uploadManager.put(file, fileName, upToken);
      // 解析上传成功的结果
      DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
    }
    catch (QiniuException ex1) {
      Response response = ex1.response;
      try {
        System.err.println(response.bodyString());
      }
      catch (QiniuException ex2) {
        // ignore
      }
    }
  }

  /**
   * 将文件从云服务器上删除
   * @param fileName 文件名
   */
  public static void deleteFileFromQiniu(String fileName) {
    // 鉴权操作
    Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
    BucketManager bucketManager = new BucketManager(auth, CONFIGURATION);
    try {
      bucketManager.delete(BUCKET, fileName);
    }
    catch (QiniuException ex) {
      System.err.println(ex.code());
      System.err.println(ex.response.toString());
    }
  }

}
