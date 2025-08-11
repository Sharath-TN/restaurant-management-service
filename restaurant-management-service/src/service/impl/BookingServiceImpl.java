package service.impl;

import dao.BookingDao;
import dao.CustomerDao;
import model.Booking;
import model.Customer;
import service.BookingService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class BookingServiceImpl implements BookingService {
    private final CustomerDao customerDao;
    private final BookingDao bookingDao;
    private final Scanner scanner;

    public BookingServiceImpl(CustomerDao customerDao, BookingDao bookingDao, Scanner scanner) {
        this.customerDao = customerDao;
        this.bookingDao = bookingDao;
        this.scanner = scanner;
    }

    @Override
    public void bookTable() {
        try {
            System.out.println("\n--- Table Booking ---");

            System.out.print("Enter your name: ");
            String name = scanner.nextLine();

            System.out.print("Enter your phone number: ");
            String phone = scanner.nextLine();

            System.out.print("Enter number of people: ");
            int numPeople = Integer.parseInt(scanner.nextLine());

            System.out.print("Enter booking time (yyyy-MM-dd HH:mm): ");
            String bookingTimeStr = scanner.nextLine();
            LocalDateTime bookingTime = LocalDateTime.parse(bookingTimeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

            // Validate booking time
            if (bookingTime.isBefore(LocalDateTime.now())) {
                System.out.println("❌ Booking time cannot be in the past.");
                return;
            }
            // Create and insert customer
            Customer customer = new Customer(name, phone);
            int customerId = customerDao.insertCustomer(customer);

            // Create and insert booking
            Booking booking = new Booking(customerId, bookingTime, numPeople);
            int bookingId = bookingDao.insertBooking(booking);

            System.out.println("✅ Booking successful! Your Booking ID is: " + bookingId);
        } catch (Exception e) {
            System.out.println("❌ Error booking table: " + e.getMessage());
        }
    }
}
