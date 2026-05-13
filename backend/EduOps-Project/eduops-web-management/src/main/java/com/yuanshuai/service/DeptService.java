package com.yuanshuai.service;

import com.yuanshuai.pojo.Dept;

import java.util.List;

public interface DeptService {
//    查询所有的部门信息
    List<Dept> findAll();
//  删除部门数据
    void deleteById(Integer id);
//  添加部门数据
    void add(Dept dept);

    Dept getById(Integer id);

    void update(Dept dept);
}
