package com.gaurav.rydo.service.ride;

import com.gaurav.rydo.dto.ride.RideHistoryResponseDto;
import com.gaurav.rydo.dto.ride.RideRequestDto;
import com.gaurav.rydo.dto.ride.RideResponseDto;
import com.gaurav.rydo.entity.Driver;
import com.gaurav.rydo.entity.Ride;
import com.gaurav.rydo.entity.enums.RideStatus;
import com.gaurav.rydo.entity.User;
import com.gaurav.rydo.repository.driver.DriverRepository;
import com.gaurav.rydo.repository.ride.RideRepository;
import com.gaurav.rydo.repository.user.UserRepository;
import com.gaurav.rydo.util.DistanceUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RideService {

    private final RideRepository rideRepository;
    private final DriverRepository driverRepository;
    private final UserRepository userRepository;

    public RideResponseDto requestRide(
            RideRequestDto requestDto
    ) {

        // Get authenticated rider
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User rider = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        // Fetch available drivers
        List<Driver> availableDrivers =
                driverRepository.findByIsAvailableTrue();

        // Find nearest driver
        Driver nearestDriver = availableDrivers.stream()

                .filter(driver ->
                        driver.getCurrentLatitude() != null
                                && driver.getCurrentLongitude() != null
                )

                .min(Comparator.comparingDouble(driver ->

                        DistanceUtil.calculateDistance(
                                requestDto.getPickupLatitude(),
                                requestDto.getPickupLongitude(),
                                driver.getCurrentLatitude(),
                                driver.getCurrentLongitude()
                        )
                ))

                .orElseThrow(() ->
                        new RuntimeException("No nearby drivers available"));

        // Mark driver unavailable
        nearestDriver.setIsAvailable(false);

        driverRepository.save(nearestDriver);

        // Simple fare calculation
        double fare = DistanceUtil.calculateDistance(
                requestDto.getPickupLatitude(),
                requestDto.getPickupLongitude(),
                requestDto.getDropLatitude(),
                requestDto.getDropLongitude()
        ) * 15;

        // Create ride
        Ride ride = Ride.builder()
                .rider(rider)
                .driver(nearestDriver)
                .pickupLatitude(requestDto.getPickupLatitude())
                .pickupLongitude(requestDto.getPickupLongitude())
                .dropLatitude(requestDto.getDropLatitude())
                .dropLongitude(requestDto.getDropLongitude())
                .fare(fare)
                .status(RideStatus.REQUESTED)
                .build();

        Ride savedRide = rideRepository.save(ride);

        // Return response
        return RideResponseDto.builder()
                .rideId(savedRide.getId())
                .riderName(
                        rider.getFirstName() + " " + rider.getLastName()
                )
                .driverName(
                        nearestDriver.getUser().getFirstName()
                                + " "
                                + nearestDriver.getUser().getLastName()
                )
                .vehicleNumber(nearestDriver.getVehicleNumber())
                .pickupLatitude(savedRide.getPickupLatitude())
                .pickupLongitude(savedRide.getPickupLongitude())
                .dropLatitude(savedRide.getDropLatitude())
                .dropLongitude(savedRide.getDropLongitude())
                .fare(savedRide.getFare())
                .status(savedRide.getStatus())
                .requestedAt(savedRide.getRequestedAt())
                .build();
    }

    public RideResponseDto acceptRide(Long rideId) {

        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() ->
                        new RuntimeException("Ride not found"));

        ride.setStatus(RideStatus.ACCEPTED);

        Ride updatedRide = rideRepository.save(ride);

        return mapToResponse(updatedRide);
    }

    public RideResponseDto startRide(Long rideId) {

        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() ->
                        new RuntimeException("Ride not found"));

        ride.setStatus(RideStatus.STARTED);

        ride.setStartedAt(LocalDateTime.now());

        Ride updatedRide = rideRepository.save(ride);

        return mapToResponse(updatedRide);
    }

    public RideResponseDto completeRide(Long rideId) {

        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() ->
                        new RuntimeException("Ride not found"));

        ride.setStatus(RideStatus.COMPLETED);

        ride.setCompletedAt(LocalDateTime.now());

        // Make driver available again
        Driver driver = ride.getDriver();

        driver.setIsAvailable(true);

        driverRepository.save(driver);

        Ride updatedRide = rideRepository.save(ride);

        return mapToResponse(updatedRide);
    }

    private RideResponseDto mapToResponse(Ride ride) {

        return RideResponseDto.builder()
                .rideId(ride.getId())
                .riderName(
                        ride.getRider().getFirstName()
                                + " "
                                + ride.getRider().getLastName()
                )
                .driverName(
                        ride.getDriver().getUser().getFirstName()
                                + " "
                                + ride.getDriver().getUser().getLastName()
                )
                .vehicleNumber(
                        ride.getDriver().getVehicleNumber()
                )
                .pickupLatitude(ride.getPickupLatitude())
                .pickupLongitude(ride.getPickupLongitude())
                .dropLatitude(ride.getDropLatitude())
                .dropLongitude(ride.getDropLongitude())
                .fare(ride.getFare())
                .status(ride.getStatus())
                .requestedAt(ride.getRequestedAt())
                .build();
    }

    public List<RideHistoryResponseDto> getMyRides() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User rider = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        List<Ride> rides = rideRepository.findByRider(rider);

        return rides.stream()
                .map(this::mapToHistoryResponse)
                .toList();
    }

    public List<RideHistoryResponseDto> getDriverRides() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Driver driver = driverRepository.findByUser(user)
                .orElseThrow(() ->
                        new RuntimeException("Driver not found"));

        List<Ride> rides = rideRepository.findByDriver(driver);

        return rides.stream()
                .map(this::mapToHistoryResponse)
                .toList();
    }

    private RideHistoryResponseDto mapToHistoryResponse(
            Ride ride
    ) {

        return RideHistoryResponseDto.builder()
                .rideId(ride.getId())
                .riderName(
                        ride.getRider().getFirstName()
                                + " "
                                + ride.getRider().getLastName()
                )
                .driverName(
                        ride.getDriver().getUser().getFirstName()
                                + " "
                                + ride.getDriver().getUser().getLastName()
                )
                .fare(ride.getFare())
                .status(ride.getStatus())
                .requestedAt(ride.getRequestedAt())
                .build();
    }

    public RideResponseDto getActiveRide() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User rider = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        List<RideStatus> activeStatuses = List.of(
                RideStatus.REQUESTED,
                RideStatus.ACCEPTED,
                RideStatus.STARTED
        );

        Ride ride = rideRepository
                .findFirstByRiderAndStatusIn(
                        rider,
                        activeStatuses
                )
                .orElseThrow(() ->
                        new RuntimeException("No active ride found"));

        return mapToResponse(ride);
    }
}