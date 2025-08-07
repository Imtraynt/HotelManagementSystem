package com.example.HotelManagementSystem.dto.response;

public class BookingStatsDTO {
    private long currentGuests;
    private long availableRooms;
    private long todaysBookings;
    private long checkInsToday;
    private long checkOutsToday;
    private double totalRevenue;

    // Constructors, getters, setters

    public BookingStatsDTO() {}

    public BookingStatsDTO(long currentGuests, long availableRooms, long todaysBookings, long checkInsToday, long checkOutsToday, double totalRevenue) {
        this.currentGuests = currentGuests;
        this.availableRooms = availableRooms;
        this.todaysBookings = todaysBookings;
        this.checkInsToday = checkInsToday;
        this.checkOutsToday = checkOutsToday;
        this.totalRevenue = totalRevenue;
    }

    // getters and setters ...
}
