package taxi.domain.models;

public class Ride {
    private final int id;
    private final Passenger passenger;
    private final Driver driver;
    private final Location pickup;
    private final Location destination;
    private final double fare;
    private final PaymentMethod paymentMethod; // optional
    private final boolean childSeat;           // optional
    private RideStatus status;

    private Ride(Builder b) {
        this.id = b.id;
        this.passenger = b.passenger;
        this.driver = b.driver;
        this.pickup = b.pickup;
        this.destination = b.destination;
        this.fare = b.fare;
        this.paymentMethod = b.paymentMethod;
        this.childSeat = b.childSeat;
        this.status = RideStatus.ACCEPTED;
    }

    public static Builder builder() {
        return new Builder();
    }

    public int getId() { return id; }
    public Passenger getPassenger() { return passenger; }
    public Driver getDriver() { return driver; }
    public double getFare() { return fare; }
    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public boolean hasChildSeat() { return childSeat; }
    public RideStatus getStatus() { return status; }
    public void setStatus(RideStatus status) { this.status = status; }

    @Override
    public String toString() {
        return String.format("Ride #%d | %s -> %s | driver: %s | fare: %.2f MDL | %s%s | %s",
                id, pickup, destination, driver.getName(), fare, paymentMethod,
                childSeat ? " | child seat" : "", status);
    }

    // ---------- Builder ----------
    public static class Builder {
        private int id;
        private Passenger passenger;
        private Driver driver;
        private Location pickup;
        private Location destination;
        private double fare;
        private PaymentMethod paymentMethod = PaymentMethod.CASH; // default
        private boolean childSeat = false;                        // default

        public Builder id(int id) { this.id = id; return this; }
        public Builder passenger(Passenger passenger) { this.passenger = passenger; return this; }
        public Builder driver(Driver driver) { this.driver = driver; return this; }
        public Builder pickup(Location pickup) { this.pickup = pickup; return this; }
        public Builder destination(Location destination) { this.destination = destination; return this; }
        public Builder fare(double fare) { this.fare = fare; return this; }
        public Builder paymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; return this; }
        public Builder childSeat(boolean childSeat) { this.childSeat = childSeat; return this; }

        public Ride build() {
            if (passenger == null || driver == null || pickup == null || destination == null) {
                throw new IllegalStateException("Ride needs passenger, driver, pickup and destination");
            }
            if (fare < 0) {
                throw new IllegalStateException("Fare cannot be negative");
            }
            return new Ride(this);
        }
    }
}