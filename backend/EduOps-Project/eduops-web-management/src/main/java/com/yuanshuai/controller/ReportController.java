package com.yuanshuai.controller;

import com.yuanshuai.pojo.ClazzOption;
import com.yuanshuai.pojo.JopOption;
import com.yuanshuai.pojo.Result;
import com.yuanshuai.service.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
@Slf4j
@RestController
@RequestMapping("/report")
public class ReportController {
    @Autowired
    private ReportService reportService;


    /**
     * 统计各个职位的员工人数
     */
    @GetMapping("/empJobData")
    public Result getEmpJobData(){

        JopOption jobOption = reportService.getEmpJobData();
        return Result.success(jobOption);
    }

    @GetMapping("/empGenderData")
    public Result getEmpGenderData(){
        List<Map<String, Object>> genderList = reportService.getEmpGenderData();
        return Result.success(genderList);
    }

    /**
     * 班级人数统计
     *
     * @return
     */
    @GetMapping("/studentCountData")
    public Result getStudentCountData() {
        log.info("班级人数统计");
        ClazzOption clazzOption = reportService.getStudentCountData();
        return Result.success(clazzOption);
    }

    /**
     * 学员学历统计
     *
     * @return
     */
    @GetMapping("/studentDegreeData")
    public Result getStudentDegreeData() {
        log.info("学员学历统计");
        List<Map<String, Object>> degreeList = reportService.getStudentDegreeData();
        return Result.success(degreeList);
    }



}
