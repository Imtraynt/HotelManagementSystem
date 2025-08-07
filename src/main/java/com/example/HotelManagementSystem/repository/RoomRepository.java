package com.example.HotelManagementSystem.repository;

import com.example.HotelManagementSystem.entity.Booking;
import com.example.HotelManagementSystem.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room,Long> {
    List<Room> findAllByIdIn(List<Long> roomIds);
    List<Room> findByStatus(String status);
    long countByStatusIgnoreCase(String status);
}
