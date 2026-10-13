package com.example.store.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
public class Order {
    private final String id;
    private final String customerName;
    private final BigDecimal subtotal;
    private final BigDecimal discount;
    private final BigDecimal total;
    private final String discountDescription;

    public Order(
            String id,
            String customerName,
            BigDecimal subtotal,
            BigDecimal discount,
            String discountDescription
    ) {
        this.id = id;
        this.customerName = customerName;
        this.subtotal = subtotal;
        this.discount = discount;
        this.total = subtotal.subtract(discount).setScale(2, RoundingMode.HALF_UP);
        this.discountDescription = discountDescription;
    }

    public String getId() {
        return id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public String getDiscountDescription() {
        return discountDescription;
    }

}
