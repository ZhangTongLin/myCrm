package com.kaishengit.crm.service;

import com.kaishengit.crm.entity.Disk;

import java.io.InputStream;
import java.util.List;

/**
 * 网盘的服务层
 * @author Administrator.
 */
public interface DiskService {
    /**
     * 新添加一个文件夹
     * @param pId
     * @param staffId
     * @param name
     */
    void addNewDisk(Integer pId, Integer staffId, String name);

    /**
     * 显示文件及文件夹
     * @return
     * @param pId
     */
    List<Disk> findHomeAllDisk(Integer pId);

    /**
     * 根据文件的id查询文件
     * @param id
     * @return
     */
    Disk findDiskById(Integer id);

    /**
     * 文件上传
     * @param pId 父id
     * @param staffId 用户的id
     * @param inputStream 输入流
     * @param fileSize 文件大小
     * @param fileName 文件原有的名称
     */
    void uploadFile(Integer pId, Integer staffId, InputStream inputStream, Long fileSize, String fileName);
}
