package com.example.store.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Cart {
    private final Map<String, CartItem> items = new LinkedHashMap<String, CartItem>();

    public void addProduct(Product product, int quantity) {
        if (product == null) {
            throw new IllegalArgumentException("Product is required.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive.");
        }
        CartItem existingItem = items.get(product.getId());
        if (existingItem == null) {
            items.put(product.getId(), new CartItem(product, quantity));
        } else {
            existingItem.increaseQuantity(quantity);
        }
    }

    public List<CartItem> getItems() {
        return Collections.unmodifiableList(new ArrayList<CartItem>(items.values()));
    }

    public BigDecimal calculateSubtotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem item : items.values()) {
            total = total.add(item.getSubtotal());
        }
        return total.setScale(2, RoundingMode.HALF_UP);
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public void clear() {
        items.clear();
    }
}
