package taxi.repository;

import taxi.model.Driver;
import java.util.List;

public interface DriverRepository {
    void save(Driver driver);
    List<Driver> findAvailable();
}