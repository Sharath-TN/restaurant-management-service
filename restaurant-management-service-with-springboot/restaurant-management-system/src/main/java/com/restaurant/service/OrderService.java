package com.restaurant.service;

import com.restaurant.dto.BillResponseDTO;
import com.restaurant.dto.OrderItemRequest;
import com.restaurant.dto.OrderItemResponseDTO;
import com.restaurant.dto.OrderRequest;
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
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    private final MenuItemRepository menuItemRepository;

    private final OrderItemRepository orderItemRepository;

    private final TableBookingRepository tableBookingRepository;

    private final BillRepository billRepository;

    public OrderService(OrderRepository orderRepository, MenuItemRepository menuItemRepository, OrderItemRepository orderItemRepository, TableBookingRepository tableBookingRepository, BillRepository billRepository) {
        this.orderRepository = orderRepository;
        this.menuItemRepository = menuItemRepository;
        this.orderItemRepository = orderItemRepository;
        this.tableBookingRepository = tableBookingRepository;
        this.billRepository = billRepository;
    }

    @Transactional
    public Order placeOrUpdateOrder(OrderRequest request) {
        TableBooking booking = tableBookingRepository.findById(request.getTableBookingId())
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if(booking.getBookingStatus()!= BookingStatus.CONFIRMED){
            throw new RuntimeException("Booking is not confirmed, cannot place order");
        }

        Order order = orderRepository.findByTableBookingId(booking.getId())
                .orElseGet(() -> {
                    Order newOrder = new Order();
                    newOrder.setTableBooking(booking);
                    newOrder.setStatus(OrderStatus.PLACED);
                    return orderRepository.save(newOrder);
                });

        for (OrderItemRequest itemReq : request.getItems()) {
            MenuItem menuItem = menuItemRepository.findById(itemReq.getMenuItemId())
                    .orElseThrow(() -> new RuntimeException("Menu item not found"));

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setMenuItem(menuItem);
            orderItem.setStatus(OrderItemStatus.PENDING);
            orderItem.setQuantity(itemReq.getQuantity());

            order.getOrderItems().add(orderItem);
        }

        return orderRepository.save(order);
    }

    public void updateOrderItemStatus(Long orderItemId, OrderItemStatus status) {
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new RuntimeException("Order item not found"));

        orderItem.setStatus(status);
        orderItemRepository.save(orderItem);
    }

    @Transactional
    public BillResponseDTO generateBill(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));

        if(order.getStatus() == OrderStatus.COMPLETED) {
            throw new RuntimeException("Bill already generated for this order.");
        }

        boolean allCompleted = order.getOrderItems().stream()
                .allMatch(item -> item.getStatus() != OrderItemStatus.PENDING);

        if (!allCompleted) {
            throw new RuntimeException("Cannot generate bill — some items are still pending.");
        }

        double totalAmount = order.getOrderItems().stream().filter(item->item.getStatus()==OrderItemStatus.PREPARED)
                .mapToDouble(item -> item.getMenuItem().getPrice() * item.getQuantity())
                .sum();

        TableBooking booking = order.getTableBooking();
        booking.setBookingStatus(BookingStatus.COMPLETED);
        tableBookingRepository.save(booking);

        Bill bill = new Bill();
        bill.setOrder(order);
        bill.setTotalAmount(totalAmount);
        bill.setGeneratedAt(LocalDateTime.now());
        billRepository.save(bill);

        order.setStatus(OrderStatus.COMPLETED);
        orderRepository.save(order);

        List<OrderItemResponseDTO> items = order.getOrderItems().stream().filter(item->item.getStatus()==OrderItemStatus.PREPARED)
                .map(item -> {
            OrderItemResponseDTO dto = new OrderItemResponseDTO();
            dto.setMenuItemName(item.getMenuItem().getName());
            dto.setQuantity(item.getQuantity());
            dto.setPricePerUnit(item.getMenuItem().getPrice());
            dto.setTotalPrice(item.getMenuItem().getPrice() * item.getQuantity());
            return dto;
        }).collect(Collectors.toList());

        BillResponseDTO response = new BillResponseDTO();
        response.setOrderId(orderId);
        response.setTotalAmount(totalAmount);
        response.setGeneratedAt(bill.getGeneratedAt());
        response.setOrderItems(items);

        return response;
    }
}

