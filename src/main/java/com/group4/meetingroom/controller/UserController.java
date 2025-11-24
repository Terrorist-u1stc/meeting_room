package com.group4.meetingroom.controller;
import com.group4.meetingroom.entity.User;
import com.group4.meetingroom.entity.vo.MessageModel;
import com.group4.meetingroom.service.UserService;
import com.group4.meetingroom.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UserController {
//    //用户登录
@PostMapping("/login")
@ResponseBody
public MessageModel<Map<String, Object>> userLogin(
        @RequestParam String username,
        @RequestParam String password) {

    MessageModel<User> result = userService.userLogin(username, password);
    MessageModel<Map<String, Object>> m = new MessageModel<>();

    if (result.getStatus() == HttpStatus.OK.value()) {
        User user = result.getData();
        String token = JwtUtils.generateToken(user.getId());

        Map<String, Object> data = new HashMap<>();
        data.put("user", user);
        data.put("token", token);
        m.setData(data);
        m.setStatus(HttpStatus.OK.value());
        m.setMsg("登录成功");
        return  m;
    }
    m.setData(null);
    m.setMsg(result.getMsg());
    m.setStatus(result.getStatus());
    return m;
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
    @CrossOrigin(origins = "*")
    @GetMapping("/test")
    @ResponseBody
    //展示用户信息
    public String getUserInfo(@RequestParam(required = false) String name) {
        return name != null ? "Hello, " + name : "Hello, Guest";
    }
    public
    @Autowired
    UserService userService;

}
