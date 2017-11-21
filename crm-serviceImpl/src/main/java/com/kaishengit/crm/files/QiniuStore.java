package com.kaishengit.crm.files;

import com.google.gson.Gson;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * 对七牛的上传下载
 * @author Administrator.
 */
@Component
public class QiniuStore implements FileStore {


    @Value("${qiniu.accessKey}")
    private String accessKey;
    @Value("${qiniu.secretyKey}")
    private String secretyKey;
    @Value("${qiniu.bucktName}")
    private String bucktName;
    @Value("${qiniu.domain}")
    private String domain;

    /**
     * 保存文件
     *
     * @param inputStream 文件的输入流
     * @param fileName    文件的原有名称
     * @return 文件的存储名称或者路径
     */
    @Override
    public String saveFile(InputStream inputStream, String fileName) throws IOException {
        Configuration configuration = new Configuration(Zone.zone1());
        UploadManager uploadManager = new UploadManager(configuration);

        Auth auth = Auth.create(accessKey,secretyKey);
        String uploadToken = auth.uploadToken(bucktName);

        byte[] bytes = IOUtils.toByteArray(inputStream);
        Response response = uploadManager.put(bytes,null,uploadToken);
        DefaultPutRet defaultPutRet = new Gson().fromJson(response.bodyString(),DefaultPutRet.class);

        return defaultPutRet.key;
    }

    /**
     * 文件下载
     *
     * @param fileName 文件存储的名称或者路径
     * @return 包含文件信息 的数组
     */
    @Override
    public byte[] downloadFile(String fileName) throws IOException {
        String newFileName = String.format("%s%s",domain,fileName);
        InputStream inputStream = new URL(newFileName).openStream();
        return IOUtils.toByteArray(inputStream);
    }
}
