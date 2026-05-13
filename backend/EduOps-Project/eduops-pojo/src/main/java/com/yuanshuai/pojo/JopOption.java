package com.yuanshuai.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JopOption {
    // 职位列表
    private List jobList;
    // 职位对应的人数有多少
    private List dataList;
}
