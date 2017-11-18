package com.kaishengit.crm.service.impl;

import com.kaishengit.crm.entity.Disk;
import com.kaishengit.crm.example.DiskExample;
import com.kaishengit.crm.exception.ServiceException;
import com.kaishengit.crm.mapper.DiskMapper;
import com.kaishengit.crm.service.DiskService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

        String newFileName = UUID.randomUUID() + fileName.substring(fileName.lastIndexOf("."));
        disk.setSaveName(newFileName);

        try {

            FileOutputStream fileOutputStream = new FileOutputStream(new File(filePath,newFileName));
            IOUtils.copy(inputStream,fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            inputStream.close();

        } catch (IOException e) {
            throw new ServiceException("上传到磁盘异常",e);
        }

        diskMapper.insertSelective(disk);

    }
}
