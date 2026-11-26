package taxi.service;

import taxi.domain.models.*;

import java.util.Optional;

public interface RideBooking {
    Optional<Ride> requestRide(Passenger passenger, Location pickup, Location destination);

    Optional<Ride> requestRide(Passenger passenger, Location pickup, Location destination,
                               PaymentMethod paymentMethod, boolean childSeat);

    void completeRide(Ride ride);
}