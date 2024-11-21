package com.group4.meetingroom.mapper;

import com.group4.meetingroom.entity.RoomReservation;
import org.apache.ibatis.annotations.*;

import java.util.List;
@Mapper
public interface RoomReservationMapper {
    @Select("SELECT start_time, end_time FROM room_reservation WHERE room_id = #{roomId}")
    @Results({
            @Result(property = "startTime", column = "start_time"),
            @Result(property = "endTime", column = "end_time")
    })

    List<RoomReservation> findReservationTimesByRoomId(Integer roomId);

    @Insert("""
            INSERT INTO room_reservation (r_reservation_id, user_id, room_id, start_time, end_time) 
            VALUES (room_reserv_id_seq.NEXTVAL, #{userId}, #{roomId}, #{startTime}, #{endTime})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "rReservationId", keyColumn = "r_reservation_id")
    @Results({
            @Result(property = "rReservationId", column = "r_reservation_id"),
            @Result(property = "userId", column = "user_id"),
            @Result(property = "roomId", column = "room_id"),
            @Result(property = "startTime", column = "start_time"),
            @Result(property = "endTime", column = "end_time")
    })
    void insertReservation(RoomReservation roomReservation);
}
