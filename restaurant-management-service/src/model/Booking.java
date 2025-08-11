package model;

import java.time.LocalDateTime;

public class Booking {
    private int id;
    private int customerId;
    private LocalDateTime bookingTime;

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    private int numPeople;
    private Boolean isActive;

    public Booking() {}

    public Booking(int id, int customerId, LocalDateTime bookingTime, int numPeople) {
        this.id = id;
        this.customerId = customerId;
        this.bookingTime = bookingTime;
        this.numPeople = numPeople;
        this.isActive = true;
    }

    public Booking(int customerId, LocalDateTime bookingTime, int numPeople) {
        this.customerId = customerId;
        this.bookingTime = bookingTime;
        this.numPeople = numPeople;
        this.isActive = true;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public LocalDateTime getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(LocalDateTime bookingTime) {
        this.bookingTime = bookingTime;
    }

    public int getNumPeople() {
        return numPeople;
    }

    public void setNumPeople(int numPeople) {
        this.numPeople = numPeople;
    }
}
