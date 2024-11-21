package com.group4.meetingroom.mapper;
import com.group4.meetingroom.entity.User;

import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    //注册用户
    @Insert("""
            insert into m_room_user(username,pwd,phone,user_id,role)
            values(#{userName}, #{passWord}, #{phoneNumber}, user_id_seq.NEXTVAL,0)
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "user_id")
    @Results({
            @Result(property = "id", column = "user_id"),
            @Result(property = "userName", column = "username"),
            @Result(property = "passWord", column = "pwd"),
            @Result(property = "phoneNumber", column = "phone"),
    })
    void insertUser(User user);
    //删除用户
    @Delete("""
           delete from m_room_user where user_id = #{id}
           """)
    int  delete(int id);
    //通过id查询用户
    @Select("""
           select user_id,
                  username,
                  phone,
                  role
           from m_room_user
           where user_id = #{id}
           """)
    @Results({
            @Result(property = "id", column = "user_id"),
            @Result(property = "userName", column = "username"),
            @Result(property = "passWord", column = "pwd"),
            @Result(property = "phoneNumber", column = "phone")
    })
    User selectById(int id);
    //通过用户名查询
    @Select("""
           select user_id,
                  username,
                  phone,
                  pwd,
                  role
           from m_room_user
           where username = #{userName}
           """)
    @Results({
            @Result(property = "id", column = "user_id"),
            @Result(property = "userName", column = "username"),
            @Result(property = "passWord", column = "pwd"),
            @Result(property = "phoneNumber", column = "phone")
    })
    User selectByName(String userName);

}

