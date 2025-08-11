package service.impl;

import dao.BookingDao;
import dao.MenuItemDao;
import dao.OrderDao;
import dao.OrderItemDao;
import dao.PaymentDao;
import model.MenuItem;
import model.Order;
import model.OrderItem;
import model.Payment;
import service.BillingService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

import static util.Constant.SUPPORTED_PAYMENT_METHODS;

public class BillingServiceImpl implements BillingService {
    private final OrderDao orderDAO;
    private final OrderItemDao orderItemDAO;
    private final MenuItemDao menuDAO;
    private final PaymentDao paymentDAO;
    private final BookingDao bookingDao;
    private final Scanner scanner;

    public BillingServiceImpl(OrderDao orderDAO, OrderItemDao orderItemDAO, MenuItemDao menuDAO, PaymentDao paymentDAO, BookingDao bookingDao, Scanner scanner) {
        this.orderDAO = orderDAO;
        this.orderItemDAO = orderItemDAO;
        this.menuDAO = menuDAO;
        this.paymentDAO = paymentDAO;
        this.bookingDao = bookingDao;
        this.scanner = scanner;
    }

    @Override
    public void generateBill() {
        System.out.println("\n--- Generate Bill & Record Payment ---");

        List<Order> orders = orderDAO.getPreparedOrders();
        if (orders.isEmpty()) {
            System.out.println("⚠️ No prepared orders found.");
            return;
        }

        System.out.println("Prepared Orders:");
        for (Order order : orders) {
            System.out.println("Order ID: " + order.getId() +
                    ", Booking ID: " + order.getBookingId() +
                    ", Waiter: " + order.getWaiterName());
        }

        System.out.print("Enter Order ID to generate bill: ");
        int orderId = Integer.parseInt(scanner.nextLine());

        List<OrderItem> items = orderItemDAO.getOrderItemsByOrderId(orderId);
        double total = 0.0;

        System.out.println("\n--- Bill Details ---");
        for (OrderItem item : items) {
            MenuItem menuItem = menuDAO.getMenuItemById(item.getMenuId());
            double itemTotal = menuItem.getPrice() * item.getQuantity();
            System.out.println(menuItem.getName() + " x " + item.getQuantity() + " = ₹" + itemTotal);
            total += itemTotal;
        }

        System.out.println("Total Amount: ₹" + total);
        System.out.print("Enter payment method (Cash/Card/UPI): ");
        String method = scanner.nextLine();

        if (!SUPPORTED_PAYMENT_METHODS.contains(method)) {
            System.out.println("⚠️ Invalid payment method. Please try again.");
            return;
        }

        Payment payment = new Payment(orderId, total, method);
        payment.setPaymentTime(LocalDateTime.now());
        paymentDAO.insertPayment(payment);

        // Mark order as "Paid"
        orderDAO.updateOrderStatus(orderId, "Paid");
        for (Order order : orders) {
            if(order.getId() == orderId) {
                bookingDao.markBookingAsComplete(order.getBookingId());
                System.out.println("Booking ID " + order.getBookingId() + " marked as complete.");
            }
        }

        System.out.println("✅ Payment recorded. Bill complete.");
    }
}
