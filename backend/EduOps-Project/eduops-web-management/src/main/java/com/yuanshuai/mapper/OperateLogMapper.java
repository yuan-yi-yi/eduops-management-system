package com.yuanshuai.mapper;

import com.yuanshuai.pojo.OperateLog;
import com.yuanshuai.pojo.OperateLogQueryParam;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OperateLogMapper {

    //插入日志数据
    @Insert("insert into operate_log (operate_emp_id, operate_time, class_name, method_name, method_params, return_value, cost_time) " +
            "values (#{operateEmpId}, #{operateTime}, #{className}, #{methodName}, #{methodParams}, #{returnValue}, #{costTime});")
    public void insert(OperateLog log);

    //查询所有日志数据
    @Select("select ol.id, operate_emp_id, operate_time, class_name, method_name, method_params, return_value, cost_time, e.name operate_emp_name " +
            "from operate_log ol left join emp e on ol.operate_emp_id = e.id order by ol.operate_time desc")
    List<OperateLog> page(OperateLogQueryParam operateLogQueryParam);

}
