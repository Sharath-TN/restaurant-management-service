import config.DatabaseConnection;
import dao.BookingDao;
import dao.CustomerDao;
import dao.MenuItemDao;
import dao.OrderDao;
import dao.OrderItemDao;
import dao.PaymentDao;
import dao.impl.BookingDaoImpl;
import dao.impl.CustomerDaoImpl;
import dao.impl.MenuItemDaoImpl;
import dao.impl.OrderDaoImpl;
import dao.impl.OrderItemDaoImpl;
import dao.impl.PaymentDaoImpl;
import service.AdminService;
import service.BillingService;
import service.BookingService;
import service.KitchenService;
import service.OrderService;
import service.impl.AdminServiceImpl;
import service.impl.BillingServiceImpl;
import service.impl.BookingServiceImpl;
import service.impl.KitchenServiceImpl;
import service.impl.OrderServiceImpl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Connection connection = null;
        try {
            connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        BookingDao bookingDao = new BookingDaoImpl(connection);
        CustomerDao customerDao = new CustomerDaoImpl(connection);
        MenuItemDao menuItemDao = new MenuItemDaoImpl(connection);
        OrderDao orderDao = new OrderDaoImpl(connection);
        OrderItemDao orderItemDao = new OrderItemDaoImpl(connection);
        PaymentDao paymentDao = new PaymentDaoImpl(connection);

        Scanner scanner = new Scanner(System.in);
        AdminService adminService = new AdminServiceImpl(menuItemDao, paymentDao, scanner);
        BookingService bookingService = new BookingServiceImpl(customerDao, bookingDao, scanner);
        BillingService billingService = new BillingServiceImpl(orderDao, orderItemDao, menuItemDao, paymentDao, bookingDao, scanner);
        KitchenService kitchenService = new KitchenServiceImpl(orderDao, scanner);
        OrderService orderService = new OrderServiceImpl(menuItemDao, orderDao, orderItemDao, bookingDao, scanner);


        while (true) {
            System.out.println("\n===============================");
            System.out.println(" Welcome to Restaurant System");
            System.out.println("===============================");
            System.out.println("Select Role:");
            System.out.println("1. Customer - Book Table");
            System.out.println("2. Waiter - Take Order");
            System.out.println("3. Kitchen - View/Prepare Orders");
            System.out.println("4. Manager - Generate Bill");
            System.out.println("5. Admin - Manage Menu / View Reports");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    bookingService.bookTable();
                    break;
                case "2":
                    orderService.takeOrder();
                    break;
                case "3":
                    kitchenService.viewAndPrepareOrders();
                    break;
                case "4":
                    billingService.generateBill();
                    break;
                case "5":
                    adminService.adminMenu();
                    break;
                case "0":
                    System.out.println("Thank you for using the Restaurant System!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please select again.");
            }
        }
    }
}