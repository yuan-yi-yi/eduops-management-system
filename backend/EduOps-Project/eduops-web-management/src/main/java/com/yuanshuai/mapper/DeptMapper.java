package com.yuanshuai.mapper;

import com.yuanshuai.pojo.Dept;
import org.apache.ibatis.annotations.*;

import java.util.List;

// 部门Mapper接口
@Mapper
public interface DeptMapper {
    @Select("select id,name,create_time,update_time from dept order by update_time desc")
    List<Dept> findAll();
    @Delete("delete from dept where id = #{id}")
    void deleteById(Integer id);
    @Insert("insert into dept(name,create_time,update_time) values(#{name}, #{createTime}, #{updateTime})")
    void insert(Dept dept);
    @Select("select id,name,create_time,update_time from dept where id = #{id}")
    Dept getById(Integer id);
    @Update("update dept set name = #{name}, update_time = #{updateTime} where id = #{id}")
    void update(Dept dept);

    /**
     * 根据ID查找部门下的员工数量
     *
     * @param id
     * @return
     */
    @Select("select count(*) from emp where dept_id = #{id}")
    int countEmpByDeptId(Integer id);

    // 统计
    @Select("select count(*) from dept")
    Integer count();

}