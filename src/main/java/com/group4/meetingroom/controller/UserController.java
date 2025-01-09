package com.group4.meetingroom.controller;

import com.group4.meetingroom.entity.User;
import com.group4.meetingroom.entity.vo.MessageModel;
import com.group4.meetingroom.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {
    //用户登录
    @PostMapping("/login")
    @ResponseBody
    public MessageModel<User> userLogin(@RequestParam String userName, @RequestParam String passWord, HttpSession session){
       MessageModel<User> result = userService.userLogin(userName, passWord);
       if(result.getStatus() == HttpStatus.OK.value()){
           session.setAttribute("currentUser", result.getData());
       }
       return result;
    }
    //用户注册
    @PostMapping("/register")
    @ResponseBody
    public MessageModel userRegister(@RequestBody User user){
        return userService.userRegister(user);
    }
    //删除用户
    @DeleteMapping("/deleteUser")
    @ResponseBody
    public MessageModel userDelete(@RequestParam int id){
        return userService.userDelete(id);
    }
    //展示用户信息

    public
    @Autowired
    UserService userService;
}
