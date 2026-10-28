package taxi.model;

public class Location {
    private final String name;
    private final double x;
    private final double y;

    public Location(String name, double x, double y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public double distanceTo(Location other) {
        double dx = x - other.x;
        double dy = y - other.y;
        return Math.sqrt(dx * dx + dy * dy); // treated as km
    }

    public String getName() { return name; }

    @Override
    public String toString() { return name; }
}