package com.kaishengit.crm.service.impl;

import com.kaishengit.crm.entity.Disk;
import com.kaishengit.crm.example.DiskExample;
import com.kaishengit.crm.exception.ServiceException;
import com.kaishengit.crm.files.FileStore;
import com.kaishengit.crm.mapper.DiskMapper;
import com.kaishengit.crm.service.DiskService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 公司网盘的业务层
 * @author Administrator.
 */
@Service
public class DiskServiceImpl implements DiskService {


    @Autowired
    private DiskMapper diskMapper;

    @Autowired
    @Qualifier("fastFileStore")
    private FileStore fileStore;

    @Value("${uploadFile.path}")
    private String filePath;

    /**
     * 新添加一个文件夹
     *
     * @param pId
     * @param staffId
     * @param name
     */
    @Override
    public void addNewDisk(Integer pId, Integer staffId, String name) {

        Disk disk = new Disk();
        disk.setName(name);
        disk.setpId(pId);
        disk.setStaffId(staffId);
        disk.setType(Disk.DISK_TYPE_FOLDER);
        disk.setUpdateTime(new Date());

        diskMapper.insertSelective(disk);

    }

    /**
     * 显示文件及文件夹
     *
     * @return
     * @param pId
     */
    @Override
    public List<Disk> findHomeAllDisk(Integer pId) {
        DiskExample diskExample = new DiskExample();
        diskExample.createCriteria().andPIdEqualTo(pId);
        return diskMapper.selectByExample(diskExample);
    }

    /**
     * 根据文件的id查询文件
     *
     * @param id
     * @return
     */
    @Override
    public Disk findDiskById(Integer id) {
        return diskMapper.selectByPrimaryKey(id);
    }

    /**
     * 文件上传
     *
     * @param pId         父id
     * @param staffId     用户的id
     * @param inputStream 输入流
     * @param fileSize    文件大小
     * @param fileName    文件原有的名称
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void uploadFile(Integer pId,
                           Integer staffId,
                           InputStream inputStream,
                           Long fileSize, String fileName) {

        Disk disk = new Disk();
        disk.setType(Disk.DISK_TYPE_FILE);
        disk.setDonwloadTime(0);
        disk.setUpdateTime(new Date());
        disk.setStaffId(staffId);
        disk.setpId(pId);
        disk.setName(fileName);

        disk.setFileSize(FileUtils.byteCountToDisplaySize(fileSize));

        try {

            String newFileName = fileStore.saveFile(inputStream,fileName);
            disk.setSaveName(newFileName);

        } catch (IOException e) {
            throw new ServiceException("上传到磁盘异常",e);
        }

        diskMapper.insertSelective(disk);

    }

    /**
     * 根据id获得对应文件的输入流
     *
     * @param id 文件的id
     * @return 对应 的输入流
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public InputStream downloadFileById(Integer id) throws IOException {

        Disk disk = diskMapper.selectByPrimaryKey(id);
        System.out.println("saveName>>>>>>>>>>>" + disk.getSaveName());

        if (disk == null || disk.getType().equals(Disk.DISK_TYPE_FOLDER)) {
            throw new ServiceException("文件已经被删除或者不存在");
        }

        disk.setDonwloadTime(disk.getDonwloadTime() + 1);
        diskMapper.updateByPrimaryKeyWithBLOBs(disk);

        byte[] bytes = fileStore.downloadFile(disk.getSaveName());
        InputStream inputStream = new ByteArrayInputStream(bytes);

        return inputStream;
    }
}
