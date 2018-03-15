package com.xyw.spbodemo.service;


import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.xyw.spbodemo.controller.NewsController;
import com.xyw.spbodemo.util.ToutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class QiniuService {

    private static final Logger logger = LoggerFactory.getLogger(QiniuService.class);

    //构造一个带指定Zone对象的配置类
    Configuration cfg = new Configuration(Zone.zone0());
    //...其他参数参考类注释
    UploadManager uploadManager = new UploadManager(cfg);
    //...生成上传凭证，然后准备上传
    String accessKey = "Ao3UI055aFsBOWwoOOMZPg8vPJICToe3Eor1wwns";
    String secretKey = "-nRhhT16xHPcoMUFj6_XPZBtVxal5QwrNyQ3Txme";
    String bucket = "nowcoder";
    //如果是Windows情况下，格式是 D:\\qiniu\\test.png
    String localFilePath = "/home/xyw/qiniu/test.png";
    //默认不指定key的情况下，以文件内容的hash值作为文件名
    String key = null;
    Auth auth = Auth.create(accessKey, secretKey);
    String upToken = auth.uploadToken(bucket);

    public String saveImage(MultipartFile file) throws IOException {
        try {

            //xx.  = adfa.jpg
            int dotPos = file.getOriginalFilename().lastIndexOf(".");
            if (dotPos < 0) {
                return null;
            }
            String fileExt = file.getOriginalFilename().substring(dotPos + 1).toLowerCase();
            if (!ToutiaoUtil.isFileAllowed(fileExt)) {
                return null;
            }
            // fsd-sfsdf-sfsd-sdfsd
            String fileName = UUID.randomUUID().toString().
                    replaceAll("-", "") + "." + fileExt;

            Response response = uploadManager.put(file.getBytes(), fileName, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);

            System.out.println(response.bodyString());
            return ToutiaoUtil.QINIU_DOMAIN_PREFIX + putRet.key;
        } catch (QiniuException ex) {
            logger.error("the exception of qi niu" + ex.getMessage());
            return null;
        }

    }

}
