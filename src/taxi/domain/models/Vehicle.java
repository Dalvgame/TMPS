package taxi.domain.models;

public abstract class Vehicle {
    private final String model;

    protected Vehicle(String model) {
        this.model = model;
    }

    public String getModel() { return model; }

    public abstract String getCategory();

    public abstract int getSeats();

    @Override
    public String toString() {
        return getCategory() + " " + model + " (" + getSeats() + " seats)";
    }
}