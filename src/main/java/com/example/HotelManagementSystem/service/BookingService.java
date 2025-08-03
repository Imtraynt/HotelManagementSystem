package com.example.HotelManagementSystem.service;

import com.example.HotelManagementSystem.dto.request.BookingRequestDTO;
import com.example.HotelManagementSystem.dto.response.BookingResponseDTO;
import com.example.HotelManagementSystem.entity.Booking;
import java.util.List;

public interface BookingService {
    BookingResponseDTO createBooking(BookingRequestDTO bookingDTO);
    BookingResponseDTO checkIn(Long bookingId);
    Booking checkOut(Long bookingId);
    List<BookingResponseDTO> createManyBookings(List<BookingRequestDTO> bookingDTOs);
}