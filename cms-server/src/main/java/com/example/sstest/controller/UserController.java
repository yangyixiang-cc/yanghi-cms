package com.example.sstest.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.sstest.bean.User;
import com.example.sstest.service.CaptchaService;
import com.example.sstest.service.UserService;
import com.example.sstest.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private CaptchaService captchaService;

    @PostMapping("/login")
    public Result login(
            User user
    ) {
        return userService.login(user);
    }

    @GetMapping("/verify_code")
    public Result verifyCode(@RequestParam("token") String token,
                        @RequestParam("inputCode") String inputCode) {
        return captchaService.versifyCaptcha(token, inputCode);
    }


    @GetMapping("/captcha")
    public Result captcha() throws IOException {
        return captchaService.captchaCreator();
    }

    @GetMapping("/hello")
    @PreAuthorize("hasAuthority('system:dept:list')") //判断访问者是否有test权限,权限就是一个个名称，如 admin 管理者 user 普通用户等
    public String hello() {
        return "hello";
    }

    @GetMapping("/logout")
    public Result logout() {
        return userService.logout();
    }

    @GetMapping("/alluser")
    public Result getAllUser() {
        return userService.getAllUser();
    }

    @DeleteMapping("/{id}")
    public Result deleteUserOneById(@PathVariable("id") Long id) {
        return userService.deleteUserOneById(id);
    }


    @GetMapping("/permission/{userId}")
    public Result getUserMenuPermission(@PathVariable("userId") Long userId) {
        return userService.selectUserPermissionByUserId(userId);
    }

    @PostMapping("/add")
    public Result addUserOne(User user) {
        return userService.addUser(user);
    }

    @PutMapping("/update")
    public Result updateUserOne(User user) {
        return userService.updateUserOne(user);
    }

    @PostMapping("/betch_delete")
    public Result betchDelterUser(@RequestParam("ids[]") Long[] ids) {
        List<Long> longs = Arrays.asList(ids);
        return userService.betchDeleteUser(longs);
    }

    @GetMapping("/page")
    public Result getUserPage(@RequestParam("current") int current, @RequestParam("pageSize") int pageSize) {
        return userService.getUserPage(current, pageSize);
    }

    @GetMapping("/validate_password")
    public Result returnValidatePassword(@RequestParam("password") String password, @RequestParam("encodePassword") String encodePassword) {
        return userService.validatePassword(password, encodePassword);
    }

    @GetMapping("/return_encode_password")
    public Result getPasswordById(@RequestParam("userId") Long userId) {
        return userService.getPasswordById(userId);
    }


    @PostMapping("/return_search_page")
    public Result<Page<User>> getUserSearchPage(@RequestParam("current") int current, @RequestParam("pageSize") int pageSize,
                                                @RequestParam("nickName") String nickName,
                                                @RequestParam("sex") String sex,
                                                @RequestParam("status") String status,
                                                @RequestParam("email") String email,
                                                @RequestParam("phonenumber") String phone,
                                                @RequestParam("beginTime") String beginTime,
                                                @RequestParam("lastTime") String lastTime) {
        return userService.getUserSearchPage(current, pageSize,
                nickName,
                sex,
                status,
                email,
                phone,
                beginTime,
                lastTime);
    }

}
