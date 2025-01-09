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
    //查询预约记录，无时间要求，表里没有room_name的字段，记得改一下
    @Select("""
            SELECT r.start_time, r.end_time, r.user_id, r.room_id, mr.room_name
            FROM room_reservation r
            JOIN meeting_room mr ON r.room_id = mr.room_id
            WHERE r.user_id = #{userId}
            """)
    @Results({
            @Result(property = "startTime", column = "start_time"),
            @Result(property = "endTime", column = "end_time"),
            @Result(property = "rReservationId", column = "r_reservation_id"),
            @Result(property = "userId", column = "user_id"),
            @Result(property = "roomId", column = "room_id"),
            @Result(property = "roomName", column = "room_name")
    })

    List<RoomReservation> findByUserId(Integer userId);
    //注意，这段代码有问题，因为数据库中的表里，没有room_name这个字段
    @Select("SELECT rr.start_time, rr.end_time, rr.r_reservation_id, rr.user_id, rr.room_id, mr.room_name " +
            "FROM room_reservation rr " +
            "JOIN meeting_room mr ON rr.room_id = r.room_id " +
            "WHERE rr.user_id = #{userId} " +
            "AND TRUNC(rr.start_time) = TO_DATE(#{date}, 'yyyy-MM-dd')")
    @Results({
            @Result(property = "startTime", column = "start_time"),
            @Result(property = "endTime", column = "end_time"),
            @Result(property = "rReservationId", column = "r_reservation_id"),
            @Result(property = "userId", column = "user_id"),
            @Result(property = "roomId", column = "room_id"),
            @Result(property = "roomName", column = "room_name")
    })
    List<RoomReservation> findByUserIdAndDate(Integer userId, String date);
    @Select("""
            SELECT start_time, end_time, room_id
            from room_reservation
            WHERE room_id = #{roomId}
            """)
    @Results({
            @Result(property = "startTime", column = "start_time"),
            @Result(property = "endTime", column = "end_time"),
            @Result(property = "roomId", column = "room_id"),
    })
    List<RoomReservation> selectAll(Integer roomId);
}
