package com.explore.utils;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Random;

public class UploadUtil {
    /**
     * 上传到七牛云
     * @param file 上传的图片
     * @return 七牛云中图片的名字
     */
    final static String accessKey = "4cOk2Qy8mLt8A_llrjSlFFFCbZJbge9Mxq-_s6Os";
    final static String secretKey = "m9HPX2ji1VB_Lc5hUdd2tNNCWv9LGsGD8gD7Jysc";
    final static Configuration cfg = new Configuration(Zone.zone2());
    //...其他参数参考类注释
    final static UploadManager uploadManager = new UploadManager(cfg);
    final static  Auth auth = Auth.create(accessKey, secretKey);
    public static String uploadQiniu(MultipartFile file,String key) {
        //构造一个带指定Zone对象的配置类

        //...生成上传凭证，然后准备上传
        //存储空间的名字
        String bucket = "ashe-image";
        //默认不指定key的情况下，以文件内容的hash值作为文件名

        String upToken = auth.uploadToken(bucket);
        try {
            Response response = uploadManager.put(file.getBytes(), key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            return putRet.key;
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }


    public static String check(MultipartFile file){
        boolean flag = FileUtil.checkFileSize(file.getSize(), 5, "M");
        if (!flag)
        {
            //图片大小超限
            return null;
        }


        if (!file.isEmpty())
        {
            String originalFilename = file.getOriginalFilename();//获取图片文件的名字
            String type = null; //图片类型
            type = originalFilename.indexOf(".") != -1 ?
                    originalFilename.substring(originalFilename.lastIndexOf(".") + 1, originalFilename.length())
                    : null;

            if (type != null)
            {
                if ("GIF".equals(type.toUpperCase()) || "PNG".equals(type.toUpperCase()) || "JPG".equals(type.toUpperCase()))
                {
                    // 新的图片的名称
                    // 设置存放图片文件的路径
                    return System.currentTimeMillis() + getRandomString(15)+"."+type;
                }

            }
        }

        return null;
    }

    private static String getRandomString(int length){
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<length;i++){
            int number=random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }
}
