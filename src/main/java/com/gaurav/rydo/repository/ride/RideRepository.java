package com.gaurav.rydo.repository.ride;

import com.gaurav.rydo.entity.Driver;
import com.gaurav.rydo.entity.Ride;
import com.gaurav.rydo.entity.User;
import com.gaurav.rydo.entity.enums.RideStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RideRepository extends JpaRepository<Ride, Long> {

    List<Ride> findByRider(User rider);

    List<Ride> findByDriver(Driver driver);

    Optional<Ride> findFirstByRiderAndStatusIn(
            User rider,
            List<RideStatus> statuses
    );

    Optional<Ride> findByIdAndDriverId(Long rideId, Long driverId);
}