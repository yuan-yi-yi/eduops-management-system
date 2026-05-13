package com.yuanshuai.service;

import com.yuanshuai.pojo.Emp;
import com.yuanshuai.pojo.EmpQueryParam;
import com.yuanshuai.pojo.LoginInfo;
import com.yuanshuai.pojo.PageResult;

import java.util.List;

public interface EmpService {
//    PageResult<Emp> page(Integer page, Integer pageSize, String name, Integer gender, LocalDate begin, LocalDate end);

    PageResult<Emp> page(EmpQueryParam queryParam);

    void save(Emp emp);

    void delete(List<Integer> ids);

    Emp getInfo(Integer id);

    void update(Emp emp);

    /**
     * 查询全部员工信息
     * @return
     */
    List<Emp> findAll();

    /**
     * 员工登录
     * @param emp
     * @return
     */
    LoginInfo login(Emp emp);

    /**
     * 修改员工密码
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     */
    void updatePassword(String oldPassword, String newPassword);

}
