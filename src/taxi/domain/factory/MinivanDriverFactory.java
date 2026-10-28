package taxi.domain.factory;

import taxi.domain.models.MinivanCar;
import taxi.domain.models.Vehicle;

public class MinivanDriverFactory extends DriverFactory {
    @Override
    protected Vehicle createVehicle(String model) {
        return new MinivanCar(model);
    }
}