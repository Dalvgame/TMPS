package com.example.store.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Product {
    private final String id;
    private final String name;
    private final BigDecimal price;

    public Product(String id, String name, BigDecimal price) {
        if (isBlank(id) || isBlank(name)) {
            throw new IllegalArgumentException("Product fields cannot be blank.");
        }
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price cannot be negative.");
        }
        this.id = id;
        this.name = name;
        this.price = price.setScale(2, RoundingMode.HALF_UP);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
