package com.example.store.discount;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PercentageDiscount implements com.example.store.discount.DiscountPolicy {
    private static final BigDecimal ONE_HUNDRED = new BigDecimal("100");

    private final BigDecimal percentage;

    public PercentageDiscount(BigDecimal percentage) {
        if (percentage == null
                || percentage.compareTo(BigDecimal.ZERO) < 0
                || percentage.compareTo(ONE_HUNDRED) > 0) {
            throw new IllegalArgumentException("Percentage must be between 0 and 100.");
        }
        this.percentage = percentage;
    }

    @Override
    public BigDecimal calculate(BigDecimal subtotal) {
        return subtotal.multiply(percentage)
                .divide(ONE_HUNDRED)
                .setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public String getDescription() {
        return percentage.stripTrailingZeros().toPlainString() + "% discount";
    }
}
