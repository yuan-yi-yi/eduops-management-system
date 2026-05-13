package com.yuanshuai.mapper;

import com.yuanshuai.pojo.EmpExpr;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

// 员工工作经验Mapper接口
@Mapper
public interface EmpExpMapper {
    // 批量新增员工工作经历
    void insertBatch(List<EmpExpr> exprList);

    // 根据员工empId，批量删除员工的工作经历
    void deleteByEmpIds(List<Integer> empIds);
}
