package taxi.patterns.decorator;

import taxi.domain.models.Location;
import taxi.pricing.FareCalculator;

public class BookingFeeDecorator extends FareCalculatorDecorator {
    private final double fee;

    public BookingFeeDecorator(FareCalculator wrapped, double fee) {
        super(wrapped);
        this.fee = fee;
    }

    @Override
    public double calculate(Location from, Location to) {
        return wrapped.calculate(from, to) + fee;
    }
}