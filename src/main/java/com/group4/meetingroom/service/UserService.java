package com.group4.meetingroom.service;

import com.group4.meetingroom.entity.User;
import com.group4.meetingroom.entity.vo.MessageModel;
import com.group4.meetingroom.mapper.UserMapper;
import com.group4.meetingroom.util.StringUtil;
import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class UserService {

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Autowired
    private UserMapper userMapper;
    //用户登录
//    public MessageModel<User> userLogin(String userName, String passWord){
//
//        MessageModel<User> messageModel1 = new MessageModel<>();
//        User u = new User();
//        u.setUserName(userName);
//        u.setPassword(passWord);
//        messageModel1.setData(u);
//
//        if (StringUtil.isEmpty(userName)||StringUtil.isEmpty(passWord)){
//            messageModel1.setStatus(0);
//            messageModel1.setMsg("用户名与密码不能为空!");
//            return messageModel1;
//        }
//        User user = userMapper.selectByName(userName);
//        if (user == null){
//            messageModel1.setStatus(0);
//            messageModel1.setMsg("该用户不存在");
//            return messageModel1;
//        }
//        if (!passWord.equals(user.getPassword())){
//            messageModel1.setStatus(0);
//            messageModel1.setMsg("用户密码不正确");
//            System.out.println("输入密码：" + passWord);
//            System.out.println("数据库密码：" + user.getPassword());
//            return  messageModel1;
//        }
//        messageModel1.setStatus(HttpStatus.OK.value());
//        messageModel1.setMsg("登录成功");
//        messageModel1.setData(user);
//        return messageModel1;
//    }
    //用户注册

    public MessageModel<User> userRegister(User user){
        MessageModel<User> messageModel1 = new MessageModel<>();

        if (StringUtil.isEmpty(user.getUserName())||StringUtil.isEmpty(user.getPassword())){
            messageModel1.setStatus(400);
            messageModel1.setMsg("用户名与密码不能为空!");
            messageModel1.setData(user);
            return messageModel1;
        }
        try{
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
            userMapper.insertUser(user);
            messageModel1.setData(user);
            messageModel1.setMsg("注册成功");
            messageModel1.setStatus(HttpStatus.OK.value());
            return messageModel1;
        }catch (PersistenceException e){
            messageModel1.setStatus(409);
            messageModel1.setMsg("注册失败");
            messageModel1.setData(user);
            return messageModel1;
        }
    }
    //用户删除

    public MessageModel userDelete(int id){
        MessageModel messageModel1 = new MessageModel();
        int rowsAffected = userMapper.delete(id);
        if(rowsAffected > 0){
            messageModel1.setStatus(HttpStatus.OK.value());
            messageModel1.setMsg("用户已成功注销");
        }else {
            messageModel1.setStatus(0);
            messageModel1.setMsg("删除失败，用户可能不存在");
        }
        return messageModel1;
    }
}
