package com.course.controller;

import com.course.model.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;


@RestController
@Api(value = "V1", tags = "用户管理系统")
@RequestMapping("/user")
public class UserManager {
    @Autowired
    private SqlSessionTemplate template;

    Logger log = LoggerFactory.getLogger(getClass());

    @ApiOperation(value = "登录接口", httpMethod = "POST")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public boolean login(HttpServletResponse response, @RequestBody User user) {
        int i = template.selectOne("login", user);
        Cookie cookie = new Cookie("login", "true");
        response.addCookie(cookie);
        if (i == 1) {
            log.info("登录的用户是：" + user.getUserName());
            return true;
        }

        return false;
    }

    @ApiOperation(value = "添加用户信息接口", httpMethod = "POST")
    @RequestMapping(value = "/addUser", method = RequestMethod.POST)
    public User addUserMessage(HttpServletRequest request,@RequestBody User user) {
        Cookie[] cookies = request.getCookies();
        if (Objects.isNull(cookies)) {
            log.info("cookies信息为空,验证不成功");
        }
        //遍历cookies信息
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("login") && cookie.getValue().equals("true")) {
                log.info("cookie信息验证成功");
                int result = template.insert("addusermessage",user);
                if (result > 0) {
                    log.info("添加用户信息成功,用户数量为:" + result);
                    return user;
                }
            }
        }
        return null;
    }

    @ApiOperation(value = "获取用户信息接口", httpMethod = "POST")
    @RequestMapping(value = "/getuserinfo", method = RequestMethod.POST)
    public List<User> getUserInfo(HttpServletRequest request, @RequestBody User user) {
        Cookie[] cookies = request.getCookies();
        if (Objects.isNull(cookies)) {
            log.info("cookies信息为空,验证不成功");
        }
        //遍历cookies信息
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("login") && cookie.getValue().equals("true")) {
                log.info("cookie信息验证成功");
                List<User> users = template.selectList("getuserinfo",user);
                log.info("获取的用户信息成功,用户数量为:" + users.size());
                return users;
            }
        }
        return null;
    }

    @ApiOperation(value = "更新或删除用户信息",httpMethod = "POST")
    @RequestMapping(value = "/updateuser",method = RequestMethod.POST)
    public int updateUser(HttpServletRequest request,@RequestBody User user){
        Cookie[] cookies = request.getCookies();
        if (Objects.isNull(cookies)) {
            log.info("cookies信息为空,验证不成功");
        }
        //遍历cookies信息
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("login") && cookie.getValue().equals("true")) {
                log.info("cookie信息验证成功");
                int result = template.update("updateuser",user);
                    log.info("更新用户信息成功,更新数量为:" + result);
            }
        }
        return 0;
    }
}
