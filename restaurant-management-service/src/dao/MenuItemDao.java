package dao;

import model.MenuItem;

import java.util.List;

public interface MenuItemDao {
    List<MenuItem> getAllMenuItems();

    MenuItem getMenuItemById(int id);

    int insertMenuItem(MenuItem item);

    void updateMenuItemPrice(int id, double newPrice);
}
