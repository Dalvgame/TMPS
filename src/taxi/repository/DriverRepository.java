package taxi.repository;

import taxi.domain.models.Driver;
import java.util.List;

public interface DriverRepository {
    void save(Driver driver);
    List<Driver> findAvailable();
}