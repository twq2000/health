package common.test;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.junit.jupiter.api.Test;

/** 测试使用七牛云提供的SDK，将本地的图片上传到七牛云服务器 */
public class QiNiuTest {

  @Test
  void testAdd() {
    // region0() 代表的是华东地区
    Configuration conf = new Configuration(Region.region0());
    UploadManager uploadManager = new UploadManager(conf);
    // 上传凭证（账户与密钥）、空间名称、本地文件路径
    String accessKey = "TeScKkSG40nHQVnCk9slW_SGMYaPBmviS6L778KD";
    String secretKey = "X0Aw0AYG6_WwEnxoMXyEM7S_BrywejyqnpATQYlV";
    String bucket = "twq-health-space";
    String localFilePath = "E:\\BaiduNetdiskDownload\\day04\\资源\\图片资源\\03a36073-a140-4942-9b9b-712cecb144901.jpg";
    // 鉴权操作
    Auth auth = Auth.create(accessKey, secretKey);
    String upToken = auth.uploadToken(bucket);
    try {
      // 将本地文件上传至云服务器。参数key在默认不指定的情况下，会以该文件内容的hash值作为文件名
      Response response = uploadManager.put(localFilePath, null, upToken);
      // 解析上传成功的结果
      DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
      // 打印文件名和hash值
      System.out.println(putRet.key);
      System.out.println(putRet.hash);
    }
    catch (QiniuException ex1) {
      Response r = ex1.response;
      System.err.println(r.toString());
      try {
        System.err.println(r.bodyString());
      }
      catch (QiniuException ex2) {
        // ignore
      }
    }
  }

  @Test
  void testDelete() {
    Configuration cfg = new Configuration(Region.region0());
    String accessKey = "TeScKkSG40nHQVnCk9slW_SGMYaPBmviS6L778KD";
    String secretKey = "X0Aw0AYG6_WwEnxoMXyEM7S_BrywejyqnpATQYlV";
    String bucket = "twq-health-space";
    String key = "FuM1Sa5TtL_ekLsdkYWcf5pyjKGu";
    Auth auth = Auth.create(accessKey, secretKey);
    BucketManager bucketManager = new BucketManager(auth, cfg);
    try {
      bucketManager.delete(bucket, key);
    }
    catch (QiniuException ex) {
      //如果遇到异常，说明删除失败
      System.err.println(ex.code());
      System.err.println(ex.response.toString());
    }

  }

}
