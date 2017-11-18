package com.kaishengit.test;

import org.apache.commons.io.IOUtils;
import org.csource.common.MyException;
import org.csource.fastdfs.*;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Administrator.
 */
public class FastDFSTest {


    @Test
    public void uploadTest() throws IOException, MyException {

        Properties properties = new Properties();
        properties.setProperty(ClientGlobal.PROP_KEY_TRACKER_SERVERS,"192.168.169.88:22122,192.168.169.89:22122");

        //初始化配置
        ClientGlobal.initByProperties(properties);

        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getConnection();

        StorageServer storageServer = null;
        StorageClient storageClient = new StorageClient(trackerServer,storageServer);

        InputStream inputStream = new FileInputStream("e:/temp/1.jar");
        byte[] bytes = IOUtils.toByteArray(inputStream);


        String[] strings = storageClient.upload_file(bytes,"jar",null);

        for (String str : strings) {
            System.out.println(str);
        }

        inputStream.close();
    }

    @Test
    public void downloadTest() throws IOException, MyException {

        Properties properties = new Properties();
        properties.setProperty(ClientGlobal.PROP_KEY_TRACKER_SERVERS,"192.168.169.88:22122");

        //初始化配置
        ClientGlobal.initByProperties(properties);

        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getConnection();

        StorageClient storageClient = new StorageClient(trackerServer,null);


        byte[] bytes = storageClient.download_file("group1","wKipWFoOWQCAQJ8YAAFyEvMAfEk42.jpeg");


        //InputStream inputStream = IOUtils.toInputStream(new String(bytes),"UTF-8");

        FileOutputStream fileOutputStream = new FileOutputStream("e:/new.jpeg");


        //IOUtils.copy(inputStream,fileOutputStream);

        fileOutputStream.write(bytes,0,bytes.length);

        fileOutputStream.flush();
        fileOutputStream.close();
        //inputStream.close();

    }


}
