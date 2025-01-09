package com.group4.meetingroom.mapper;
import com.group4.meetingroom.entity.Booking;
import com.group4.meetingroom.entity.MeetingRoom;
import com.group4.meetingroom.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper

public interface RoomMapper {
    //新增会议室
    @Insert("""
            insert into meeting_room(room_name,capacity,room_id,status,equipment,location)
            values(#{roomName}, #{capacity}, room_id_seq.NEXTVAL,1,#{equipment},#{location})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "room_id")
    @Results({
            @Result(property = "id", column = "room_id"),
            @Result(property = "roomName", column = "room_name"),
            @Result(property = "capacity", column = "capacity"),
            @Result(property = "status", column = "status"),
    })
    void addRoom(MeetingRoom room);
    //根据id查找会议室信息
    @Select("""
           select room_id,
                  room_name,
                  capacity,
                  status,
                  equipment,
                  location
           from meeting_room
           where room_id = #{id}
           """)
    @Results({
            @Result(property = "id", column = "room_id"),
            @Result(property = "roomName", column = "room_name"),
            @Result(property = "capacity", column = "capacity"),
            @Result(property = "status", column = "status"),
    })
    MeetingRoom selectById(int id);
//根据会议室名查找会议室，貌似没有用到过
    @Select("""
           select room_id,
                  room_name,
                  capacity,
                  status,
                  equipment,
                  location
           from meeting_room
           where room_id = #{roomName}
           """)
    @Results({
            @Result(property = "id", column = "room_id"),
            @Result(property = "roomName", column = "room_name"),
            @Result(property = "capacity", column = "capacity"),
            @Result(property = "status", column = "status"),
    })
    MeetingRoom selectByName(String roomName);
    @Select("""
            select * from meeting_room   
            """)
    @Results({
            @Result(property = "id", column = "room_id"),
            @Result(property = "roomName", column = "room_name"),
            @Result(property = "capacity", column = "capacity"),
            @Result(property = "status", column = "status"),
    })
    List<MeetingRoom> selectAll();
    //更新用动态sql语句比较好，但我忘记怎么写了，先将就一下吧
    @Update("""
        UPDATE meeting_room
        SET room_name = #{roomName},
            capacity = #{capacity},
            status = #{status},
            equipment = #{equipment},
            location = #{location}
        WHERE room_id = #{id}
        """)
    void updateRoom(MeetingRoom room);
}
