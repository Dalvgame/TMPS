package taxi.model;

public class Driver {
    private final int id;
    private final String name;
    private final String carModel;
    private Location location;
    private boolean available = true;

    public Driver(int id, String name, String carModel, Location location) {
        this.id = id;
        this.name = name;
        this.carModel = carModel;
        this.location = location;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getCarModel() { return carModel; }
    public Location getLocation() { return location; }
    public void setLocation(Location location) { this.location = location; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
}