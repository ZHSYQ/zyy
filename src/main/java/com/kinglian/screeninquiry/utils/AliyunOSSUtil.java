package com.kinglian.screeninquiry.utils;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.CreateBucketRequest;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.kinglian.screeninquiry.config.OSSConfig;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @project: zblog
 * @author: zhs
 * @date: 2019/4/9 14:26
 * @package: com.kinglian.screeninquiry.utils
 * @description:
 */
@Component
public class AliyunOSSUtil {

    @Autowired
    private OSSConfig constantConfig;
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(AliyunOSSUtil.class);

    /**
     * 上传文件
     */
    public String upLoad(File file) {
        logger.info("------OSS文件上传开始--------" + file.getName());
        String endpoint = constantConfig.getLXIMAGE_END_POINT();
        System.out.println("获取到的Point为:" + endpoint);
        String accessKeyId = constantConfig.getLXIMAGE_ACCESS_KEY_ID();
        String accessKeySecret = constantConfig.getLXIMAGE_ACCESS_KEY_SECRET();
        String bucketName = constantConfig.getLXIMAGE_BUCKET_NAME();
        String fileHost = constantConfig.getLXIMAGE_FILE_HOST();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = format.format(new Date());

        // 判断文件
        if (file == null) {
            return null;
        }
        OSSClient client = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        try {
            // 判断容器是否存在,不存在就创建
            if (!client.doesBucketExist(bucketName)) {
                client.createBucket(bucketName);
                CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);
                createBucketRequest.setCannedACL(CannedAccessControlList.PublicRead);
                client.createBucket(createBucketRequest);
            }
            // 设置文件路径和名称
            String fileUrl =   (dateStr + "/" + UUID.randomUUID().toString().replace("-", "") + "-" + file.getName());
            // 上传文件
            PutObjectResult result = client.putObject(new PutObjectRequest(bucketName, fileUrl, file));
            // 设置权限(公开读)
            client.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
            if (result != null) {
                logger.info("------OSS文件上传成功------" + fileUrl);
                return fileHost+"/"+fileUrl;

            }
        } catch (OSSException oe) {
            logger.error(oe.getMessage());
        } catch (ClientException ce) {
            logger.error(ce.getErrorMessage());
        } finally {
            if (client != null) {
                client.shutdown();
            }
        }
        return null;
    }
}

