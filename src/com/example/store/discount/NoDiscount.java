package com.example.store.discount;

import java.math.BigDecimal;

public class NoDiscount implements com.example.store.discount.DiscountPolicy {
    @Override
    public BigDecimal calculate(BigDecimal subtotal) {
        return BigDecimal.ZERO.setScale(2);
    }

    @Override
    public String getDescription() {
        return "No discount";
    }
}
