package com.kaishengit.crm.mapper;

import com.kaishengit.crm.entity.Staff;
import com.kaishengit.crm.example.StaffExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface StaffMapper {
    long countByExample(StaffExample example);

    int deleteByExample(StaffExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Staff record);

    int insertSelective(Staff record);

    List<Staff> selectByExample(StaffExample example);

    Staff selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Staff record, @Param("example") StaffExample example);

    int updateByExample(@Param("record") Staff record, @Param("example") StaffExample example);

    int updateByPrimaryKeySelective(Staff record);

    int updateByPrimaryKey(Staff record);

    Staff verify(@Param(value = "staffNum") String staffNum,
                 @Param(value = "password") String password);
    List<Staff> findStaffByParamWithDept(Map<String, Object> params);

    Long countByDeptId(@Param("deptId") Integer deptId);

    List<Map<String,Object>> forCustomerChart();
}