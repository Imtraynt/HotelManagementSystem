package com.example.HotelManagementSystem.controller;

import com.example.HotelManagementSystem.dto.request.BookingRequestDTO;
import com.example.HotelManagementSystem.dto.response.BookingResponseDTO;
import com.example.HotelManagementSystem.dto.response.BookingStatsDTO;
import com.example.HotelManagementSystem.entity.Booking;
import com.example.HotelManagementSystem.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api/booking")
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/createBooking")
    public ResponseEntity<BookingResponseDTO> createBooking(@RequestBody BookingRequestDTO bookingRequestDTO) {
        BookingResponseDTO response = bookingService.createBooking(bookingRequestDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/checkIn")
    public ResponseEntity<BookingResponseDTO> checkIn(@RequestBody @RequestParam Long bookingId) {
        BookingResponseDTO response = bookingService.checkIn(bookingId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/checkOut")
    public ResponseEntity<Booking> checkOut(@RequestParam Long bookingId) {
        Booking response = bookingService.checkOut(bookingId); // Assuming refined version
        return ResponseEntity.ok(response);
    }

    @PostMapping("/createManyBookings")
    public ResponseEntity<List<BookingResponseDTO>> createManyBookings(@RequestBody List<BookingRequestDTO> bookingRequestDTOs) {
        List<BookingResponseDTO> responses = bookingService.createManyBookings(bookingRequestDTOs);
        return ResponseEntity.ok(responses);
    }
    @GetMapping("/{id}")
    public ResponseEntity<BookingResponseDTO> getBookingById(@PathVariable Long id) {
        BookingResponseDTO booking = bookingService.getBookingById(id);
        return ResponseEntity.ok(booking);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<BookingResponseDTO>> getAllBookings(
            @RequestParam(required = false) String status) {
        List<BookingResponseDTO> bookings = bookingService.getAllBookings(status);
        return ResponseEntity.ok(bookings);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookingResponseDTO> updateBooking(@PathVariable Long id, @RequestBody BookingRequestDTO bookingRequestDTO) {
        BookingResponseDTO updatedBooking = bookingService.updateBooking(id, bookingRequestDTO);
        return ResponseEntity.ok(updatedBooking);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.ok("Booking with ID " + id + " has been deleted successfully.");
    }


    @GetMapping("/stats")
    public ResponseEntity<BookingStatsDTO> getBookingStats() {
        long currentGuests = bookingService.countCurrentGuests();
        long availableRooms = bookingService.countAvailableRooms();
        long todaysBookings = bookingService.countTodaysBookings();
        long checkInsToday = bookingService.countCheckInsToday();
        long checkOutsToday = bookingService.countCheckOutsToday();
        double totalRevenue = bookingService.getTotalRevenue();

        BookingStatsDTO stats = new BookingStatsDTO(
                currentGuests,
                availableRooms,
                todaysBookings,
                checkInsToday,
                checkOutsToday,
                totalRevenue
        );

        return ResponseEntity.ok(stats);
    }

}