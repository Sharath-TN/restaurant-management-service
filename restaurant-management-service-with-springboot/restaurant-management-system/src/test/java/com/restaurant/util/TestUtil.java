package com.restaurant.util;

import com.restaurant.dto.MenuItemDTO;
import com.restaurant.entity.MenuItem;

public class TestUtil {
    public static final Long TEST_MENU_ITEM_ID = 1L;
    public static final String TEST_MENU_ITEM_NAME = "Test Menu Item";
    public static final double TEST_MENU_ITEM_PRICE = 9.99;

    public static MenuItem getMenuItem() {
        return MenuItem.builder()
                .id(TEST_MENU_ITEM_ID)
                .name(TEST_MENU_ITEM_NAME)
                .price(TEST_MENU_ITEM_PRICE)
                .build();
    }

    public static MenuItemDTO getMenuItemDTO() {
        return new MenuItemDTO() {{
            setName(TEST_MENU_ITEM_NAME);
            setPrice(TEST_MENU_ITEM_PRICE);
        }};
    }
}
