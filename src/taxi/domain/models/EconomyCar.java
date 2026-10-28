package taxi.domain.models;

public class EconomyCar extends Vehicle {
    public EconomyCar(String model) { super(model); }

    @Override
    public String getCategory() { return "Economy"; }

    @Override
    public int getSeats() { return 4; }
}