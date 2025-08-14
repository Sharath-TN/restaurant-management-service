package com.restaurant.service.impl;

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
import com.restaurant.service.OrderService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    private final MenuItemRepository menuItemRepository;

    private final OrderItemRepository orderItemRepository;

    private final TableBookingRepository tableBookingRepository;

    private final BillRepository billRepository;

    public OrderServiceImpl(OrderRepository orderRepository, MenuItemRepository menuItemRepository, OrderItemRepository orderItemRepository, TableBookingRepository tableBookingRepository, BillRepository billRepository) {
        this.orderRepository = orderRepository;
        this.menuItemRepository = menuItemRepository;
        this.orderItemRepository = orderItemRepository;
        this.tableBookingRepository = tableBookingRepository;
        this.billRepository = billRepository;
    }

    @Transactional
    @Override
    public Order placeOrUpdateOrder(OrderRequest request) {
        log.debug("Received request to place or update order: {}", request);
        TableBooking booking = tableBookingRepository.findById(request.getTableBookingId())
                .orElseThrow(() -> {
                    log.error("Table booking with ID {} not found", request.getTableBookingId());
                    return new RuntimeException("Booking not found");
                });

        if(booking.getBookingStatus()!= BookingStatus.CONFIRMED){
            log.error("Booking with ID {} is not confirmed", booking.getId());
            throw new RuntimeException("Booking is not confirmed, cannot place order");
        }

        Order order = orderRepository.findByTableBookingId(booking.getId())
                .orElseGet(() -> {
                    log.info("Creating new order for booking ID: {}", booking.getId());
                    Order newOrder = new Order();
                    newOrder.setTableBooking(booking);
                    newOrder.setStatus(OrderStatus.PLACED);
                    return orderRepository.save(newOrder);
                });

        for (OrderItemRequest itemReq : request.getItems()) {
            MenuItem menuItem = menuItemRepository.findById(itemReq.getMenuItemId())
                    .orElseThrow(() -> {
                        log.error("Menu item with ID {} not found", itemReq.getMenuItemId());
                        return new RuntimeException("Menu item not found");
                    });

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setMenuItem(menuItem);
            orderItem.setStatus(OrderItemStatus.PENDING);
            orderItem.setQuantity(itemReq.getQuantity());

            order.getOrderItems().add(orderItem);
        }
        log.info("Saving order with ID: {}", order.getId());
        return orderRepository.save(order);
    }

    @Override
    public void updateOrderItemStatus(Long orderItemId, OrderItemStatus status) {
        log.debug("Received request to update order item status: orderItemId={}, status={}", orderItemId, status);
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> {
                    log.error("Order item with ID {} not found", orderItemId);
                    return new RuntimeException("Order item not found");
                });

        orderItem.setStatus(status);
        log.info("Updating order item with ID: {} to status: {}", orderItemId, status);
        orderItemRepository.save(orderItem);
    }

    @Transactional
    @Override
    public BillResponseDTO generateBill(Long orderId) {
        log.debug("Received request to generate bill for order ID: {}", orderId);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> {
                    log.error("Order with ID {} not found", orderId);
                    return new RuntimeException("Order not found with ID: " + orderId);
                });

        if(order.getStatus() == OrderStatus.COMPLETED) {
            log.error("Bill already generated for order ID: {}", orderId);
            throw new RuntimeException("Bill already generated for this order.");
        }

        boolean allCompleted = order.getOrderItems().stream()
                .allMatch(item -> item.getStatus() != OrderItemStatus.PENDING);

        if (!allCompleted) {
            log.error("Cannot generate bill for order ID: {} — some items are still pending", orderId);
            throw new RuntimeException("Cannot generate bill — some items are still pending.");
        }

        double totalAmount = order.getOrderItems().stream().filter(item->item.getStatus()==OrderItemStatus.PREPARED)
                .mapToDouble(item -> item.getMenuItem().getPrice() * item.getQuantity())
                .sum();

        TableBooking booking = order.getTableBooking();
        booking.setBookingStatus(BookingStatus.COMPLETED);
        log.info("Updating booking status to COMPLETED for booking ID: {}", booking.getId());
        tableBookingRepository.save(booking);

        Bill bill = new Bill();
        bill.setOrder(order);
        bill.setTotalAmount(totalAmount);
        bill.setGeneratedAt(LocalDateTime.now());
        log.info("Generating bill for order ID: {} with total amount: {}", orderId, totalAmount);
        billRepository.save(bill);

        order.setStatus(OrderStatus.COMPLETED);
        log.info("Updating order status to COMPLETED for order ID: {}", orderId);
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

        log.info("Bill generated successfully for order ID: {}", orderId);
        return response;
    }
}

