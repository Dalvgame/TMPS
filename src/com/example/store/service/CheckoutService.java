package com.example.store.service;

import com.example.store.discount.DiscountPolicy;
import com.example.store.model.Cart;
import com.example.store.model.Order;
import com.example.store.repository.OrderRepository;

import java.math.BigDecimal;
import java.util.UUID;

// SRP: this class is responsible only for completing checkout.
// DIP: it depends on the OrderRepository interface, not its implementation.
public class CheckoutService {
    private final OrderRepository orderRepository;

    public CheckoutService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order checkout(
            String customerName,
            Cart cart,
            DiscountPolicy discountPolicy
    ) {
        if (customerName == null || customerName.trim().isEmpty()
                || cart == null || discountPolicy == null) {
            throw new IllegalArgumentException("Checkout arguments cannot be null.");
        }
        if (cart.isEmpty()) {
            throw new IllegalStateException("Cannot checkout an empty cart.");
        }

        BigDecimal subtotal = cart.calculateSubtotal();
        BigDecimal discount = discountPolicy.calculate(subtotal);
        if (discount.compareTo(BigDecimal.ZERO) < 0 || discount.compareTo(subtotal) > 0) {
            throw new IllegalStateException("The discount policy returned an invalid value.");
        }

        Order order = new Order(
                UUID.randomUUID().toString(),
                customerName,
                subtotal,
                discount,
                discountPolicy.getDescription()
        );

        orderRepository.save(order);
        cart.clear();
        return order;
    }
}
