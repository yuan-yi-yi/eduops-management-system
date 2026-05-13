package com.yuanshuai.controller;

import com.yuanshuai.anno.Log;
import com.yuanshuai.pojo.PageResult;
import com.yuanshuai.pojo.Result;
import com.yuanshuai.pojo.Student;
import com.yuanshuai.pojo.StudentQueryParam;
import com.yuanshuai.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/students")
public class StudentController {
    @Autowired
    private StudentService studentService;

    /**
     * 条件分页查询学员
     *
     * @param studentQueryParam
     * @return
     */
    @GetMapping
    public Result list(StudentQueryParam studentQueryParam) {
        log.info("条件分页查询学员：{}", studentQueryParam);
        PageResult<Student> studentList = studentService.page(studentQueryParam);
        return Result.success(studentList);
    }

    /**
     * 添加学员信息
     *
     * @param student
     * @return
     */
    @Log
    @PostMapping
    public Result save(@RequestBody Student student) {
        log.info("添加学员信息: {}", student);
        studentService.insert(student);
        return Result.success();
    }

    /**
     * 根据ID查询学生
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result getInfo(@PathVariable Integer id) {
        log.info("根据ID查询学生: {}", id);
        Student student = studentService.getById(id);
        return Result.success(student);
    }

    /**
     * 修改学员的数据信息
     *
     * @param student
     * @return
     */
    @Log
    @PutMapping
    public Result update(@RequestBody Student student) {
        log.info("修改学员的数据信息: {}", student);
        studentService.update(student);
        return Result.success();
    }

    /**
     * 批量删除学生信息
     *
     * @param ids
     * @return
     */
    @Log
    @DeleteMapping("/{ids}")
    public Result delete(@PathVariable List<Integer> ids) {
        log.info("批量删除学生信息: {}", ids);
        studentService.delete(ids);
        return Result.success();
    }

    /**
     * 修改学员的违纪数据信息
     *
     * @param id
     * @param score
     * @return
     */
    @Log
    @PutMapping("/violation/{id}/{score}")
    public Result violate(@PathVariable Integer id, @PathVariable Integer score) {
        log.info("修改学员的违纪数据信息: 学生ID={}, 违规分数={}", id, score);
        studentService.violate(id, score);
        return Result.success();
    }



}