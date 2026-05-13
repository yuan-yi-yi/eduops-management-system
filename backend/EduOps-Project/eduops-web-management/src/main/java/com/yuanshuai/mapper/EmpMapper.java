package com.yuanshuai.mapper;

import com.yuanshuai.pojo.Emp;
import com.yuanshuai.pojo.EmpQueryParam;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

// 员工 Mapper 接口
@Mapper
public interface EmpMapper {

    public List<Emp> list(EmpQueryParam queryParam);

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into emp (username, password, name, gender, phone, job, salary, image, entry_date, dept_id, create_time, update_time) " +
            "values (#{username}, #{password}, #{name}, #{gender}, #{phone}, #{job}, #{salary}, #{image}, #{entryDate}, #{deptId}, #{createTime}, #{updateTime})")
    void insert(Emp emp);

    void deleteByIds(List<Integer> ids);

    Emp getById(Integer id);

    void update(Emp emp);

    @MapKey("pos")
    List<Map<String, Object>> countEmpJobData();

    @MapKey("name")
    List<Map<String, Object>> countEmpGenderData();

    @Select("select id, username, password, name, gender, phone, job, salary, image, entry_date, dept_id, create_time, update_time\n" +
            "from emp")
    List<Emp> findAll();

    /**
     * 根据用户名查询员工信息（包含密码，用于登录验证）
     *
     * @param username 用户名
     * @return
     */
    @Select("select id, username, password, name, gender, phone, job, salary, image, entry_date, dept_id, create_time, update_time " +
            "from emp where username = #{username}")
    Emp getByUsername(@Param("username") String username);

    /**
     * 修改员工密码
     * @param id 员工 ID
     * @param newPassword 新密码
     */
    @Update("update emp set password = #{newPassword}, update_time = now() where id = #{id}")
    void updatePassword(@Param("id") Integer id, @Param("newPassword") String newPassword);


    // 统计
    @Select("select count(*) from emp")
    Integer count();


}