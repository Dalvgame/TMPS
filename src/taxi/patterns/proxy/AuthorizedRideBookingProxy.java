package taxi.patterns.proxy;

import taxi.domain.models.*;
import taxi.service.RideBooking;

import java.util.Optional;
import java.util.Set;

public class AuthorizedRideBookingProxy implements RideBooking {
    private final RideBooking real;
    private final Set<Integer> blockedPassengerIds;

    public AuthorizedRideBookingProxy(RideBooking real, Set<Integer> blockedPassengerIds) {
        this.real = real;
        this.blockedPassengerIds = blockedPassengerIds;
    }

    @Override
    public Optional<Ride> requestRide(Passenger passenger, Location pickup, Location destination) {
        if (isBlocked(passenger)) {
            return deny(passenger);
        }
        return real.requestRide(passenger, pickup, destination);
    }

    @Override
    public Optional<Ride> requestRide(Passenger passenger, Location pickup, Location destination,
                                      PaymentMethod paymentMethod, boolean childSeat) {
        if (isBlocked(passenger)) {
            return deny(passenger);
        }
        return real.requestRide(passenger, pickup, destination, paymentMethod, childSeat);
    }

    @Override
    public void completeRide(Ride ride) {
        real.completeRide(ride);   // finishing a ride is always allowed
    }

    private boolean isBlocked(Passenger passenger) {
        return blockedPassengerIds.contains(passenger.getId());
    }

    private Optional<Ride> deny(Passenger passenger) {
        System.out.println("[ACCESS DENIED] Passenger " + passenger.getName() + " is blocked.");
        return Optional.empty();
    }
}