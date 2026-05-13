package com.yuanshuai.controller;

import com.yuanshuai.anno.Log;
import com.yuanshuai.pojo.Emp;
import com.yuanshuai.pojo.EmpQueryParam;
import com.yuanshuai.pojo.PageResult;
import com.yuanshuai.pojo.Result;
import com.yuanshuai.service.EmpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

// 员工管理的Controller

@Slf4j
@RestController
@RequestMapping("/emps")
public class EmpController {
    // 注入Service
    @Autowired
    EmpService empService;



    // 分页查询员工数据
    @GetMapping
    public Result page(EmpQueryParam queryParam) {
        log.info("分页查询员工数据，queryParam：{}", queryParam);
        PageResult<Emp> pageResult = empService.page(queryParam);
         return Result.success(pageResult);
    }


    // 新增员工
    @Log
    @PostMapping
    public Result save(@RequestBody Emp emp) {
        log.info("新增员工，emp：{}", emp);
        // 新增员工
        empService.save(emp);
        return Result.success();
    }

    // 根据ID批量删除员工
    @Log
    @DeleteMapping
    public Result delete(@RequestParam List<Integer> ids) {
        log.info("根据ID批量删除员工，ids：{}", ids);
        empService.delete(ids);
        return Result.success();
    }

    // 根据传递过来的empId, 回显员工信息和工作经历
    @GetMapping("/{id}")
    public Result getInfo(@PathVariable Integer id) {
        log.info("根据传递过来的empId, 回显员工信息和工作经历，id：{}", id);
        Emp emp = empService.getInfo(id);
        return Result.success(emp);
    }

    // 修改员工的信息和工作经历
    @Log
    @PutMapping
    public Result update(@RequestBody Emp emp) {
        log.info("修改员工的信息和工作经历，emp：{}", emp);
        empService.update(emp);
        return Result.success();
    }



    /**
     * 查询全部员工信息
     * @return
     */
    @GetMapping("/list")
    public Result list(){
        log.info("查询全部员工信息");
        List<Emp> empList = empService.findAll();
        return Result.success(empList);
    }

    /**
     * 修改员工密码
     * @param param 包含旧密码和新密码
     * @return
     */
    @Log
    @PutMapping("/password")
    public Result updatePassword(@RequestBody Map<String, String> param) {
        log.info("修改员工密码");
        String oldPassword = param.get("oldPassword");
        String newPassword = param.get("newPassword");
        empService.updatePassword(oldPassword, newPassword);
        return Result.success();
    }

}