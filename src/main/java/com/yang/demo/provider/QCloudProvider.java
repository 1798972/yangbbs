package com.yang.demo.provider;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.*;
import com.qcloud.cos.region.Region;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.URL;
import java.util.Date;
import java.util.UUID;


/**
 * @Author Yiang37
 * @Date 2020/2/25 21:10
 * Description: 腾讯云对象存储
 */
@Component
public class QCloudProvider {

    @Value("${qcloud.secretid}")
    private String secretid;
    @Value("${qcloud.secretkey}")
    private String secretkey;

    /*// 指定要上传到 COS 上对象键
    static String key = "mydemo.jpg";*/

    // 简单文件上传, 最大支持 5 GB, 适用于小文件上传, 建议 20M以下的文件使用该接口
    //file里面填写本地图片的位置 我这里是相对项目的位置，在项目下有src/test/demo.jpg这张图片
    public String upload(File file, String fileName) {
        // 1 初始化用户身份信息
        COSCredentials cred = new BasicCOSCredentials(secretid, secretkey);
        // 2 设置bucket的区域
        ClientConfig clientConfig = new ClientConfig(new Region("ap-guangzhou"));
        // 3 生成cos客户端
        COSClient cosClient = new COSClient(cred, clientConfig);
        //桶的名字
        String bucketName = "yang-tcos-1258124344";

        //名字的处理
        String targetName;

        String[] path = fileName.split("\\.");  //点的转义 \\.
        if (path.length > 1) {
            //targetName   a77aebed-77c9-41e5-8f1b-07286cbc8fbc.png
            targetName = UUID.randomUUID().toString() + "." + path[path.length - 1];
        } else {
            return null;
        }
        //动态生成对象键名
        String key = targetName;
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, file);

        PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
        //etag    fdd68975c693dcb355cd6c4dfee6f3ab
//        String etag = putObjectResult.getETag();  // 获取文件的 etag
//        return etag;

        //关闭后返回文件链接  要开启公有读的属性
        cosClient.shutdown();
        Date expiration = new Date(new Date().getTime() + 24 * 60 * 60 * 365 *10 );
        URL oldurl = cosClient.generatePresignedUrl(bucketName, key, expiration);
        // 获得链接后解析字符串并且返回
        // 先将ur
        String url = oldurl.toString();
        // http://jobpic-1258185724.cos.ap-guangzhou.myqcloud.com/image/T.jpg?sign
        // 直接查找到第一个？的位置
        url = url.substring(0, url.indexOf("?"));
        // 开始解析字符串
        return url;
    }

    // 设置要下载到的本地路径
//    public void download() {
//        File downFile = new File("src/test/medemo.jpg");
//        GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, key);
//        ObjectMetadata downObjectMeta = cosClient.getObject(getObjectRequest, downFile);
//    }

    // 指定要删除的 bucket 和对象键
//    public void del() {
//        cosClient.deleteObject(bucketName, key);
//    }

}