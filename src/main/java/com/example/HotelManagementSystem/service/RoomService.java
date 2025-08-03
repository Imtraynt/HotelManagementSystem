package com.example.HotelManagementSystem.service;

import com.example.HotelManagementSystem.dto.request.RoomRequestDTO;
import com.example.HotelManagementSystem.dto.response.RoomResponseDTO;
import java.util.List;

public interface RoomService {
    RoomResponseDTO createRoom(RoomRequestDTO roomDTO);
    List<RoomResponseDTO> createManyRooms(List<RoomRequestDTO> roomDTOs);
    RoomResponseDTO getRoomById(Long id);
}