package com.yuanshuai.mapper;

import com.yuanshuai.pojo.Student;
import com.yuanshuai.pojo.StudentQueryParam;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface StudentMapper {
    /**
     * 条件分页查询学员
     * @param studentQueryParam
     * @return
     */
    List<Student> list(StudentQueryParam studentQueryParam);

    /**
     * 添加学员信息
     *
     * @param student
     */
    @Insert("insert into " +
            "student(name, no, gender, phone, id_card, is_college, address, degree, graduation_date, clazz_id, create_time, update_time) " +
            "values (#{name}, #{no}, #{gender}, #{phone}, #{idCard}, #{isCollege}, #{address}, #{degree}, #{graduationDate}, #{clazzId}, #{createTime}, #{updateTime})")
    void insert(Student student);

    /**
     * 根据ID查询学生
     *
     * @param id
     * @return
     */
    @Select("select id, name, no, gender, phone, id_card, is_college, address, degree, " +
            "graduation_date, clazz_id, violation_count, violation_score, create_time, update_time " +
            "from student where id = #{id}")
    Student getById(Integer id);

    /**
     * 修改学员的数据信息
     *
     * @param student
     */
    @Update("update student " +
            "set name=#{name}, no=#{no}, gender=#{gender}, phone=#{phone}, id_card=#{idCard}, is_college=#{isCollege}, " +
            "address=#{address}, degree=#{degree}, graduation_date=#{graduationDate}, clazz_id=#{clazzId}, " +
            "violation_count=#{violationCount}, violation_score=#{violationScore}, update_time=#{updateTime} " +
            "where id = #{id}")
    void update(Student student);

    /**
     * 批量删除学生信息
     *
     * @param ids
     */
    void deleteByIds(List<Integer> ids);


    /**
     * 学员学历统计
     *
     * @return
     */
    @MapKey("")
    List<Map<String, Object>> getStudentDegreeData();

    // 统计
    @Select("select count(*) from student")
    Integer count();

}