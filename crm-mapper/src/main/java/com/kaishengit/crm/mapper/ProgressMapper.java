package com.kaishengit.crm.mapper;

import com.kaishengit.crm.entity.Progress;
import java.util.List;

import com.kaishengit.crm.example.ProgressExample;
import org.apache.ibatis.annotations.Param;

public interface ProgressMapper {
    long countByExample(ProgressExample example);

    int deleteByExample(ProgressExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Progress record);

    int insertSelective(Progress record);

    List<Progress> selectByExample(ProgressExample example);

    Progress selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Progress record, @Param("example") ProgressExample example);

    int updateByExample(@Param("record") Progress record, @Param("example") ProgressExample example);

    int updateByPrimaryKeySelective(Progress record);

    int updateByPrimaryKey(Progress record);
}