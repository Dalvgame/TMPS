package com.example.store.discount;

import java.math.BigDecimal;

// OCP: new discount types can be added without changing CheckoutService.
public interface DiscountPolicy {
    BigDecimal calculate(BigDecimal subtotal);

    String getDescription();
}
