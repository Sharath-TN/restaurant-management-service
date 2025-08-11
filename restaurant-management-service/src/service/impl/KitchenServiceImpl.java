package service.impl;

import dao.OrderDao;
import model.Order;
import service.KitchenService;

import java.util.List;
import java.util.Scanner;

public class KitchenServiceImpl implements KitchenService {
    private final OrderDao orderDAO;
    private final Scanner scanner;

    public KitchenServiceImpl(OrderDao orderDAO, Scanner scanner) {
        this.orderDAO = orderDAO;
        this.scanner = scanner;
    }

    @Override
    public void viewAndPrepareOrders() {
        System.out.println("\n--- Kitchen Orders ---");

        List<Order> pendingOrders = orderDAO.getPendingOrders();
        if (pendingOrders.isEmpty()) {
            System.out.println("✅ No pending orders. All caught up!");
            return;
        }

        System.out.println("Pending Orders:");
        for (Order order : pendingOrders) {
            System.out.println("Order ID: " + order.getId() +
                    ", Booking ID: " + order.getBookingId() +
                    ", Waiter: " + order.getWaiterName() +
                    ", Time: " + order.getCreatedAt());
        }

        System.out.print("Enter Order ID to mark as Prepared: ");
        int orderId = Integer.parseInt(scanner.nextLine());

        boolean success = orderDAO.updateOrderStatus(orderId, "Prepared");
        if (success) {
            System.out.println("✅ Order " + orderId + " marked as Prepared.");
        } else {
            System.out.println("❌ Failed to update order status.");
        }
    }
}
