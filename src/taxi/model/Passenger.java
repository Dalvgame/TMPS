package taxi.model;

public class Passenger {
    private final int id;
    private final String name;

    public Passenger(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() { return id; }
    public String getName() { return name; }
}