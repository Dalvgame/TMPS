package taxi.domain.models;

public class Driver {
    private final int id;
    private final String name;
    private final Vehicle vehicle;
    private Location location;
    private boolean available = true;

    public Driver(int id, String name, Vehicle vehicle, Location location) {
        this.id = id;
        this.name = name;
        this.vehicle = vehicle;
        this.location = location;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public Vehicle getVehicle() { return vehicle; }
    public Location getLocation() { return location; }
    public void setLocation(Location location) { this.location = location; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
}