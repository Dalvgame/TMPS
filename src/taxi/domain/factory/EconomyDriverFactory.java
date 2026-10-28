package taxi.domain.factory;

import taxi.domain.models.EconomyCar;
import taxi.domain.models.Vehicle;

public class EconomyDriverFactory extends DriverFactory {
    @Override
    protected Vehicle createVehicle(String model) {
        return new EconomyCar(model);
    }
}