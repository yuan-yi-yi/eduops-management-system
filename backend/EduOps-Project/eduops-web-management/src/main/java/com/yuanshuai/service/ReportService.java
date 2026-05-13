package com.yuanshuai.service;

import com.yuanshuai.pojo.ClazzOption;
import com.yuanshuai.pojo.JopOption;

import java.util.List;
import java.util.Map;

public interface ReportService {
    JopOption getEmpJobData();

    List<Map<String, Object>> getEmpGenderData();

    ClazzOption getStudentCountData();

    /**
     * 学员学历统计
     *
     * @return
     */
    List<Map<String, Object>> getStudentDegreeData();
}
