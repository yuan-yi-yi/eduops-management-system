package com.yuanshuai.service;

import com.yuanshuai.pojo.PageResult;
import com.yuanshuai.pojo.Student;
import com.yuanshuai.pojo.StudentQueryParam;

import java.util.List;

public interface StudentService {
    /**
     * 条件分页查询学员
     *
     * @param studentQueryParam
     * @return
     */
    PageResult<Student> page(StudentQueryParam studentQueryParam);

    /**
     * 添加学员信息
     *
     * @param student
     */
    void insert(Student student);

    /**
     * 根据ID查询学生
     *
     * @param id
     * @return
     */
    Student getById(Integer id);

    /**
     * 修改学员的数据信息
     *
     * @param student
     */
    void update(Student student);

    /**
     * 批量删除学生信息
     *
     * @param ids
     */
    void delete(List<Integer> ids);

    /**
     * 修改学员的违纪数据信息
     * @param id
     * @param score
     */
    void violate(Integer id, Integer score);
}