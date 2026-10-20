package com.example.store;

import com.example.store.discount.DiscountPolicy;
import com.example.store.discount.PercentageDiscount;
import com.example.store.model.Cart;
import com.example.store.model.CartItem;
import com.example.store.model.Order;
import com.example.store.model.Product;
import com.example.store.repository.InMemoryOrderRepository;
import com.example.store.repository.OrderRepository;
import com.example.store.service.CheckoutService;

import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {
        Product keyboard = new Product(
                "P-100", "Mechanical Keyboard", new BigDecimal("349.90")
        );
        Product mouse = new Product(
                "P-200", "Wireless Mouse", new BigDecimal("129.50")
        );
        Product fan = new Product(
                "P-300", "Fan", new BigDecimal("50.75")
        );

        Cart cart = new Cart();
        cart.addProduct(keyboard, 1);
        cart.addProduct(mouse, 2);
        cart.addProduct(fan, 1);
        printCart(cart);

        DiscountPolicy discount = new PercentageDiscount(new BigDecimal("10"));
        OrderRepository orderRepository = new InMemoryOrderRepository();
        CheckoutService checkoutService = new CheckoutService(orderRepository);

        Order order = checkoutService.checkout("VLADUT", cart, discount);
        printOrder(order);
        System.out.println("Saved orders: " + orderRepository.findAll().size());
    }

    private static void printCart(Cart cart) {
        System.out.println("\n=== SHOPPING CART ===");
        for (CartItem item : cart.getItems()) {
            System.out.printf(
                    "%-22s x %d = %.2f MDL%n",
                    item.getProduct().getName(), item.getQuantity(), item.getSubtotal()
            );
        }
        System.out.printf("Subtotal: %.2f MDL%n", cart.calculateSubtotal());
    }

    private static void printOrder(Order order) {
        System.out.println("\n=== ORDER CONFIRMATION ===");
        System.out.println("Order: " + order.getId());
        System.out.println("Customer: " + order.getCustomerName());
        System.out.printf("Subtotal: %.2f MDL%n", order.getSubtotal());
        System.out.printf("Discount: %.2f MDL (%s)%n",
                order.getDiscount(), order.getDiscountDescription());
        System.out.printf("Total: %.2f MDL%n", order.getTotal());
    }
}
