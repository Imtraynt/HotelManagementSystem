package com.example.HotelManagementSystem.dto.response;

public class RoomInfoDTO {
    private Long id;
    private String roomNumber;
    private Double price;

    public RoomInfoDTO(Long id, String roomNumber, Double price) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}

