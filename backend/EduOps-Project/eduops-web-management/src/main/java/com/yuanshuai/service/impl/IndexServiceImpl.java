package com.yuanshuai.service.impl;

import com.yuanshuai.exception.BusinessException;
import com.yuanshuai.mapper.ClazzMapper;
import com.yuanshuai.mapper.DeptMapper;
import com.yuanshuai.mapper.EmpMapper;
import com.yuanshuai.mapper.StudentMapper;
import com.yuanshuai.pojo.Emp;
import com.yuanshuai.pojo.IndexStats;
import com.yuanshuai.service.IndexService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class IndexServiceImpl implements IndexService {

    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private ClazzMapper clazzMapper;
    @Autowired
    private EmpMapper empMapper;
    @Autowired
    private DeptMapper deptMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public IndexStats getIndexStats(String username) {
        log.info("获取首页统计数据，用户名：{}", username);

        // 根据用户名查询员工信息
        Emp emp = empMapper.getByUsername(username);
        if (emp == null) {
            throw new BusinessException("用户不存在");
        }

        // 构建统计数据
        IndexStats stats = new IndexStats();
        stats.setStudentCount(studentMapper.count());
        stats.setClazzCount(clazzMapper.count());
        stats.setEmpCount(empMapper.count());
        stats.setDeptCount(deptMapper.count());
        stats.setEmpImage(emp.getImage());
        stats.setEmpName(emp.getName());

        return stats;
    }
}