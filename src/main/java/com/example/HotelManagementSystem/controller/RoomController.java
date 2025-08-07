package com.example.HotelManagementSystem.controller;

import com.example.HotelManagementSystem.dto.request.RoomRequestDTO;
import com.example.HotelManagementSystem.dto.response.RoomResponseDTO;
import com.example.HotelManagementSystem.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api/room")
public class RoomController {

    private final RoomService roomService;

    @Autowired
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping("/createRoom")
    public ResponseEntity<RoomResponseDTO> createRoom(@RequestBody RoomRequestDTO roomRequestDTO) {
        RoomResponseDTO response = roomService.createRoom(roomRequestDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/createManyRooms")
    public ResponseEntity<List<RoomResponseDTO>> createManyRooms(@RequestBody List<RoomRequestDTO> roomRequestDTOs) {
        List<RoomResponseDTO> responses = roomService.createManyRooms(roomRequestDTOs);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomResponseDTO> getRoomById(@PathVariable Long id) {
        RoomResponseDTO response = roomService.getRoomById(id);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/available")
    public ResponseEntity<List<RoomResponseDTO>> getAvailableRooms() {
        return ResponseEntity.ok(roomService.getAvailableRooms());
    }

    @GetMapping("/getRooms")
    public ResponseEntity<List<RoomResponseDTO>> getAllRooms() {
        List<RoomResponseDTO> rooms = roomService.getAllRooms();
        return ResponseEntity.ok(rooms);
    }

    @PutMapping("/updateRoom/{id}")
    public ResponseEntity<RoomResponseDTO> updateRoom(@PathVariable Long id, @RequestBody RoomRequestDTO roomRequestDTO) {
        RoomResponseDTO updatedRoom = roomService.updateRoom(id, roomRequestDTO);
        return ResponseEntity.ok(updatedRoom);
    }

    @DeleteMapping("/deleteRoom/{id}")
    public ResponseEntity<String> deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
        return ResponseEntity.ok("Room deleted successfully");
    }
}