package com.yuanshuai.service.impl;

import com.yuanshuai.mapper.ClazzMapper;
import com.yuanshuai.mapper.EmpMapper;
import com.yuanshuai.mapper.StudentMapper;
import com.yuanshuai.pojo.ClazzOption;
import com.yuanshuai.pojo.JopOption;
import com.yuanshuai.service.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ReportServiceImpl implements ReportService {


    @Autowired
    private EmpMapper empMapper;
    @Autowired
    private ClazzMapper clazzMapper;
    @Autowired
    private StudentMapper studentMapper;


    @Override
    public JopOption getEmpJobData() {
        // 1. 调用mapper接口，获取统计数据
        List<Map<String, Object>> list = empMapper.countEmpJobData(); // map: pos=教研主管, num=1
        // 2. 组装结果，并返回
        List jobList = list.stream().map(dataMap -> dataMap.get("pos")).toList();
        List dataList = list.stream().map(dataMap -> dataMap.get("num")).toList();
        JopOption jopOption = new JopOption(jobList, dataList);
        log.info(jopOption.toString());
        return jopOption;
    }

    @Override
    public List<Map<String, Object>> getEmpGenderData() {
        return empMapper.countEmpGenderData();
    }

    @Override
    public ClazzOption getStudentCountData() {
        // 1. 调用Mapper接口获取统计数据
        List<Map<String, Object>> list = clazzMapper.getStudentCountData();

        // 2. 组装结果并返回
        List<Object> clazzList = list.stream().map(dataMap -> dataMap.get("clazz_name")).toList();
        List<Object> dataList = list.stream().map(dataMap -> dataMap.get("num")).toList();

        return new ClazzOption(clazzList, dataList);
    }

    @Override
    public List<Map<String, Object>> getStudentDegreeData() {
        List<Map<String, Object>> list = studentMapper.getStudentDegreeData();
        return list;
    }



}