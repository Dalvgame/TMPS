package taxi.domain.factory;

import taxi.domain.models.PremiumCar;
import taxi.domain.models.Vehicle;

public class PremiumDriverFactory extends DriverFactory {
    @Override
    protected Vehicle createVehicle(String model) {
        return new PremiumCar(model);
    }
}