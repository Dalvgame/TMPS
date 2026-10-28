package taxi.domain.models;

public class MinivanCar extends Vehicle {
    public MinivanCar(String model) { super(model); }

    @Override
    public String getCategory() { return "Minivan"; }

    @Override
    public int getSeats() { return 6; }
}