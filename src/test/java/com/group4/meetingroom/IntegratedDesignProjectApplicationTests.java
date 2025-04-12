package com.group4.meetingroom;

import com.group4.meetingroom.entity.User;
import com.group4.meetingroom.entity.vo.MessageModel;
import com.group4.meetingroom.mapper.UserMapper;
import com.group4.meetingroom.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class IntegratedDesignProjectApplicationTests {
	private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); // 确保与 SecurityConfig 中的 Bean 一致

	@Autowired
	private UserMapper userMapper;

	@Test
	public void testPasswordMatch() {
		String rawPassword = "123456"; // 用户输入的原始密码
		String username = "admin";   // 测试用户名

		// --- 步骤 1: 模拟注册流程 ---
		// 假设你的注册方法需要手动调用，此处直接操作数据库


		// --- 步骤 2: 查询数据库中的加密密码 ---
		User dbUser = userMapper.selectByName(username);
		// ✅ 使用 matches() 方法验证
		boolean isMatch = passwordEncoder.matches(rawPassword, dbUser.getPassword());
		System.out.println("密码是否匹配: " + isMatch); // 应输出 true
	}

}
