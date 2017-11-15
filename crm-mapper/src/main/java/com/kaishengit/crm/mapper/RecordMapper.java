package com.kaishengit.crm.mapper;

import com.kaishengit.crm.entity.Record;
import java.util.List;

import com.kaishengit.crm.example.RecordExample;
import org.apache.ibatis.annotations.Param;

public interface RecordMapper {
    long countByExample(RecordExample example);

    int deleteByExample(RecordExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Record record);

    int insertSelective(Record record);

    List<Record> selectByExample(RecordExample example);

    Record selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Record record, @Param("example") RecordExample example);

    int updateByExample(@Param("record") Record record, @Param("example") RecordExample example);

    int updateByPrimaryKeySelective(Record record);

    int updateByPrimaryKey(Record record);

    List<Record> selectWithCustomerByStaffId(@Param("staffId") Integer staffId);

    Record selectWithCustomerById(@Param("id") Integer id);
}