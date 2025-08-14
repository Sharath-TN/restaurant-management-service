package com.restaurant.util;

import com.restaurant.dto.MenuItemDTO;
import com.restaurant.dto.OrderItemRequest;
import com.restaurant.dto.OrderRequest;
import com.restaurant.dto.TableBookingDTO;
import com.restaurant.entity.MenuItem;
import com.restaurant.entity.Order;
import com.restaurant.entity.OrderItem;
import com.restaurant.entity.TableBooking;
import com.restaurant.enumeration.BookingStatus;
import com.restaurant.enumeration.OrderItemStatus;
import com.restaurant.enumeration.OrderStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TestUtil {
    public static final Long TEST_MENU_ITEM_ID = 1L;
    public static final String TEST_MENU_ITEM_NAME = "Test Menu Item";
    public static final double TEST_MENU_ITEM_PRICE = 9.99;
    public static final Long TEST_ORDER_ID = 100L;
    public static final OrderStatus TEST_ORDER_STATUS = OrderStatus.PLACED;
    public static final Long TEST_ORDER_ITEM_ID = 200L;
    public static final Long TEST_ORDER_ITEM_QUANITY = 2L;
    public static final OrderItemStatus TEST_ORDER_ITEM_STATUS = OrderItemStatus.PENDING;
    public static final Long TEST_TABLE_BOOKING_ID = 1L;
    public static final String TEST_CUSTOMER_NAME = "John Doe";
    public static final Long TEST_PHONE_NUMBER = 1234567890L;
    public static final LocalDateTime TEST_BOOKING_DATE = LocalDateTime.of(2023, 10, 1, 18, 30);
    public static final Long TEST_NUMBER_OF_GUESTS = 4L;
    public static final BookingStatus TEST_BOOKING_STATUS = BookingStatus.CONFIRMED;

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

    public static TableBooking getTableBooking() {
        return TableBooking.builder()
                .id(TEST_TABLE_BOOKING_ID)
                .customerName(TEST_CUSTOMER_NAME)
                .phoneNumber(TEST_PHONE_NUMBER)
                .bookingDate(TEST_BOOKING_DATE)
                .numberOfGuests(TEST_NUMBER_OF_GUESTS)
                .bookingStatus(TEST_BOOKING_STATUS)
                .build();
    }

    public static OrderItem getOrderItem() {
        return new OrderItem() {{
            setId(TEST_ORDER_ITEM_ID);
            setMenuItem(getMenuItem());
            setQuantity(TEST_ORDER_ITEM_QUANITY);
            setStatus(TEST_ORDER_ITEM_STATUS);
        }};
    }

    public static Order getOrder() {
        return new Order() {{
            setId(TEST_ORDER_ID);
            setTableBooking(getTableBooking());
            setStatus(TEST_ORDER_STATUS);
        }};
    }

    public static OrderItemRequest getOrderItemRequest() {
        return new OrderItemRequest() {{
            setMenuItemId(TEST_MENU_ITEM_ID);
            setQuantity(TEST_ORDER_ITEM_QUANITY);
        }};
    }

    public static OrderRequest getOrderRequest() {
        return new OrderRequest() {{
            setTableBookingId(1L); // Assuming a valid table booking ID
            setItems(List.of(getOrderItemRequest()));
        }};
    }

    public static TableBookingDTO getTableBookingDTO() {
        return new TableBookingDTO() {{
            setCustomerName(TEST_CUSTOMER_NAME);
            setPhoneNumber(TEST_PHONE_NUMBER);
            setBookingDate(TEST_BOOKING_DATE);
            setNumberOfGuests(TEST_NUMBER_OF_GUESTS);
        }};
    }
}
