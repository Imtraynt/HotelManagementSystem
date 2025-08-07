package com.example.HotelManagementSystem.service.serviceImpl;

import com.example.HotelManagementSystem.dto.request.RoomRequestDTO;
import com.example.HotelManagementSystem.dto.response.RoomResponseDTO;
import com.example.HotelManagementSystem.entity.Room;
import com.example.HotelManagementSystem.repository.RoomRepository;
import com.example.HotelManagementSystem.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

    @Autowired
    public RoomServiceImpl(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public RoomResponseDTO createRoom(RoomRequestDTO roomDTO) {
        if (roomDTO == null || roomDTO.getRoomNumber() == null || roomDTO.getPrice() == null) {
            throw new IllegalArgumentException("Room number and price are required");
        }

        Room room = new Room();
        room.setRoomNumber(roomDTO.getRoomNumber());
        room.setStatus(roomDTO.getStatus() != null ? roomDTO.getStatus() : "available");
        room.setPrice(roomDTO.getPrice());
        room.setRoomType(roomDTO.getRoomType());
        room = roomRepository.save(room);

        return mapToResponseDTO(room);
    }

    @Override
    public List<RoomResponseDTO> createManyRooms(List<RoomRequestDTO> roomDTOs) {
        if (roomDTOs == null || roomDTOs.isEmpty()) {
            throw new IllegalArgumentException("Room list cannot be null or empty");
        }

        List<Room> rooms = roomDTOs.stream().map(dto -> {
            if (dto == null || dto.getRoomNumber() == null || dto.getPrice() == null) {
                throw new IllegalArgumentException("Each room must have a number and price");
            }
            Room room = new Room();
            room.setRoomNumber(dto.getRoomNumber());
            room.setStatus(dto.getStatus() != null ? dto.getStatus() : "AVAILABLE");
            room.setPrice(dto.getPrice());
            room.setRoomType(dto.getRoomType());
            return room;
        }).collect(Collectors.toList());

        rooms = roomRepository.saveAll(rooms);

        return rooms.stream().map(this::mapToResponseDTO).collect(Collectors.toList());
    }

    @Override
    public RoomResponseDTO getRoomById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found with ID: " + id));
        return mapToResponseDTO(room);
    }

    @Override
    public List<RoomResponseDTO> getAvailableRooms() {
        List<Room> rooms = roomRepository.findByStatus("available");
        return rooms.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomResponseDTO> getAllRooms() {
        List<Room> rooms = roomRepository.findAll();
        return rooms.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RoomResponseDTO updateRoom(Long id, RoomRequestDTO roomRequestDTO) {
        if (id == null || roomRequestDTO == null) {
            throw new IllegalArgumentException("ID and Room data are required");
        }

        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found with ID: " + id));

        // Update fields
        if (roomRequestDTO.getRoomNumber() != null) {
            room.setRoomNumber(roomRequestDTO.getRoomNumber());
        }
        if (roomRequestDTO.getStatus() != null) {
            room.setStatus(roomRequestDTO.getStatus());
        }
        if (roomRequestDTO.getPrice() != null) {
            room.setPrice(roomRequestDTO.getPrice());
        }
        if (roomRequestDTO.getRoomType() != null) {
            room.setRoomType(roomRequestDTO.getRoomType());
        }

        Room updatedRoom = roomRepository.save(room);
        return mapToResponseDTO(updatedRoom);
    }

    @Override
    public void deleteRoom(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found with ID: " + id));

        roomRepository.delete(room);
    }



    private RoomResponseDTO mapToResponseDTO(Room room) {
        RoomResponseDTO response = new RoomResponseDTO();
        response.setId(room.getId());
        response.setRoomNumber(room.getRoomNumber());
        response.setStatus(room.getStatus());
        response.setPrice(room.getPrice());
        response.setRoomType(room.getRoomType());
        return response;
    }
}