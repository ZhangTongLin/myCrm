package com.kaishengit.crm.files;

import java.io.IOException;
import java.io.InputStream;

/**
 * 上传文件储存的接口
 * @author Administrator.
 */
public interface FileStore {

    /**
     * 保存文件
     * @param inputStream 文件的输入流
     * @param fileName 文件的原有名称
     * @return 文件的存储名称或者路径
     */
    String saveFile(InputStream inputStream,String fileName) throws IOException;

    /**
     * 文件下载
     * @param fileName 文件存储的名称或者路径
     * @return 包含文件信息 的数组
     */
    byte[] downloadFile(String fileName) throws IOException;

}
