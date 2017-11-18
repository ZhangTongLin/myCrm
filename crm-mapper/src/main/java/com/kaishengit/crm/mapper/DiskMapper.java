package com.kaishengit.crm.mapper;

import com.kaishengit.crm.entity.Disk;
import java.util.List;

import com.kaishengit.crm.example.DiskExample;
import org.apache.ibatis.annotations.Param;

public interface DiskMapper {
    long countByExample(DiskExample example);

    int deleteByExample(DiskExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Disk record);

    int insertSelective(Disk record);

    List<Disk> selectByExampleWithBLOBs(DiskExample example);

    List<Disk> selectByExample(DiskExample example);

    Disk selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Disk record, @Param("example") DiskExample example);

    int updateByExampleWithBLOBs(@Param("record") Disk record, @Param("example") DiskExample example);

    int updateByExample(@Param("record") Disk record, @Param("example") DiskExample example);

    int updateByPrimaryKeySelective(Disk record);

    int updateByPrimaryKeyWithBLOBs(Disk record);

    int updateByPrimaryKey(Disk record);
}