package com.yuanshuai.controller;

import com.yuanshuai.anno.Log;
import com.yuanshuai.pojo.Clazz;
import com.yuanshuai.pojo.ClazzQueryParam;
import com.yuanshuai.pojo.PageResult;
import com.yuanshuai.pojo.Result;
import com.yuanshuai.service.ClazzService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/clazzs")
public class ClazzController {

    @Autowired
    private ClazzService clazzService;

    @GetMapping
    public Result page(ClazzQueryParam clazzQueryParam){
        log.info("分页查询：{}", clazzQueryParam);
        PageResult<Clazz> clazzList = clazzService.page(clazzQueryParam);
        return Result.success(clazzList);
    }

    /**
     * 新增班级
     * @param clazz
     * @return
     */
    @Log
    @PostMapping
    public Result save(@RequestBody Clazz clazz) {
        log.info("新增班级：{}", clazz);
        clazzService.insert(clazz);
        return Result.success();
    }

    /**
     * 根据主键ID查询班级的信息
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result getInfo(@PathVariable Integer id) {
        log.info("根据主键ID查询班级的信息: {}", id);
        Clazz clazz = clazzService.getById(id);
        return Result.success(clazz);
    }

    /**
     * 修改班级的数据信息
     * @param clazz
     * @return
     */
    @Log
    @PutMapping
    public Result update(@RequestBody Clazz clazz){
        log.info("修改班级的数据信息: {}",clazz);
        clazzService.update(clazz);
        return Result.success();
    }


    /**
     * 删除班级信息
     * @param id
     * @return
     */
    @Log
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id){
        log.info("删除班级信息: {}",id);
        clazzService.delete(id);
        return Result.success();
    }


    /**
     * 查询所有班级信息
     *
     * @return
     */
    @GetMapping("/list")
    public Result list() {
        log.info("查询所有班级信息");
        List<Clazz> clazzList = clazzService.list();
        return Result.success(clazzList);
    }



}