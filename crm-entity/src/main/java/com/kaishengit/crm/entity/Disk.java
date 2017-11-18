package com.kaishengit.crm.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 
 */
public class Disk implements Serializable {


    public static final String DISK_TYPE_FILE = "file";
    public static final String DISK_TYPE_FOLDER = "dir";

    private Integer id;

    /**
     * 文件原有的名称
     */
    private String name;

    /**
     * 文件大小
     */
    private String fileSize;

    private Integer pId;

    /**
     * 文件类型dir是文件 file是文件夹
     */
    private String type;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 员工id
     */
    private Integer staffId;

    /**
     * 下载次数
     */
    private Integer donwloadTime;

    /**
     * 下载时候所需要的密码
     */
    private String password;

    /**
     * 保存的名字
     */
    private String saveName;

    /**
     * md5加密之后的文件内容
     */
    private String md5;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public Integer getpId() {
        return pId;
    }

    public void setpId(Integer pId) {
        this.pId = pId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getStaffId() {
        return staffId;
    }

    public void setStaffId(Integer staffId) {
        this.staffId = staffId;
    }

    public Integer getDonwloadTime() {
        return donwloadTime;
    }

    public void setDonwloadTime(Integer donwloadTime) {
        this.donwloadTime = donwloadTime;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSaveName() {
        return saveName;
    }

    public void setSaveName(String saveName) {
        this.saveName = saveName;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }
}