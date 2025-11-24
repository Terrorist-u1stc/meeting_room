package com.group4.meetingroom.controller;
import com.group4.meetingroom.entity.CustomUserDetails;
import com.group4.meetingroom.entity.User;
import com.group4.meetingroom.entity.vo.MessageModel;
import com.group4.meetingroom.service.UserService;
import com.group4.meetingroom.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
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
        String token = JwtUtils.generateToken(user.getId(), user.getTokenVersion());

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
    @PostMapping("/logout")
    @ResponseBody
    public MessageModel<Void> logout(){
        var auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Logout Authentication = " + auth);
        if (auth == null) {
            MessageModel<Void> r= new MessageModel<>();
            r.setStatus(402);
            r.setMsg("wrong");
            return r;
        }
        User user = ((CustomUserDetails) auth.getPrincipal()).getUser();
        return userService.logout(user.getId());
    }
    @CrossOrigin(origins = "*")
    @PostMapping("/test")
    @ResponseBody
    public Map<String, Object> test(Authentication auth) {
        System.out.println("Authentication = " + auth);
        return Map.of(
                "userId", auth != null ? auth.getName() : null,
                "authorities", auth != null ? auth.getAuthorities() : null
        );
    }

    public
    @Autowired
    UserService userService;

}
