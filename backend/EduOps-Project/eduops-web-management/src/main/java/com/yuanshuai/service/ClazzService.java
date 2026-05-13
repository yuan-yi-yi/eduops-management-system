package com.yuanshuai.service;

import com.yuanshuai.pojo.Clazz;
import com.yuanshuai.pojo.ClazzQueryParam;
import com.yuanshuai.pojo.PageResult;

import java.util.List;

public interface ClazzService {
    PageResult<Clazz> page(ClazzQueryParam clazzQueryParam);

    /**
     * 新增班级
     * @param clazz
     */
    void insert(Clazz clazz);

    /**
     * 根据主键ID查询班级的信息
     * @param id
     * @return
     */
    Clazz getById(Integer id);

    /**
     * 修改班级的数据信息
     * @param clazz
     */
    void update(Clazz clazz);

    /**
     * 删除班级信息
     * @param id
     */
    void delete(Integer id);

    /**
     * 查询所有班级信息
     *
     * @return
     */
    List<Clazz> list();

}