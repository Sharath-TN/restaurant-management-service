package dao.impl;

import dao.MenuItemDao;
import model.MenuItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MenuItemDaoImpl implements MenuItemDao {
    private final Connection connection;

    public MenuItemDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<MenuItem> getAllMenuItems() {
        List<MenuItem> list = new ArrayList<>();
        String sql = "SELECT id, name, price FROM menu";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                MenuItem item = new MenuItem(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("price")
                );
                list.add(item);
            }

        } catch (SQLException e) {
            System.out.println("Error fetching menu: " + e.getMessage());
        }

        return list;
    }

    @Override
    public MenuItem getMenuItemById(int id) {
        String sql = "SELECT * FROM menu WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new MenuItem(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("price")
                );
            }

        } catch (SQLException e) {
            System.out.println("Error fetching menu item: " + e.getMessage());
        }

        return null;
    }

    @Override
    public int insertMenuItem(MenuItem item) {
        String sql = "INSERT INTO menu (name, price) VALUES (?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, item.getName());
            stmt.setDouble(2, item.getPrice());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }

        } catch (SQLException e) {
            System.out.println("Error inserting menu item: " + e.getMessage());
        }
        return -1;
    }

    @Override
    public void updateMenuItemPrice(int id, double newPrice) {
        String sql = "UPDATE menu SET price = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setDouble(1, newPrice);
            stmt.setInt(2, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error updating menu price: " + e.getMessage());
        }
    }
}
