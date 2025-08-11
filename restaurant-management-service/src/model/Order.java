package model;

import java.time.LocalDateTime;

public class Order {
    private int id;
    private int bookingId;
    private String waiterName;
    private String status;
    private LocalDateTime createdAt;

    public Order() {}

    public Order(int id, int bookingId, String waiterName, String status, LocalDateTime createdAt) {
        this.id = id;
        this.bookingId = bookingId;
        this.waiterName = waiterName;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Order(int bookingId, String waiterName, String status) {
        this.bookingId = bookingId;
        this.waiterName = waiterName;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public String getWaiterName() {
        return waiterName;
    }

    public void setWaiterName(String waiterName) {
        this.waiterName = waiterName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
