package taxi.matching;

import taxi.domain.models.Driver;
import taxi.domain.models.Location;
import java.util.List;
import java.util.Optional;

public interface DriverMatcher {
    Optional<Driver> findDriver(Location pickup, List<Driver> candidates);
}