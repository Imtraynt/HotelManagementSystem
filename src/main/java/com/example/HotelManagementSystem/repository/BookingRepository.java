package com.example.HotelManagementSystem.repository;

import com.example.HotelManagementSystem.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking,Long> {
    List<Booking> findByStatusIgnoreCase(String status);


    long countByStatusIgnoreCase(String status);

    long countByStatusIgnoreCaseAndBookingDateBetween(String status, LocalDateTime start, LocalDateTime end);

    long countByStatusIgnoreCaseAndCheckInTimeBetween(String status, LocalDateTime start, LocalDateTime end);

    long countByStatusIgnoreCaseAndCheckOutTimeBetween(String status, LocalDateTime start, LocalDateTime end);

    @Query("SELECT SUM(r.price) FROM Booking b JOIN b.rooms r WHERE b.status = :status")
    Double sumRevenueByStatus(@Param("status") String status);
}
