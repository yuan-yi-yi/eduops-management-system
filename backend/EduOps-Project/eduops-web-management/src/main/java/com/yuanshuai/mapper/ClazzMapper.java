package com.yuanshuai.mapper;

import com.yuanshuai.pojo.Clazz;
import com.yuanshuai.pojo.ClazzQueryParam;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface ClazzMapper {


    List<Clazz> list(ClazzQueryParam clazzQueryParam);

    /**
     * 新增班级
     * @param clazz
     */
    @Insert("insert into clazz(name, room, begin_date, end_date, master_id, subject, create_time, update_time)\n" +
            "values (#{name}, #{room}, #{beginDate}, #{endDate}, #{masterId}, #{subject}, #{createTime}, #{updateTime})")
    void insert(Clazz clazz);

    /**
     * 根据主键ID查询班级的信息
     * @param id
     * @return
     */
    @Select("select id, name, room, begin_date, end_date, master_id, subject, create_time, update_time\n" +
            "from clazz where id = #{id}")
    Clazz getById(Integer id);


    /**
     * 修改班级的数据信息
     * @param clazz
     */
    @Update("update clazz " +
            "set name=#{name}, room=#{room}, begin_date=#{beginDate}, end_date=#{endDate}, " +
            "master_id=#{masterId}, subject=#{subject}, update_time=#{updateTime} " +
            "where id = #{id}")
    void update(Clazz clazz);

    /**
     * 删除班级信息
     * @param id
     */
    @Delete("delete from clazz where id = #{id}")
    void delete(Integer id);

    /**
     * 根据班级ID查询关联的学生数量
     * @param clazzId
     * @return
     */
    @Select("select count(*) from student where clazz_id = #{clazzId}")
    int countStudentByClazzId(Integer clazzId);


    /**
     * 查询所有班级信息
     *
     * @return
     */
    @Select("select c.id, c.name, c.room, c.begin_date, c.end_date, c.master_id, " +
            "c.subject, c.create_time, c.update_time " +
            "from clazz c")
    List<Clazz> lists();



    /**
     * 班级人数统计
     *
     * @return
     */
    @MapKey("")
    List<Map<String, Object>> getStudentCountData();


    // 统计
    @Select("select count(*) from clazz")
    Integer count();


}