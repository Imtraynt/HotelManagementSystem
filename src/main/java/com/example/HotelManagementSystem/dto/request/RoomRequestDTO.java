package com.example.HotelManagementSystem.dto.request;

public class RoomRequestDTO {
    private String roomNumber;
    private String status;
    private Double price;

    public RoomRequestDTO() {}

    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
}