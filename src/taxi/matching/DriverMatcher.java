package taxi.matching;

import taxi.model.Driver;
import taxi.model.Location;
import java.util.List;
import java.util.Optional;

public interface DriverMatcher {
    Optional<Driver> findDriver(Location pickup, List<Driver> candidates);
}