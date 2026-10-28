package taxi.matching;

import taxi.domain.models.Driver;
import taxi.domain.models.Location;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class NearestDriverMatcher implements DriverMatcher {
    @Override
    public Optional<Driver> findDriver(Location pickup, List<Driver> candidates) {
        return candidates.stream()
                .min(Comparator.comparingDouble(d -> d.getLocation().distanceTo(pickup)));
    }
}