package com.kaishengit.crm.mapper;

import com.kaishengit.crm.entity.DeptStaffKey;
import java.util.List;

import com.kaishengit.crm.example.DeptStaffExample;
import org.apache.ibatis.annotations.Param;

public interface DeptStaffMapper {
    long countByExample(DeptStaffExample example);

    int deleteByExample(DeptStaffExample example);

    int deleteByPrimaryKey(DeptStaffKey key);

    int insert(DeptStaffKey record);

    int insertSelective(DeptStaffKey record);

    List<DeptStaffKey> selectByExample(DeptStaffExample example);

    int updateByExampleSelective(@Param("record") DeptStaffKey record, @Param("example") DeptStaffExample example);

    int updateByExample(@Param("record") DeptStaffKey record, @Param("example") DeptStaffExample example);
}