package taxi.domain.models;

public class PremiumCar extends Vehicle {
    public PremiumCar(String model) { super(model); }

    @Override
    public String getCategory() { return "Premium"; }

    @Override
    public int getSeats() { return 4; }
}