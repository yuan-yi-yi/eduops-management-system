package com.yuanshuai.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
// 分页接口封装类，作为Result的data属性，转给前端
public class PageResult<T> {
    private Long total;
    private List<T> rows;
}
