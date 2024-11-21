package com.group4.meetingroom;

import com.group4.meetingroom.entity.User;
import com.group4.meetingroom.entity.vo.MessageModel;
import com.group4.meetingroom.mapper.UserMapper;
import com.group4.meetingroom.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class IntegratedDesignProjectApplicationTests {
	@Autowired
	UserMapper userMapper;
	@Autowired
	UserService userService;
	@Test
	void contextLoads() {
		User user = new User();
		user.setPassWord("1234567");
		user.setUserName("tom");
		user.setPhoneNumber("123456789");
		user.setId(1);
		userMapper.delete(4);
		//userMapper.insertUser(user);
		//MessageModel messageModel = userService.userLogin(user.getUserName(), user.getPassWord());
		//System.out.println(messageModel.getMsg());
	}

}
