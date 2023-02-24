package com.example.sstest.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.sstest.bean.Menu;
import com.example.sstest.bean.User;
import com.example.sstest.utils.Result;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

public interface UserService extends IService<User> {

    User getUserOneByUsername(String username);


    boolean addUserOne(String username, String password);

    Result login(User user);

    Result logout();

    Result<List<User>> getAllUser();

    Result deleteUserOneById(Long id);

    /**
     * 用户分页
     *
     * @param current  当前页
     * @param pageSize 每页条数
     * @return
     */
    Result<Page<User>> getUserPage(int current, int pageSize);

    /**
     * 用户分页 搜索
     *
     * @param current  当前页
     * @param pageSize 每页条数
     * @return
     */
    Result<Page<User>> getUserSearchPage(int current, int pageSize,
                                         String nickName,
                                         String sex,
                                         String status,
                                         String email,
                                         String phone,
                                         String beginTime,
                                         String lastTime);


    Result<List<Menu>> selectUserPermissionByUserId(Long id);


    Result addUser(User user);

    Result updateUserOne(User user);

    Result betchDeleteUser(List<Long> ids);

    Result validatePassword(String password, String encodePassword);

    Result getPasswordById(Long userId);
}
