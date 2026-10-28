package taxi.model;

public class Ride {
    private final int id;
    private final Passenger passenger;
    private final Driver driver;
    private final Location pickup;
    private final Location destination;
    private final double fare;
    private RideStatus status;

    public Ride(int id, Passenger passenger, Driver driver,
                Location pickup, Location destination, double fare) {
        this.id = id;
        this.passenger = passenger;
        this.driver = driver;
        this.pickup = pickup;
        this.destination = destination;
        this.fare = fare;
        this.status = RideStatus.ACCEPTED;
    }

    public int getId() { return id; }
    public Passenger getPassenger() { return passenger; }
    public Driver getDriver() { return driver; }
    public double getFare() { return fare; }
    public RideStatus getStatus() { return status; }
    public void setStatus(RideStatus status) { this.status = status; }

    @Override
    public String toString() {
        return String.format("Ride #%d | %s -> %s | driver: %s | fare: %.2f MDL | %s",
                id, pickup, destination, driver.getName(), fare, status);
    }
}