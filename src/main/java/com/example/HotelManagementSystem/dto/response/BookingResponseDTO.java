package com.example.HotelManagementSystem.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public class BookingResponseDTO {
    private Long id;
    private List<Long> roomIds;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private String guestName;

    public BookingResponseDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public List<Long> getRoomIds() { return roomIds; }
    public void setRoomIds(List<Long> roomIds) { this.roomIds = roomIds; }
    public LocalDateTime getCheckInTime() { return checkInTime; }
    public void setCheckInTime(LocalDateTime checkInTime) { this.checkInTime = checkInTime; }
    public LocalDateTime getCheckOutTime() { return checkOutTime; }
    public void setCheckOutTime(LocalDateTime checkOutTime) { this.checkOutTime = checkOutTime; }
    public String getGuestName() { return guestName; }
    public void setGuestName(String guestName) { this.guestName = guestName; }
}