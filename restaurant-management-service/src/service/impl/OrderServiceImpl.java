package service.impl;

import dao.BookingDao;
import dao.MenuItemDao;
import dao.OrderDao;
import dao.OrderItemDao;
import model.MenuItem;
import model.Order;
import model.OrderItem;
import service.OrderService;

import java.util.*;

public class OrderServiceImpl implements OrderService {
    private final MenuItemDao menuDao;
    private final OrderDao orderDao;
    private final OrderItemDao orderItemDao;
    private final BookingDao bookingDao;
    private final Scanner scanner;

    public OrderServiceImpl(MenuItemDao menuDao, OrderDao orderDao, OrderItemDao orderItemDao, BookingDao bookingDao, Scanner scanner) {
        this.menuDao = menuDao;
        this.orderDao = orderDao;
        this.orderItemDao = orderItemDao;
        this.bookingDao = bookingDao;
        this.scanner = scanner;
    }

    @Override
    public void takeOrder() {
        try {
            System.out.println("\n--- Take Order ---");

            // Show active bookings
            var bookings = bookingDao.getAllBookings();
            if (bookings.isEmpty()) {
                System.out.println("⚠️ No active bookings found.");
                return;
            }

            System.out.println("Available Bookings:");
            bookings.forEach((id, info) -> System.out.println(id + ". " + info));

            System.out.print("Select booking ID: ");
            int bookingId = Integer.parseInt(scanner.nextLine());

            // Show menu
            List<MenuItem> menu = menuDao.getAllMenuItems();
            if (menu.isEmpty()) {
                System.out.println("⚠️ No menu items available.");
                return;
            }

            System.out.println("Menu:");
            for (MenuItem item : menu) {
                System.out.println(item.getId() + ". " + item.getName() + " - ₹" + item.getPrice());
            }

            System.out.print("Enter item IDs (comma-separated): ");
            String[] itemIds = scanner.nextLine().split(",");

            List<OrderItem> orderItems = new ArrayList<>();
            for (String itemIdStr : itemIds) {
                int itemId = Integer.parseInt(itemIdStr.trim());

                System.out.print("Enter quantity for item " + itemId + ": ");
                int quantity = Integer.parseInt(scanner.nextLine());

                orderItems.add(new OrderItem(0, itemId, quantity)); // orderId will be set later
            }

            System.out.print("Enter your name (Waiter): ");
            String waiterName = scanner.nextLine();

            Order order = new Order(bookingId, waiterName, "Pending");
            int orderId = orderDao.insertOrder(order);

            for (OrderItem item : orderItems) {
                item.setOrderId(orderId);
                orderItemDao.insertOrderItem(item);
            }

            System.out.println("✅ Order placed successfully! Order ID: " + orderId);

        } catch (Exception e) {
            System.out.println("❌ Error taking order: " + e.getMessage());
        }
    }
}
