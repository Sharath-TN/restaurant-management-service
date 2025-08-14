package com.restaurant.service.impl;

import com.restaurant.dto.BillResponseDTO;
import com.restaurant.entity.Bill;
import com.restaurant.entity.MenuItem;
import com.restaurant.entity.Order;
import com.restaurant.entity.OrderItem;
import com.restaurant.entity.TableBooking;
import com.restaurant.enumeration.BookingStatus;
import com.restaurant.enumeration.OrderItemStatus;
import com.restaurant.enumeration.OrderStatus;
import com.restaurant.repository.BillRepository;
import com.restaurant.repository.MenuItemRepository;
import com.restaurant.repository.OrderItemRepository;
import com.restaurant.repository.OrderRepository;
import com.restaurant.repository.TableBookingRepository;
import com.restaurant.service.OrderService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.restaurant.util.TestUtil.TEST_MENU_ITEM_ID;
import static com.restaurant.util.TestUtil.TEST_ORDER_ID;
import static com.restaurant.util.TestUtil.TEST_ORDER_ITEM_ID;
import static com.restaurant.util.TestUtil.TEST_ORDER_ITEM_STATUS;
import static com.restaurant.util.TestUtil.TEST_TABLE_BOOKING_ID;
import static com.restaurant.util.TestUtil.getMenuItem;
import static com.restaurant.util.TestUtil.getOrder;
import static com.restaurant.util.TestUtil.getOrderItem;
import static com.restaurant.util.TestUtil.getOrderRequest;
import static com.restaurant.util.TestUtil.getTableBooking;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class OrderServiceImplTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private MenuItemRepository menuItemRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private TableBookingRepository tableBookingRepository;

    @Mock
    private BillRepository billRepository;

    private OrderService orderService;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = openMocks(this);
        orderService = new OrderServiceImpl(orderRepository, menuItemRepository, orderItemRepository, tableBookingRepository, billRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    public void testSuccessfulPlaceOrUpdateOrder() {
        doReturn(Optional.of(getTableBooking()))
                .when(tableBookingRepository)
                .findById(any());
        doReturn(Optional.of(getMenuItem()))
                .when(menuItemRepository)
                .findById(any());
        doReturn(getOrder())
                .when(orderRepository)
                .save(any());
        doReturn(Optional.empty())
                .when(orderRepository)
                .findByTableBookingId(any());

        Order order = orderService.placeOrUpdateOrder(getOrderRequest());

        assertNotNull(order);
        assertEquals(TEST_ORDER_ID, order.getId());
        assertEquals(TEST_MENU_ITEM_ID, order.getOrderItems().get(0).getMenuItem().getId());
        assertEquals(2L, order.getOrderItems().get(0).getQuantity());
        assertEquals(1, order.getOrderItems().size());
        verify(tableBookingRepository).findById(TEST_TABLE_BOOKING_ID);
        verify(orderRepository).findByTableBookingId(TEST_TABLE_BOOKING_ID);
        verify(menuItemRepository).findById(TEST_MENU_ITEM_ID);
        verify(orderRepository, times(2)).save(any(Order.class));
        verifyNoMoreInteractions(tableBookingRepository, menuItemRepository, orderRepository);
    }

    @Test
    public void testFailurePlaceOrUpdateOrderDueToBookingNotConfirmed() {
        TableBooking booking = getTableBooking();
        booking.setBookingStatus(BookingStatus.PENDING);
        doReturn(Optional.of(booking))
                .when(tableBookingRepository)
                .findById(TEST_TABLE_BOOKING_ID);

        try {
            orderService.placeOrUpdateOrder(getOrderRequest());
            fail("Expected RuntimeException due to booking not confirmed");
        } catch (RuntimeException e) {
            assertEquals("Booking is not confirmed, cannot place order", e.getMessage());
            verify(tableBookingRepository).findById(TEST_TABLE_BOOKING_ID);
            verifyNoMoreInteractions(tableBookingRepository, menuItemRepository, orderRepository);
        }
    }

    @Test
    public void testSuccessfulUpdateOrderItemStatus() {
        doReturn(Optional.of(getOrderItem()))
                .when(orderItemRepository)
                .findById(any());
        doReturn(getOrderItem())
                .when(orderItemRepository)
                .save(any());

        orderService.updateOrderItemStatus(TEST_ORDER_ITEM_ID, TEST_ORDER_ITEM_STATUS);

        verify(orderItemRepository).findById(TEST_ORDER_ITEM_ID);
    }

    @Test
    public void testFailureUpdateOrderItemStatusDueToOrderItemNotFound() {
        doReturn(Optional.empty())
                .when(orderItemRepository)
                .findById(TEST_ORDER_ITEM_ID);

        try {
            orderService.updateOrderItemStatus(TEST_ORDER_ITEM_ID, TEST_ORDER_ITEM_STATUS);
            fail("Expected RuntimeException due to order item not found");
        } catch (RuntimeException e) {
            assertEquals("Order item not found", e.getMessage());
            verify(orderItemRepository).findById(TEST_ORDER_ITEM_ID);
            verifyNoMoreInteractions(orderItemRepository);
        }
    }

    @Test
    void generateBillSuccessfully() {
        Long orderId = 1L;

        MenuItem menuItem = new MenuItem();
        menuItem.setId(1L);
        menuItem.setName("Pizza");
        menuItem.setPrice(15.0);

        TableBooking booking = new TableBooking();
        booking.setId(2L);
        booking.setBookingStatus(BookingStatus.CONFIRMED);

        OrderItem orderItem = new OrderItem();
        orderItem.setId(10L);
        orderItem.setMenuItem(menuItem);
        orderItem.setQuantity(2L);
        orderItem.setStatus(OrderItemStatus.PREPARED);

        Order order = new Order();
        order.setId(orderId);
        order.setTableBooking(booking);
        order.setOrderItems(new ArrayList<>(List.of(orderItem)));
        order.setStatus(OrderStatus.PLACED);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(tableBookingRepository.save(any(TableBooking.class))).thenReturn(booking);
        when(billRepository.save(any(Bill.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        BillResponseDTO response = orderService.generateBill(orderId);

        verify(orderRepository).findById(orderId);
        verify(tableBookingRepository).save(booking);
        verify(billRepository).save(any(Bill.class));
        verify(orderRepository).save(order);

        assertNotNull(response);
        assertEquals(orderId, response.getOrderId());
        assertEquals(30.0, response.getTotalAmount());
        assertEquals(1, response.getOrderItems().size());
        assertEquals("Pizza", response.getOrderItems().get(0).getMenuItemName());
    }
}