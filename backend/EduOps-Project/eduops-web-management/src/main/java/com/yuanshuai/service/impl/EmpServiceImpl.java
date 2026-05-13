package com.yuanshuai.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yuanshuai.mapper.EmpExpMapper;
import com.yuanshuai.mapper.EmpMapper;
import com.yuanshuai.pojo.*;
import com.yuanshuai.service.EmpLogService;
import com.yuanshuai.service.EmpService;
import com.yuanshuai.utils.CurrentHolder;
import com.yuanshuai.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class EmpServiceImpl implements EmpService {


    @Autowired
    private EmpMapper empMapper;
    @Autowired
    private EmpExpMapper empExpMapper;
    @Autowired
    private EmpLogService empLogService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();



    // 分页查询员工数据
    @Override
    public PageResult<Emp> page(EmpQueryParam queryParam) {
        // 设置分页参数（PageHelper），使用分页插件
        PageHelper.startPage(queryParam.getPage(), queryParam.getPageSize());
        // 执行查询
        List<Emp> empList = empMapper.list(queryParam);
        // 强制转换成Page对象，就可以使用Page类里面的各个方法，调用方法获取总数和当前页数据
        Page<Emp> pageObj = (Page<Emp>) empList;
        // 封装PageResult对象，返回给Controller
        return new PageResult<Emp>(pageObj.getTotal(), pageObj.getResult());
    }



    // 新增员工
    @Transactional(rollbackFor = Exception.class)  // 开启事务，异常回滚
    @Override
    public void save(Emp emp) {
        try {
            // 1新增员工基本信息
            emp.setCreateTime(LocalDateTime.now());
            emp.setUpdateTime(LocalDateTime.now());
            // 如果没有传递密码，设置默认密码为123456并加密
            if (emp.getPassword() == null || emp.getPassword().isEmpty()) {
                String defaultPassword = "123456";
                emp.setPassword(passwordEncoder.encode(defaultPassword));
                log.info("新增员工：{}，使用默认密码123456", emp.getUsername());
            } else {
                // 如果传递了密码，进行BCrypt加密
                emp.setPassword(passwordEncoder.encode(emp.getPassword()));
                log.info("新增员工：{}，使用自定义密码", emp.getUsername());
            }
            empMapper.insert(emp);
            // 2新增员工工作经历
            List<EmpExpr> exprList = emp.getExprList();
            // 判断是否有工作经历
            if (!CollectionUtils.isEmpty(exprList))  {
                exprList.forEach(expr -> expr.setEmpId(emp.getId()));
                empExpMapper.insertBatch(exprList);
            }
        }  finally {

            // 记录新增员工日志
            EmpLog empLog = new EmpLog(null, LocalDateTime.now(), "新增员工:" + emp);
            empLogService.insertLog(empLog);
        }


    }


    // 根据ID批量删除员工
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(List<Integer> ids) {
        // 1，批量删除员工的基本信息
        empMapper.deleteByIds(ids);
        // 2. 根据传递过来的员工id,删除对应员工的工作经历
        empExpMapper.deleteByEmpIds(ids);
    }


    // 根据传递过来的empId, 回显员工信息和工作经历
    @Override
    public Emp getInfo(Integer id) {
        return empMapper.getById(id);
    }

    // 修改员工的信息和工作经历
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(Emp emp) {
        // 1. 修改员工基本信息
        emp.setUpdateTime(LocalDateTime.now());
        empMapper.update(emp);
        // 2. 修改员工工作经历
        empExpMapper.deleteByEmpIds(Arrays.asList(emp.getId()));
        if (!CollectionUtils.isEmpty(emp.getExprList())) {
            emp.getExprList().forEach(expr -> expr.setEmpId(emp.getId()));
            empExpMapper.insertBatch(emp.getExprList());
        }
    }



    @Override
    public List<Emp> findAll() {
        return empMapper.findAll();
    }



    // ... existing code ...

    @Override
    public LoginInfo login(Emp emp) {
        // 1. 根据用户名查询员工信息
        Emp e = empMapper.getByUsername(emp.getUsername());

        // 2. 判断用户是否存在
        if (e == null) {
            log.warn("登录失败：用户不存在，用户名：{}", emp.getUsername());
            return null;
        }

        // 3. 验证密码（支持明文和 BCrypt 加密密码）
        boolean passwordMatches = false;
        if (e.getPassword() != null && (e.getPassword().startsWith("$2a$") || e.getPassword().startsWith("$2b$"))) {
            // BCrypt 加密密码验证
            passwordMatches = passwordEncoder.matches(emp.getPassword(), e.getPassword());
        } else {
            // 明文密码验证（兼容旧数据）
            passwordMatches = emp.getPassword().equals(e.getPassword());
        }

        if (!passwordMatches) {
            log.warn("登录失败：密码错误，用户名：{}", emp.getUsername());
            return null;
        }

        // 4. 登录成功，生成 JWT 令牌
        log.info("登录成功：{}", e);
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", e.getId());
        claims.put("username", e.getUsername());
        String jwt = JwtUtils.generateToken(claims);
        return new LoginInfo(e.getId(), e.getUsername(), e.getName(), jwt);
    }

// ... existing code ...


    /**
     * 修改员工密码
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     */
    @Override
    public void updatePassword(String oldPassword, String newPassword) {
        // 1. 获取当前登录员工的ID
        Integer id = CurrentHolder.getCurrentId();
        if (id == null) {
            throw new com.yuanshuai.exception.BusinessException("用户未登录");
        }

        // 2. 根据ID查询员工信息
        Emp emp = empMapper.getById(id);
        if (emp == null) {
            throw new com.yuanshuai.exception.BusinessException("用户不存在");
        }

        // 3. 验证旧密码是否正确（支持明文和BCrypt加密密码）
        boolean passwordMatches = false;
        if (emp.getPassword().startsWith("$2a$") || emp.getPassword().startsWith("$2b$")) {
            // 如果数据库中是BCrypt加密的密码
            passwordMatches = passwordEncoder.matches(oldPassword, emp.getPassword());
        } else {
            // 如果数据库中是明文密码（兼容旧数据）
            passwordMatches = oldPassword.equals(emp.getPassword());
        }

        if (!passwordMatches) {
            log.warn("用户ID: {} 修改密码失败，原密码错误", id);
            throw new com.yuanshuai.exception.BusinessException("原密码错误");
        }

        // 4. 对新密码进行BCrypt加密
        String encodedPassword = passwordEncoder.encode(newPassword);

        // 5. 调用mapper接口更新密码
        empMapper.updatePassword(id, encodedPassword);
        log.info("用户ID: {} 密码修改成功", id);
    }



}
