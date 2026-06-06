package com.gaurav.rydo.repository.driver;

import com.gaurav.rydo.entity.Driver;
import com.gaurav.rydo.entity.Ride;
import com.gaurav.rydo.entity.User;
import com.gaurav.rydo.entity.enums.RideStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DriverRepository extends JpaRepository<Driver, Long> {

    Optional<Driver> findByUser(User user);

    boolean existsByLicenseNumber(String licenseNumber);

    boolean existsByVehicleNumber(String vehicleNumber);

    List<Driver> findByIsAvailableTrue();

    @Query(value = """
    SELECT *
    FROM drivers d
    WHERE d.is_available = true
    AND ST_DWithin(
        d.location::geography,
        ST_SetSRID(
            ST_MakePoint(:longitude, :latitude),
            4326
        )::geography,
        :radiusInMeters
    )
    """,
            nativeQuery = true)
    List<Driver> findNearbyDrivers(
            Double latitude,
            Double longitude,
            Double radiusInMeters
    );

    @Query(value = """
    SELECT *
    FROM drivers d
    WHERE d.is_available = true
    AND d.location IS NOT NULL
    ORDER BY ST_Distance(
        d.location::geography,
        ST_SetSRID(
            ST_MakePoint(:longitude, :latitude),
            4326
        )::geography
    )
    LIMIT 1
    """,
            nativeQuery = true)
    Optional<Driver> findNearestDriver(
            Double latitude,
            Double longitude
    );
}