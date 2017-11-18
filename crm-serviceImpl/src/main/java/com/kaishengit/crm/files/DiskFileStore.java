package com.kaishengit.crm.files;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.UUID;

/**
 * 存入硬盘中
 * @author Administrator.
 */

@Component
public class DiskFileStore implements FileStore {


    @Value("${uploadFile.path}")
    private String filePath;
    /**
     * 保存文件
     *
     * @param inputStream 文件的输入流
     * @param fileName    文件的原有名称
     * @return 文件的存储名称或者路径
     */
    @Override
    public String saveFile(InputStream inputStream, String fileName) throws IOException {


        String newFileName = "";
        if (fileName.lastIndexOf(".") != -1) {
            newFileName = UUID.randomUUID() + fileName.substring(fileName.lastIndexOf("."));
        } else {
            newFileName = UUID.randomUUID().toString();
        }


        FileOutputStream outputStream = new FileOutputStream(new File(filePath,newFileName));
        IOUtils.copy(inputStream,outputStream);

        inputStream.close();
        outputStream.flush();
        outputStream.close();

        return newFileName;

    }

    /**
     * 文件下载
     *
     * @param fileName 文件存储的名称或者路径
     * @return 包含文件信息 的数组
     */
    @Override
    public byte[] downloadFile(String fileName) throws IOException {

        InputStream inputStream = new FileInputStream(new File(filePath,fileName));

        return IOUtils.toByteArray(inputStream);
    }
}
