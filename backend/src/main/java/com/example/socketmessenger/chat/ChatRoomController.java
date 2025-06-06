package com.example.socketmessenger.chat;

import com.example.socketmessenger.util.ResponseUtil;
import com.example.socketmessenger.util.enums.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat/rooms")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @GetMapping("/list")
    public ResponseEntity<?> listRooms() {
        return ResponseUtil.createSuccessResponse(SuccessCode.SELECT_SUCCESS, chatRoomService.getAllRooms());
    }

    @PostMapping("/create")
    public ResponseEntity<?> createRoom(@RequestParam String roomName) {
        ChatRoom chatRoom = chatRoomService.createRoom(roomName);
        return ResponseUtil.createSuccessResponse(SuccessCode.INSERT_SUCCESS, chatRoom);
    }
}
