package com.yuanshuai.controller;

import com.yuanshuai.anno.Log;
import com.yuanshuai.pojo.Dept;
import com.yuanshuai.pojo.Result;
import com.yuanshuai.service.DeptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
// 请求处理类，交给IOC容器管理
@RestController
//    标记请求路径，统一处理
@RequestMapping("/depts")
public class DeptController {
//    定义一个处理方法，响应前端发过来的请求
//    注入DeptService的接口，面向接口编程
    @Autowired
    private DeptService deptService;


    @GetMapping()
    public Result list() {
        log.info("查询所有部门数据");
        List<Dept> deptList = deptService.findAll();
        return Result.success(deptList);
    }

    @Log
    @DeleteMapping
    public Result delete(Integer id) {
        log.info("删除部门数据，id为：{}", id);
        deptService.deleteById(id);
        return Result.success();
    }

    @Log
    @PostMapping()
    public Result add(@RequestBody Dept dept) {
        log.info("添加部门数据，{}", dept);
        deptService.add(dept);
        return  Result.success();
    }

    @GetMapping("/{id}")
    public Result getInfo(@PathVariable Integer id) {
        log.info("获取部门数据，id为：{}", id);
        Dept dept = deptService.getById(id);
        return Result.success(dept);
    }

    @Log
    @PutMapping
    public Result update(@RequestBody Dept dept) {
        log.info("修改部门数据，{}", dept);
        deptService.update(dept);
        return  Result.success();
    }

}
