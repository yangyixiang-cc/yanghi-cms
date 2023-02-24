package com.example.sstest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.sstest.bean.LoginUser;
import com.example.sstest.bean.Menu;
import com.example.sstest.bean.User;
import com.example.sstest.mapper.MenuMapper;
import com.example.sstest.mapper.UserMapper;
import com.example.sstest.service.UserService;
import com.example.sstest.utils.Constant;
import com.example.sstest.utils.JwtUtil;
import com.example.sstest.utils.RedisCache;
import com.example.sstest.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private MenuMapper menuMapper;


    @Override
    public User getUserOneByUsername(String username) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("user_name",username);
        return this.getOne(userQueryWrapper);
    }

    @Override
    public boolean addUserOne(String username,String password) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        User user = new User();
        user.setUserName(username);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        return this.save(user);
    }

    @Override
    public Result login(User user) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if(Objects.isNull(authenticate)){
            throw new RuntimeException("登录失败");
        }
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String status = loginUser.getUser().getStatus();
        if(status.equals(Constant.USER_DEACTIVATE)){
            return Result.failed("用户已经停用，请联系管理员！");
        }
        String id = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(id);
        redisCache.setCacheObject("login:"+id,loginUser);
        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.put("token",jwt);
        stringObjectHashMap.put("user",loginUser.getUser());
        stringObjectHashMap.put("permissions", loginUser.getPermissions());
        stringObjectHashMap.put("roles", loginUser.getRoles());
        return Result.ok(stringObjectHashMap, "登录成功");
    }

    @Override
    public Result logout() {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken)SecurityContextHolder.getContext().getAuthentication();
        LoginUser principal = (LoginUser) usernamePasswordAuthenticationToken.getPrincipal();
        Long id = principal.getUser().getId();
        redisCache.deleteObject("login:"+id);
        return Result.ok("注销成功");
    }

    @Override
    public Result<List<User>> getAllUser() {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        List<User> list = this.list(userQueryWrapper.select("id", "user_name", "nick_name", "email", "phonenumber", "sex", "avatar", "status", "create_time", "update_time"));
        return Result.ok(list, "获取全部用户成功");
    }



    @Override
    public Result<Page<User>> getUserPage(int current, int pageSize) {
        Page<User> userPage = new Page<>(current, pageSize);
        Page<User> page = this.page(userPage);
        return Result.ok(page);
    }

    @Override
    public Result<Page<User>> getUserSearchPage(int current, int pageSize,
                                                String nickName,
                                                String sex,
                                                String status,
                                                String email,
                                                String phone,
                                                String beginTime,
                                                String lastTime) {
        boolean isTime = true;
        if("".equals(beginTime) && "".equals(lastTime)){
            isTime = false;
        }
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper
                .like(!"".equals(nickName),"nick_name", nickName)
                .like(!"".equals(sex),"sex",sex)
                .like(!"".equals(status),"status", status)
            .like(!"".equals(email),"email", email)
                .like(!"".equals(phone),"phonenumber", phone)
                .between(isTime,"create_time",beginTime, lastTime);
        Page<User> userPage = new Page<>(current, pageSize);
        Page<User> page = this.page(userPage,userQueryWrapper);
        return Result.ok(page);
    }

    @Override
    public Result<List<Menu>>  selectUserPermissionByUserId(Long id) {
        List<Menu> menus = menuMapper.selectUserPermissionByUserId(id);

        return Result.ok(menus);
    }

    @Override
    public Result deleteUserOneById(Long id) {
        boolean b = this.removeById(id);
        if(!b){
            return Result.failed("数据删除失败");
        }
        return Result.ok("数据删除成功");
    }
    @Override
    public Result addUser(User user) {
        String userName = user.getUserName();
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("user_name", userName);
        List<User> list = this.list(userQueryWrapper);
        if(list.size() > 0){
            return Result.failed("用户名已存在");
        }
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        boolean save = this.save(user);
        if(save){
            return Result.ok("添加用户成功");
        }
        return Result.failed("添加用户失败");
    }

    @Override
    public Result updateUserOne(User user) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        if(!Objects.isNull(user.getPassword())){
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        }
        boolean b = this.updateById(user);
        if(b){
            return Result.ok("更新用户成功");
        }
        return Result.failed("更新用户失败");
    }

    @Override
    public Result betchDeleteUser(List<Long> ids) {
        boolean b = this.removeBatchByIds(ids);
        if(b){
            return Result.ok("批量删除用户成功");
        }
        return Result.failed("批量删除用户失败");
    }

    @Override
    public Result validatePassword(String password, String encodePassword) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        boolean matches = bCryptPasswordEncoder.matches(password, encodePassword);
        if(matches){
            return Result.ok(matches, "密码正确");
        }else{
            return Result.failed(matches, "密码不正确");
        }
    }

    @Override
    public Result getPasswordById(Long userId) {
        User byId = this.getById(userId);
        return Result.ok(byId.getPassword(),"");
    }

}
