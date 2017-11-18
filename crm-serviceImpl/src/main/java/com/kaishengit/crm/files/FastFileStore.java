package com.kaishengit.crm.files;

import com.kaishengit.crm.exception.ServiceException;
import org.apache.commons.io.IOUtils;
import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.rmi.ServerError;
import java.rmi.ServerException;
import java.util.Properties;

/**
 * 上传到fastdfs中
 * @author Administrator.
 */
@Component
public class FastFileStore implements FileStore {

    @Value("${tracker.ip}")
    private String trackerIp;

    /**
     * 保存文件
     *
     * @param inputStream 文件的输入流
     * @param fileName    文件的原有名称
     * @return 文件的存储名称或者路径
     */
    @Override
    public String saveFile(InputStream inputStream, String fileName) throws IOException {

        String extendName = "";

        if (fileName.indexOf(".") != -1) {
            extendName = fileName.substring(fileName.lastIndexOf(".") + 1);
        }

        StorageClient storageClient = getStorageClient();
        byte[] bytes = IOUtils.toByteArray(inputStream);

        try {
            String[] strings = storageClient.upload_file(bytes,extendName,null);

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(strings[0])
                         .append("#")
                         .append(strings[1]);
            return stringBuilder.toString();

        } catch (MyException e) {
            throw new ServerException("存入fastDFS异常",e);
        }

    }

    /**
     * 文件下载
     *
     * @param fileName 文件存储的名称或者路径
     * @return 包含文件信息 的数组
     */
    @Override
    public byte[] downloadFile(String fileName) throws IOException {
        StorageClient storageClient = getStorageClient();

        String[] strings = fileName.split("#");
        try {
            byte[] bytes = storageClient.download_file(strings[0],strings[1]);
            return bytes;
        } catch (MyException e) {
            throw new ServerException("获取文件异常",e);
        }


    }

    /**
     * 获取storageClient对象
     * @return
     */
    private StorageClient getStorageClient() {
        try {
            Properties properties = new Properties();
            properties.setProperty(ClientGlobal.PROP_KEY_TRACKER_SERVERS,trackerIp);

            ClientGlobal.initByProperties(properties);

            TrackerClient trackerClient = new TrackerClient();
            TrackerServer trackerServer = trackerClient.getConnection();

            StorageClient storageClient = new StorageClient(trackerServer,null);

            return storageClient;

        } catch (MyException  | IOException e) {
            throw new ServiceException("获取storageClient异常",e);
        }

    }

}
