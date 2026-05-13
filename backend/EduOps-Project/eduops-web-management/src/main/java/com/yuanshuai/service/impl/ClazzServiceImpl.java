package com.yuanshuai.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yuanshuai.exception.BusinessException;
import com.yuanshuai.mapper.ClazzMapper;
import com.yuanshuai.pojo.Clazz;
import com.yuanshuai.pojo.ClazzQueryParam;
import com.yuanshuai.pojo.PageResult;
import com.yuanshuai.service.ClazzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClazzServiceImpl implements ClazzService {

    @Autowired
    private ClazzMapper clazzMapper;

    /**
     * 分页查询全部班级数据
     * @param clazzQueryParam
     * @return
     */
    @Override
    public PageResult<Clazz> page(ClazzQueryParam clazzQueryParam) {
        // return clazzMapper.list(clazzQueryParam);
        // 在Service层使用PageHelper分页查询
        // 1. 设置分页参数
        PageHelper.startPage(clazzQueryParam.getPage(), clazzQueryParam.getPageSize());
        // 2. 执行查询
        List<Clazz> clazzList = clazzMapper.list(clazzQueryParam);
        // 3. 解析查询结果并封装
        Page<Clazz> p = (Page<Clazz>) clazzList;
        return new PageResult<Clazz>(p.getTotal(), p.getResult());
    }


    @Override
    public void insert(Clazz clazz) {
        clazz.setCreateTime(LocalDateTime.now());
        clazz.setUpdateTime(LocalDateTime.now());
        clazzMapper.insert(clazz);
    }


    @Override
    public Clazz getById(Integer id) {
        return clazzMapper.getById(id);
    }


    @Override
    public void update(Clazz clazz) {
        clazz.setUpdateTime(LocalDateTime.now());
        clazzMapper.update(clazz);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(Integer id) {
        // 1. 校验该班级下是否有关联的学生
        int studentCount = clazzMapper.countStudentByClazzId(id);
        if(studentCount>0){
            throw new BusinessException("对不起, 该班级下有学生, 不能直接删除!");
        }
        // 2. 校验通过，执行删除操作
        clazzMapper.delete(id);
    }

    @Override
    public List<Clazz> list() {
        return clazzMapper.lists();
    }



}