package com.example.HotelManagementSystem.repository;

import com.example.HotelManagementSystem.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking,Long> {
}
