package service.impl;

import dao.MenuItemDao;
import dao.PaymentDao;
import model.MenuItem;
import service.AdminService;

import java.util.List;
import java.util.Scanner;

public class AdminServiceImpl implements AdminService {
    private final MenuItemDao menuDao;
    private final PaymentDao paymentDao;
    private final Scanner scanner;

    public AdminServiceImpl(MenuItemDao menuDao, PaymentDao paymentDao, Scanner scanner) {
        this.menuDao = menuDao;
        this.paymentDao = paymentDao;
        this.scanner = scanner;
    }

    @Override
    public void adminMenu() {
        while (true) {
            System.out.println("\n--- Admin Panel ---");
            System.out.println("1. Add Menu Item");
            System.out.println("2. Update Menu Item Price");
            System.out.println("3. View Today's Sales Report");
            System.out.println("0. Back");
            System.out.print("Enter choice: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    addMenuItem(scanner);
                    break;
                case "2":
                    updateMenuItemPrice(scanner);
                    break;
                case "3":
                    viewSalesReport();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    private void addMenuItem(Scanner scanner) {
        System.out.print("Enter item name: ");
        String name = scanner.nextLine();

        System.out.print("Enter price: ");
        double price = Double.parseDouble(scanner.nextLine());

        MenuItem item = new MenuItem(name, price);
        menuDao.insertMenuItem(item);
        System.out.println("✅ Item added successfully.");
    }

    private void updateMenuItemPrice(Scanner scanner) {
        List<MenuItem> menuItems = menuDao.getAllMenuItems();

        if (menuItems.isEmpty()) {
            System.out.println("⚠️ No menu items found.");
            return;
        }

        System.out.println("Available Menu Items:");
        for (MenuItem item : menuItems) {
            System.out.println(item.getId() + ". " + item.getName() + " - ₹" + item.getPrice());
        }

        System.out.print("Enter item ID to update: ");
        int id = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter new price: ");
        double newPrice = Double.parseDouble(scanner.nextLine());

        menuDao.updateMenuItemPrice(id, newPrice);
        System.out.println("✅ Price updated.");
    }

    private void viewSalesReport() {
        double totalSales = paymentDao.getTodaySales();
        System.out.println("📈 Total Sales Today: ₹" + totalSales);
    }
}
