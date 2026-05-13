package com.yuanshuai.pojo;

import lombok.Data;

@Data
public class IndexStats {
    private Integer studentCount;
    private Integer clazzCount;
    private Integer empCount;
    private Integer deptCount;
    private String empImage;       // 员工头像URL
    private String empName;        // 员工姓名
}