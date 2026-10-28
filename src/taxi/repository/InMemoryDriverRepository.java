package taxi.repository;

import taxi.domain.models.Driver;
import java.util.ArrayList;
import java.util.List;

public class InMemoryDriverRepository implements DriverRepository {

    private static InMemoryDriverRepository instance;

    private final List<Driver> drivers = new ArrayList<>();

    // private constructor: nobody else can call "new"
    private InMemoryDriverRepository() {
    }

    public static synchronized InMemoryDriverRepository getInstance() {
        if (instance == null) {
            instance = new InMemoryDriverRepository();
        }
        return instance;
    }

    @Override
    public void save(Driver driver) {
        drivers.add(driver);
    }

    @Override
    public List<Driver> findAvailable() {
        List<Driver> result = new ArrayList<>();
        for (Driver d : drivers) {
            if (d.isAvailable()) {
                result.add(d);
            }
        }
        return result;
    }
}