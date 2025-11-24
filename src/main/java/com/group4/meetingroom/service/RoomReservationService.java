package com.group4.meetingroom.service;

import com.group4.meetingroom.entity.Booking;
import com.group4.meetingroom.entity.MeetingRoom;
import com.group4.meetingroom.entity.RoomReservation;
import com.group4.meetingroom.entity.User;
import com.group4.meetingroom.entity.vo.MessageModel;
import com.group4.meetingroom.mapper.RoomMapper;
import com.group4.meetingroom.mapper.RoomReservationMapper;
import com.group4.meetingroom.mapper.UserMapper;
import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RoomReservationService {
    @Autowired
    RoomReservationMapper roomReservationMapper;
    @Autowired
    RoomMapper roomMapper;
    @Autowired
    UserMapper userMapper;
    public MessageModel<RoomReservation> selectById(Integer id){
    MessageModel<RoomReservation> result = new MessageModel<>();
    return result;
}
    public MessageModel<Void> reserveRoom(LocalDateTime startTime,LocalDateTime endTime,int roomId,
                                          int attendees, int userId) {
        MessageModel<Void> response = new MessageModel<>();
       User user = userMapper.selectById(userId);
        RoomReservation roomReservation = new RoomReservation();
        roomReservation.setStartTime(startTime);
        roomReservation.setEndTime(endTime);
        roomReservation.setUserId(userId);
        roomReservation.setRoomId(roomId);
        roomReservation.setUserName(user.getUserName());
        roomReservation.setAttendees(attendees);

        MeetingRoom room;
        room = roomMapper.selectById(roomId);

        if (room == null) {
            response.setMsg("该会议室不存在");
            response.setStatus(404);
            return response;
        }

        // 检查会议室状态是否可用
        if (room.getStatus() != 1) {
            response.setMsg("该会议室目前不可用");
            response.setStatus(400);
            return response;
        }

        // 检查预约时间是否冲突
        List<RoomReservation> reservations = roomReservationMapper.findReservationTimesByRoomId(roomId);
        for (RoomReservation existingReservation : reservations) {
            if (roomReservation.getStartTime().isBefore(existingReservation.getEndTime()) &&
                    roomReservation.getEndTime().isAfter(existingReservation.getStartTime())) {
                response.setMsg("该会议室该时间段已被占用");
                response.setStatus(409);
                return response;
            }
        }
        try{
            // 插入预约记录
            roomReservationMapper.insertReservation(roomReservation);
            response.setStatus(200);
            response.setMsg("预约成功");
            return response;
        }catch (PersistenceException e){

            e.printStackTrace();
            response.setStatus(500);
            response.setMsg("预约失败，请稍后重试或检查预约信息");
            return response;
        }
    }
    //查询预约记录
    public MessageModel<List<RoomReservation>> getBookings(String date, Integer userId){
        List<RoomReservation> reservations;
        if(date == null){
            reservations = roomReservationMapper.findByUserId(userId);
        }else
        {reservations = roomReservationMapper.findByUserIdAndDate(userId, date);}
        if (reservations != null && !reservations.isEmpty()) {
//            for(RoomReservation reservation: reservations){
//                //统计参会人数
//                reservation.setParticipants(roomReservationMapper.countAttendees(reservation.getrReservationId()));
//            }
            // 如果有预定，返回成功状态
            return new MessageModel<>(1, "成功", reservations);
        } else {
            // 如果没有预定，返回失败状态
            return new MessageModel<>(0, "没有找到预定记录", null);
        }
    }

    public MessageModel<Void> cancel(int id){
        MessageModel<Void> response= new MessageModel<>();

        int result = roomReservationMapper.cancel(id);
        if (result > 0) {
            response.setMsg("取消预约成功");
            response.setStatus(200);
        } else {
            response.setStatus(500); // 失败
            response.setMsg("取消预约失败");
        }

        return response;
    }
    public MessageModel<RoomReservation> getMeetingDetail(Integer reservationId){
        if (reservationId == null) {
            return new MessageModel<>(400, "reservationId 不能为空", null);
        }

        try {
            RoomReservation reservation = roomReservationMapper.selectById(reservationId);
            if (reservation == null) {
                return new MessageModel<>(404, "该会议预约不存在或已取消", null);
            }
            return new MessageModel<>(200, "查询成功", reservation);

        } catch (Exception e) {
            e.printStackTrace();
            return new MessageModel<>(500, "服务器内部异常", null);
        }
    }
    public MessageModel<Void> joinMeeting(Integer userId, Integer reservationId){
        MessageModel<Void> result = new MessageModel<>();

        if(userId == null || reservationId == null){
            result.setStatus(400);
            result.setMsg("用户ID或预约ID不能为空");
            return result;
        }

        int reservationCount = roomReservationMapper.reservationExists(reservationId);
        if(reservationCount == 0){
            result.setStatus(404);
            result.setMsg("该预约不存在");
            return result;
        }

        int count = roomReservationMapper.countByReservationAndUser(reservationId, userId);
        if(count > 0){
            result.setStatus(409);
            result.setMsg("用户已参会，无需重复加入");
            return result;
        }

        roomReservationMapper.insert(reservationId, userId);
        result.setStatus(200);
        result.setMsg("加入会议成功");
        return result;
    }
}
