package com.example.sstest.controller;

import com.example.sstest.utils.Result;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

/**
 * 文件上传控制器
 */
@RestController
@RequestMapping(value = "/cos")
public class FileController {

    @Value("${spring.cos.accessKey}")
    private String accessKey;
    @Value("${spring.cos.secretKey}")
    private String secretKey;
    @Value("${spring.cos.bucket}")
    private String bucket;
    @Value("${spring.cos.bucketName}")
    private String bucketName;
    @Value("${spring.cos.path}")
    private String path;
    @Value("${spring.cos.prefix}")
    private String prefix;

    /**
     * 上传到腾讯云服务器
     *
     * @return
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public Result Upload(@RequestParam(value = "file") MultipartFile file) {
        if (file == null) {
            return Result.failed("文件为空");
        }
        String oldFileName = file.getOriginalFilename();
        String eName = oldFileName.substring(oldFileName.lastIndexOf("."));
        String newFileName = UUID.randomUUID() + eName;
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DATE);
        // 1 初始化用户身份信息(secretId, secretKey)
        COSCredentials cred = new BasicCOSCredentials(accessKey, secretKey);
        // 2 设置bucket的区域, COS地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
        ClientConfig clientConfig = new ClientConfig(new Region(bucket));
        // 3 生成cos客户端
        COSClient cosclient = new COSClient(cred, clientConfig);
        // bucket的命名规则为{name}-{appid} ，此处填写的存储桶名称必须为此格式
        String bucketName = this.bucketName;

        // 简单文件上传, 最大支持 5 GB, 适用于小文件上传, 建议 20 M 以下的文件使用该接口
        // 大文件上传请参照 API 文档高级 API 上传
        File localFile = null;
        try {
            localFile = File.createTempFile("temp", null);
            file.transferTo(localFile);
            // 指定要上传到 COS 上的路径
            String key = this.prefix + year + "/" + month + "/" + day + "/" + newFileName;
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, localFile);
            PutObjectResult putObjectResult = cosclient.putObject(putObjectRequest);
            HashMap<String, String> objectObjectHashMap = new HashMap<>();
            String filePath = this.path + putObjectRequest.getKey();
            objectObjectHashMap.put("path", filePath);
            return Result.ok(objectObjectHashMap, "上传成功");
        } catch (IOException e) {
            return Result.failed(e.getMessage());
        } finally {
            // 关闭客户端(关闭后台线程)
            cosclient.shutdown();
        }
    }

    @DeleteMapping("/delete")
    public Result deleteFile(@RequestParam("fileName") String fileName) {
        String temp = fileName.substring(fileName.indexOf('/')+2);
        String key = temp.substring(temp.indexOf('/'));
        COSClient cosclient = null;
        try {
            //初始化用户身份信息(secretId,secretKey)
            COSCredentials cosCredentials = new BasicCOSCredentials(accessKey, secretKey);
            //设置bucket的区域
            ClientConfig clientConfig = new ClientConfig(new Region(bucket));
            // 生成cos客户端
            cosclient = new COSClient(cosCredentials, clientConfig);
            // 指定要删除的 bucket 和路径
            cosclient.deleteObject(bucketName, key);
            // 关闭客户端(关闭后台线程)
            cosclient.shutdown();
        } catch (CosClientException e) {
            e.printStackTrace();
            return Result.failed("删除失败");
        }
        return Result.ok("删除成功");
    }


}