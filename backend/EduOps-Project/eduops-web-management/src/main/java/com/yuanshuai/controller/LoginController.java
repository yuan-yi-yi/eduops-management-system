package com.yuanshuai.controller;

import com.yuanshuai.pojo.Emp;
import com.yuanshuai.pojo.LoginInfo;
import com.yuanshuai.pojo.Result;
import com.yuanshuai.pojo.UpdatePasswordParam;
import com.yuanshuai.service.EmpService;
import com.yuanshuai.utils.CurrentHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 登录Controller
 */
@Slf4j
@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private EmpService empService;

    /**
     * 登录
     *
     * @return
     */
    @PostMapping()
    public Result login(@RequestBody Emp emp) {
        log.info("登录: {}", emp);
        LoginInfo loginInfo = empService.login(emp);
        if(loginInfo != null) {
            return Result.success(loginInfo);
        }else {
            return Result.error("用户名或密码错误!");
        }
    }

    /**
     * 修改密码
     *
     * @param updatePasswordParam 修改密码参数
     * @return
     */
    @PutMapping("/password")
    public Result updatePassword(@Validated @RequestBody UpdatePasswordParam updatePasswordParam) {
        log.info("修改密码，用户ID: {}", CurrentHolder.getCurrentId());
        empService.updatePassword(updatePasswordParam.getOldPassword(), updatePasswordParam.getNewPassword());
        return Result.success("密码修改成功");
    }
}
