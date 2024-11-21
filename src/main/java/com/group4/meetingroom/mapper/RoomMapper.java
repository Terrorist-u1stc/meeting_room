package com.group4.meetingroom.mapper;
import com.group4.meetingroom.entity.MeetingRoom;
import com.group4.meetingroom.entity.User;
import org.apache.ibatis.annotations.*;

@Mapper

public interface RoomMapper {
    //新增会议室
    @Insert("""
            insert into meeting_room(room_name,capacity,room_id,status,equipment)
            values(#{roomName}, #{capacity}, room_id_seq.NEXTVAL,1,#{equipment})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "room_id")
    @Results({
            @Result(property = "id", column = "room_id"),
            @Result(property = "roomName", column = "room_name"),
            @Result(property = "capacity", column = "capacity"),
            @Result(property = "status", column = "status"),
    })
    void addRoom(MeetingRoom room);
    @Select("""
           select room_id,
                  room_name,
                  capacity,
                  status,
                  equipment
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
}
