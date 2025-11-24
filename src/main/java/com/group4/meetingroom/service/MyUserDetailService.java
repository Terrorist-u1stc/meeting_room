package com.group4.meetingroom.service;
import com.group4.meetingroom.entity.CustomUserDetails;
import com.group4.meetingroom.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.group4.meetingroom.entity.User;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailService implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.selectByName(username);
        if(user == null){
            throw new UsernameNotFoundException("用户不存在");
        }
        return new CustomUserDetails(user);
    }
    public CustomUserDetails loadUserById(int userId) {
        User user = userMapper.selectById(userId); // 或者调用 repository
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在: " + userId);
        }
        return new CustomUserDetails(user);
    }

}
