package taxi.patterns.decorator;

import taxi.domain.models.Location;
import taxi.pricing.FareCalculator;

public class DiscountDecorator extends FareCalculatorDecorator {
    private final double percent;

    public DiscountDecorator(FareCalculator wrapped, double percent) {
        super(wrapped);
        if (percent < 0 || percent > 100) {
            throw new IllegalArgumentException("Discount must be between 0 and 100");
        }
        this.percent = percent;
    }

    @Override
    public double calculate(Location from, Location to) {
        return wrapped.calculate(from, to) * (1 - percent / 100.0);
    }
}