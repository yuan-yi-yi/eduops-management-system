package com.yuanshuai.service.impl;

import com.yuanshuai.exception.BusinessException;
import com.yuanshuai.mapper.DeptMapper;
import com.yuanshuai.pojo.Dept;
import com.yuanshuai.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DeptServiceImpl implements DeptService {


    @Autowired
    private DeptMapper deptMapper;


    @Override
    public List<Dept> findAll() {
        return deptMapper.findAll();
    }

    // @Override
    // public void deleteById(Integer id) {
    //     deptMapper.deleteById(id);
    // }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteById(Integer id) {
        // 1. 校验该部门下是否有关联的员工
        int empCount = deptMapper.countEmpByDeptId(id);
        if (empCount > 0) {
            throw new BusinessException("对不起, 该部门下有员工, 不能直接删除!");
        }
        // 2. 校验通过，执行删除操作
        deptMapper.deleteById(id);
    }


    @Override
    public void add(Dept dept) {
//        补全属性，创建时间、更新时间等
        dept.setCreateTime(LocalDateTime.now());
        dept.setUpdateTime(LocalDateTime.now());
        deptMapper.insert(dept);
    }

    @Override
    public Dept getById(Integer id) {
        return deptMapper.getById(id);
    }

    @Override
    public void update(Dept dept) {
//        补全基础属性：更新时间
        dept.setUpdateTime(LocalDateTime.now());
        deptMapper.update(dept);
    }



}
