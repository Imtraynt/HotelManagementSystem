package com.example.HotelManagementSystem.service.serviceImpl;

import com.example.HotelManagementSystem.dto.request.BookingRequestDTO;
import com.example.HotelManagementSystem.dto.response.BookingResponseDTO;
import com.example.HotelManagementSystem.dto.response.RoomInfoDTO;
import com.example.HotelManagementSystem.dto.response.RoomResponseDTO;
import com.example.HotelManagementSystem.entity.Booking;
import com.example.HotelManagementSystem.entity.Room;
import com.example.HotelManagementSystem.repository.BookingRepository;
import com.example.HotelManagementSystem.repository.RoomRepository;
import com.example.HotelManagementSystem.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.print.Book;
import java.time.LocalDate;
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

        booking.setStatus("Booked");  // Default status
        booking.setBookingDate(LocalDateTime.now());

        booking = bookingRepository.save(booking);

        rooms.forEach(room -> {
            room.setStatus("occupy");
            roomRepository.save(room);
        });
        return mapToResponseDTO(booking, roomIds);
    }

    @Override
    public BookingResponseDTO updateBooking(Long bookingId, BookingRequestDTO bookingDTO) {
        if (bookingId == null || bookingDTO == null) {
            throw new IllegalArgumentException("Booking ID and booking data cannot be null");
        }

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found with ID: " + bookingId));

        // Update fields if provided
        if (bookingDTO.getGuestName() != null) {
            booking.setGuestName(bookingDTO.getGuestName());
        }
        if (bookingDTO.getCheckInTime() != null) {
            booking.setCheckInTime(bookingDTO.getCheckInTime());
        }
        if (bookingDTO.getCheckOutTime() != null) {
            booking.setCheckOutTime(bookingDTO.getCheckOutTime());
        }

        // Handle rooms update if roomIds provided
        if (bookingDTO.getRoomIds() != null && !bookingDTO.getRoomIds().isEmpty()) {
            List<Room> rooms = roomRepository.findAllByIdIn(bookingDTO.getRoomIds());
            if (rooms.size() != bookingDTO.getRoomIds().size()) {
                List<Long> missingIds = bookingDTO.getRoomIds().stream()
                        .filter(id -> rooms.stream().noneMatch(r -> r.getId().equals(id)))
                        .collect(Collectors.toList());
                throw new IllegalArgumentException("Rooms " + missingIds + " do not exist");
            }
            booking.setRooms(rooms);
        }

        // Update status
        if (bookingDTO.getStatus() != null) {
            booking.setStatus(bookingDTO.getStatus());

            // If status is canceled, update related rooms to available
            if (bookingDTO.getStatus().equalsIgnoreCase("canceled")) {
                for (Room room : booking.getRooms()) {
                    room.setStatus("available"); // Ensure this matches your RoomStatus enum or DB value
                }
                roomRepository.saveAll(booking.getRooms());
            }
        }

        booking = bookingRepository.save(booking);
        return mapToResponseDTO(booking, booking.getRooms().stream().map(Room::getId).collect(Collectors.toList()));
    }


    @Override
    public void deleteBooking(Long bookingId) {
        if (bookingId == null) {
            throw new IllegalArgumentException("Booking ID cannot be null");
        }
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found with ID: " + bookingId));

        // Before deleting, set rooms to AVAILABLE
        if (booking.getRooms() != null) {
            booking.getRooms().forEach(room -> {
                room.setStatus("available");
                roomRepository.save(room);
            });
        }
        bookingRepository.delete(booking);
    }

    @Override
    public List<BookingResponseDTO> getAllBookings(String status) {
        List<Booking> bookings;

        if (status == null || status.isEmpty()) {
            bookings = bookingRepository.findAll();
        } else {
            bookings = bookingRepository.findByStatusIgnoreCase(status);
        }

        return bookings.stream()
                .map(b -> mapToResponseDTO(
                        b,
                        b.getRooms().stream()
                                .map(Room::getId)
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }
    @Override
    public BookingResponseDTO getBookingById(Long bookingId) {
        if (bookingId == null) {
            throw new IllegalArgumentException("Booking ID cannot be null");
        }
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found with ID: " + bookingId));
        return mapToResponseDTO(booking, booking.getRooms().stream().map(Room::getId).collect(Collectors.toList()));
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

    @Override
    public long countCurrentGuests() {
        // Assuming status "check_in" means guest currently checked in
        return bookingRepository.countByStatusIgnoreCase("check_in");
    }

    @Override
    public long countAvailableRooms() {
        return roomRepository.countByStatusIgnoreCase("available");
    }

    @Override
    public long countTodaysBookings() {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.plusDays(1).atStartOfDay().minusNanos(1);

        // Status "booked" means newly booked
        return bookingRepository.countByStatusIgnoreCaseAndBookingDateBetween("booked", startOfDay, endOfDay);
    }

    @Override
    public long countCheckInsToday() {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.plusDays(1).atStartOfDay().minusNanos(1);

        return bookingRepository.countByStatusIgnoreCaseAndCheckInTimeBetween("check_in", startOfDay, endOfDay);
    }

    @Override
    public long countCheckOutsToday() {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.plusDays(1).atStartOfDay().minusNanos(1);

        return bookingRepository.countByStatusIgnoreCaseAndCheckOutTimeBetween("check_out", startOfDay, endOfDay);
    }

    @Override
    public double getTotalRevenue() {
        Double total = bookingRepository.sumRevenueByStatus("check_out"); // Assuming revenue from completed check-outs
        return total != null ? total : 0.0;
    }




    private BookingResponseDTO mapToResponseDTO(Booking booking, List<Long> roomIds) {

            List<RoomInfoDTO> roomDetails = booking.getRooms().stream()
                    .map(room -> new RoomInfoDTO(room.getId(), room.getRoomNumber(), room.getPrice()))
                    .collect(Collectors.toList());


            BookingResponseDTO response = new BookingResponseDTO();
            response.setId(booking.getId());
            response.setRoomIds(roomIds);
            response.setRooms(roomDetails);
            response.setCheckInTime(booking.getCheckInTime());
            response.setCheckOutTime(booking.getCheckOutTime());
            response.setGuestName(booking.getGuestName());
            response.setStatus(booking.getStatus());
            response.setBookingDate(booking.getBookingDate());
            return response;
        }
}