package com.example.HotelManagementSystem.service.serviceImpl;

import com.example.HotelManagementSystem.dto.request.BookingRequestDTO;
import com.example.HotelManagementSystem.dto.response.BookingResponseDTO;
import com.example.HotelManagementSystem.entity.Booking;
import com.example.HotelManagementSystem.entity.Room;
import com.example.HotelManagementSystem.repository.BookingRepository;
import com.example.HotelManagementSystem.repository.RoomRepository;
import com.example.HotelManagementSystem.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository, RoomRepository roomRepository) {
        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
    }

    @Override
    public BookingResponseDTO createBooking(BookingRequestDTO bookingDTO) {
        if (bookingDTO == null) {
            throw new IllegalArgumentException("Booking request cannot be null");
        }
        List<Long> roomIds = bookingDTO.getRoomIds();
        if (roomIds == null || roomIds.isEmpty()) {
            throw new IllegalArgumentException("At least one room must be selected");
        }

        List<Room> rooms = roomRepository.findAllByIdIn(roomIds);
        if (rooms.size() != roomIds.size()) {
            List<Long> missingIds = roomIds.stream()
                    .filter(id -> rooms.stream().noneMatch(r -> r.getId().equals(id)))
                    .collect(Collectors.toList());
            throw new IllegalArgumentException("Rooms " + missingIds + " do not exist");
        }

        List<Room> unavailableRooms = rooms.stream()
                    .filter(room -> !"available".equals(room.getStatus()))
                .collect(Collectors.toList());
        if (!unavailableRooms.isEmpty()) {
            throw new IllegalArgumentException("Rooms " + unavailableRooms.stream().map(Room::getId).collect(Collectors.toList()) + " are not available");
        }

        Booking booking = new Booking();
        booking.setRooms(rooms);
        booking.setCheckInTime(bookingDTO.getCheckInTime() != null ? bookingDTO.getCheckInTime() : LocalDateTime.now());
        booking.setCheckOutTime(bookingDTO.getCheckOutTime() != null ? bookingDTO.getCheckOutTime() : LocalDateTime.now().plusDays(1));
        booking.setGuestName(bookingDTO.getGuestName() != null ? bookingDTO.getGuestName() : "Guest");

        booking.setStatus("Confirmed");  // Default status
        booking.setBookingDate(LocalDateTime.now());

        booking = bookingRepository.save(booking);

        rooms.forEach(room -> {
            room.setStatus("Occupy");
            roomRepository.save(room);
        });

        return mapToResponseDTO(booking, roomIds);
    }

    @Override
    public BookingResponseDTO checkIn(Long bookingId) {
        if (bookingId == null) {
            throw new IllegalArgumentException("Booking ID cannot be null");
        }
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found with ID: " + bookingId));
        if (booking.getRooms() != null) {
            List<Room> rooms = booking.getRooms();
            List<Room> unavailableRooms = rooms.stream()
                    .filter(room -> !"AVAILABLE".equals(room.getStatus()))
                    .collect(Collectors.toList());
            if (!unavailableRooms.isEmpty()) {
                throw new IllegalArgumentException("Some rooms are already occupied: " + unavailableRooms.stream().map(Room::getId).collect(Collectors.toList()));
            }
            rooms.forEach(room -> {
                room.setStatus("OCCUPIED");
                roomRepository.save(room);
            });
        }
        return mapToResponseDTO(booking, booking.getRooms().stream().map(Room::getId).collect(Collectors.toList()));
    }

    @Override
    public Booking checkOut(Long bookingId) {
        if (bookingId == null) {
            throw new IllegalArgumentException("Booking ID cannot be null");
        }
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found with ID: " + bookingId));
        if (booking.getRooms() != null) {
            booking.getRooms().forEach(room -> {
                room.setStatus("AVAILABLE");
                roomRepository.save(room);
            });
        }
        bookingRepository.delete(booking);
        return booking;
    }

    @Override
    public List<BookingResponseDTO> createManyBookings(List<BookingRequestDTO> bookingDTOs) {
        if (bookingDTOs == null || bookingDTOs.isEmpty()) {
            throw new IllegalArgumentException("Booking list cannot be null or empty");
        }
        return bookingDTOs.stream().map(this::createBooking).collect(Collectors.toList());
    }

    private BookingResponseDTO mapToResponseDTO(Booking booking, List<Long> roomIds) {
        BookingResponseDTO response = new BookingResponseDTO();
        response.setId(booking.getId());
        response.setRoomIds(roomIds);
        response.setCheckInTime(booking.getCheckInTime());
        response.setCheckOutTime(booking.getCheckOutTime());
        response.setGuestName(booking.getGuestName());
        response.setStatus(booking.getStatus());
        response.setBookingDate(booking.getBookingDate());
        return response;
    }
}