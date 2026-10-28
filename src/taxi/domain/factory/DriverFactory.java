package taxi.domain.factory;

import taxi.domain.models.Driver;
import taxi.domain.models.Location;
import taxi.domain.models.Vehicle;

public abstract class DriverFactory {

    // The factory method: subclasses decide which Vehicle is created
    protected abstract Vehicle createVehicle(String model);

    public Driver createDriver(int id, String name, String carModel, Location location) {
        return new Driver(id, name, createVehicle(carModel), location);
    }
}