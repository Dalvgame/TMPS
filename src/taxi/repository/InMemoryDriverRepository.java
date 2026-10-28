package taxi.repository;

import taxi.model.Driver;
import java.util.ArrayList;
import java.util.List;

public class InMemoryDriverRepository implements DriverRepository {
    private final List<Driver> drivers = new ArrayList<>();

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