package com.hsx.utils;

import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.PutObjectRequest;
//import com.hsx.config.OssProperties;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.UUID;

public class AliyunOSSUtil {


    // 读取配置
    private static String endpoint = "oss-cn-beijing.aliyuncs.com"; //111
    //private static String endpoint = "https://oss-cn-beijing.aliyuncs.com";
    private static String accessKeyId = "LTAI5tP58pfiyc6HjEjynpUU";
    private static String accessKeySecret = "f4E3mZlVuEPwqC40w3pHmYlIwtFH9o";
    private static String bucketName = "hsx-hb";

    /**
     * 创建存储空间
     * 参数：
     * bucketName：存储空间名
     * */
    public static void insertBucket(String bucketName) {
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        // 创建存储空间。
        ossClient.createBucket(bucketName);
        // 关闭OSSClient。
        ossClient.shutdown();
    }

    /**
     * 上传头像
     * 参数：
     *   bucketName：存储空间名
     *   fileName：文件名
     * */
    public static String uploadLocalFiles(Long userId,  MultipartFile file) throws IOException {
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        //获取文件传输
        InputStream inputStream = file.getInputStream();

        //获取文件名称
        String fileName = file.getOriginalFilename();

        //创建一个UUID
        String uuid = UUID.randomUUID().toString().replace("-","");

        fileName = userId +"/" + "avatar" +"/" +  uuid + fileName;


        byte [] bytes = null;
        try {
            bytes = file.getBytes();
        }catch (Exception e){
            e.printStackTrace();
        }

        // 创建PutObjectRequest对象。
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName,new ByteArrayInputStream(bytes));
        // 上传文件。
        ossClient.putObject(putObjectRequest);
        // 关闭OSSClient。
        ossClient.shutdown();
        return  "https://"+ bucketName+"."+endpoint+"/"+fileName;
    }

    /**
     * 文件下载到本地
     * 参数：
     *   bucketName：存储空间名
     *   fileName：文件名
     *   localFile: 本地文件路径
     * */
    public static void download(String fileName, String localFile) {
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        // 下载OSS文件到本地文件。如果指定的本地文件存在会覆盖，不存在则新建。
        ossClient.getObject(new GetObjectRequest(bucketName, fileName), new File(localFile));
        // 关闭OSSClient。
        ossClient.shutdown();
    }

    /**
     * 删除文件
     * 参数：
     *   fileName：文件名
     * */
    public static void deleteFile(String fileName) {
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        // 删除文件。如需删除文件夹，请将ObjectName设置为对应的文件夹名称。如果文件夹非空，则需要将文件夹下的所有object删除后才能删除该文件夹。
        ossClient.deleteObject(bucketName, fileName);
        // 关闭OSSClient。
        ossClient.shutdown();
    }


    /**
     * 上传视频
     * 参数：
     *   headImg:MultipartFile类型的文件
     *   fileName：文件名
     * */
    public static void uploadFileByte(MultipartFile headImg,String fileName) {

        //转换为字节数组
        byte[] bytes = null;
        try {
            bytes = headImg.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        // 上传Byte数组。
        ossClient.putObject(bucketName, fileName, new ByteArrayInputStream(bytes));
        // 关闭OSSClient。
        ossClient.shutdown();
    }

    /**
     * 视频截取帧
     * 参数：
     *   bucketName：存储空间名
     *   fileName：文件名
     * */
    public static URL videoInterceptCover(String fileName) {
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        // 设置视频截帧操作。
        String style = "video/snapshot,t_1000,f_jpg,w_800,h_600";
        // 指定过期时间为10分钟。
        Date expiration = new Date(new Date().getTime() + 1000 * 60 * 10);
        GeneratePresignedUrlRequest req = new GeneratePresignedUrlRequest(bucketName, fileName, HttpMethod.GET);
        req.setExpiration(expiration);
        req.setProcess(style);
        URL signedUrl = ossClient.generatePresignedUrl(req);
        //System.out.println(signedUrl);
        // 关闭OSSClient。
        ossClient.shutdown();
        return signedUrl;
    }

    /**
     * 上传网络流至阿里云
     * 参数：
     *   netPath:网络路径
     *   bucketName：存储空间名
     *   fileName：指定文件名
     * */
    public static void uploadNetFile(String netPath,String fileName) throws IOException {

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 上传网络流。
        InputStream inputStream = new URL(netPath).openStream();
        ossClient.putObject(bucketName, fileName, inputStream);

        // 关闭OSSClient。
        ossClient.shutdown();
    }

}

