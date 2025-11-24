package com.group4.meetingroom.mapper;
import com.group4.meetingroom.entity.User;

import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    //注册用户
    @Insert("""
            insert into m_room_user(username,pwd,phone,email,role)
            values(#{userName}, #{password}, #{phoneNumber},#{email},'ROLE_USER')
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "user_id")
    @Results({
            @Result(property = "id", column = "user_id"),
            @Result(property = "userName", column = "username"),
            @Result(property = "password", column = "pwd"),
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
                  token_version,
                  role
           from m_room_user
           where user_id = #{id}
           """)
    @Results({
            @Result(property = "id", column = "user_id"),
            @Result(property = "userName", column = "username"),
            @Result(property = "password", column = "pwd"),
            @Result(property = "phoneNumber", column = "phone"),
            @Result(property = "tokenVersion", column = "token_version")
    })
    User selectById(int id);
    //通过用户名查询
    @Select("""
           select user_id,
                  username,
                  phone,
                  pwd,
                  email,
                  token_version,
                  role
           from m_room_user
           where username = #{userName}
           """)
    @Results({
            @Result(property = "id", column = "user_id"),
            @Result(property = "userName", column = "username"),
            @Result(property = "password", column = "pwd"),
            @Result(property = "phoneNumber", column = "phone"),
            @Result(property = "tokenVersion", column = "token_version")
    })
    User selectByName(String userName);

    @Select("SELECT token_version FROM m_room_user WHERE user_id = #{userId}")
    int getTokenVersionByUserId(@Param("userId") int userId);


    @Update("UPDATE m_room_user " +
            "SET token_version = (token_version + 1) % 1000000 " +
            "WHERE user_id = #{userId}")
    int updateTokenVersion(@Param("userId") int userId);

}

