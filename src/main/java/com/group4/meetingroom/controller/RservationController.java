package com.group4.meetingroom.controller;
import com.group4.meetingroom.entity.CustomUserDetails;
import com.group4.meetingroom.entity.QRCodeUtil;
import com.group4.meetingroom.entity.RoomReservation;
import com.group4.meetingroom.entity.User;
import com.group4.meetingroom.entity.vo.MessageModel;
import com.group4.meetingroom.service.RoomReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;


@RestController
public class RservationController {
    private static final String QR_CODE_DIR = "/home/ubuntu/qrcodes/";

    @Autowired
    private RoomReservationService reservationService;
    //预约会议室
    @CrossOrigin(origins = "*")
    @PostMapping("/api/meeting-rooms/availability")
//    public Map<String, Object> reserveRoom(
//            @RequestBody Booking booking,
//            HttpSession session) {
//        User user = (User) session.getAttribute("user");
//        if (user == null){
//            Map<String, Object> response = new HashMap<>();
//            response.put("success", false);
//            response.put("message", "用户未登录");
//            response.put("meetingRooms", null);
//            return response;}
//        booking.setUserId(user.getId());
//        booking.setUserName(user.getUserName());
//        return reservationService.reserveRoom(booking);
//    }
    public MessageModel<Void> reserveRoom(@RequestParam LocalDateTime startTime, @RequestParam LocalDateTime endTime,
                                          @RequestParam int roomID,@RequestParam int attendees) {
        User user = ((CustomUserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal()).getUser();

        return reservationService.reserveRoom(startTime, endTime, roomID, attendees, user.getId());
    }
    //查询预约记录
    @CrossOrigin(origins = "*")
    @PostMapping("/getMeetingRoomBookings")
    public MessageModel<List<RoomReservation>> userReserve(
            @RequestParam(required = false) String date
    ){
        User user = ((CustomUserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal()).getUser();
//        if (user == null){
//            MessageModel<List<RoomReservation>> response = new MessageModel<>();
//            response.setMsg("用户未登录");
//            response.setStatus(401);
//            response.setData(null);
//            return response;
//        }
        return reservationService.getBookings(date, user.getId());
    }
    @CrossOrigin(origins = "*")
    @DeleteMapping("/cancel")
    public MessageModel<Void> cancel(@RequestParam int rReservationId){
        return reservationService.cancel(rReservationId);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/generateQRCode")
    public MessageModel<String> generateQRCode(@RequestParam int reservationId) {
        try {
            String qrContent = "http://101.34.82.172:8080/getMeetingDetail?reservationId=" + reservationId;//这里要改成前端页面的url
            String filePath = QR_CODE_DIR + reservationId + ".png";
            String accessUrl = "http://101.34.82.172:8080/qrcodes/" + reservationId +".png";
            QRCodeUtil.generateQRCodeImage(qrContent, 300, 300, filePath);
            return new MessageModel<>(200, "二维码生成成功", accessUrl);
        } catch (Exception e) {
            e.printStackTrace();
            return new MessageModel<>(500, "二维码生成失败", null);
        }
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/getMeetingDetail")
    public MessageModel<RoomReservation> getMeetingDetail(@RequestParam int reservationId){
        return reservationService.getMeetingDetail(reservationId);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/joinMeeting")
    public MessageModel<Void> joinMeeting(@RequestParam int reservationId){
        User user = ((CustomUserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal()).getUser();
        return reservationService.joinMeeting(user.getId(), reservationId);
    }
}


