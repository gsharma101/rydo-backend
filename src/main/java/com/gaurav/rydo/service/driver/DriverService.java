package com.gaurav.rydo.service.driver;

import com.gaurav.rydo.dto.driver.DriverAvailabilityUpdateRequestDto;
import com.gaurav.rydo.dto.driver.DriverLocationUpdateRequestDto;
import com.gaurav.rydo.dto.driver.DriverRegistrationRequestDto;
import com.gaurav.rydo.dto.driver.DriverResponseDto;
import com.gaurav.rydo.entity.Driver;
import com.gaurav.rydo.entity.User;
import com.gaurav.rydo.repository.driver.DriverRepository;
import com.gaurav.rydo.repository.user.UserRepository;
import com.gaurav.rydo.util.DistanceUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DriverService {

    private final DriverRepository driverRepository;
    private final UserRepository userRepository;

    public DriverResponseDto registerDriver(
            DriverRegistrationRequestDto requestDto
    ) {

        // Get authenticated user email
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        // Fetch user from DB
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        // Check if already registered as driver
        if (driverRepository.findByUser(user).isPresent()) {
            throw new RuntimeException("Driver already registered");
        }

        // Validate license number
        if (driverRepository.existsByLicenseNumber(
                requestDto.getLicenseNumber()
        )) {
            throw new RuntimeException("License number already exists");
        }

        // Validate vehicle number
        if (driverRepository.existsByVehicleNumber(
                requestDto.getVehicleNumber()
        )) {
            throw new RuntimeException("Vehicle number already exists");
        }

        // Create driver entity
        Driver driver = Driver.builder()
                .user(user)
                .licenseNumber(requestDto.getLicenseNumber())
                .vehicleNumber(requestDto.getVehicleNumber())
                .vehicleType(requestDto.getVehicleType())
                .build();

        // Save driver
        Driver savedDriver = driverRepository.save(driver);

        // Return response DTO
        return DriverResponseDto.builder()
                .id(savedDriver.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .licenseNumber(savedDriver.getLicenseNumber())
                .vehicleNumber(savedDriver.getVehicleNumber())
                .vehicleType(savedDriver.getVehicleType())
                .isAvailable(savedDriver.getIsAvailable())
                .rating(savedDriver.getRating())
                .build();
    }

    public DriverResponseDto getCurrentDriver() {

        // Get authenticated user email
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        // Fetch user from DB
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        // Fetch driver by user
        Driver driver = driverRepository.findByUser(user)
                .orElseThrow(() ->
                        new RuntimeException("Driver not found"));

        // Return response DTO
        return DriverResponseDto.builder()
                .id(driver.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .licenseNumber(driver.getLicenseNumber())
                .vehicleNumber(driver.getVehicleNumber())
                .vehicleType(driver.getVehicleType())
                .isAvailable(driver.getIsAvailable())
                .rating(driver.getRating())
                .build();
    }

    public DriverResponseDto updateAvailability(
            DriverAvailabilityUpdateRequestDto requestDto
    ) {

        // Get authenticated user email
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        // Fetch user from DB
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        // Fetch driver by user
        Driver driver = driverRepository.findByUser(user)
                .orElseThrow(() ->
                        new RuntimeException("Driver not found"));

        // Update availability
        driver.setIsAvailable(requestDto.getIsAvailable());

        // Save updated driver
        Driver updatedDriver = driverRepository.save(driver);

        // Return response DTO
        return DriverResponseDto.builder()
                .id(updatedDriver.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .licenseNumber(updatedDriver.getLicenseNumber())
                .vehicleNumber(updatedDriver.getVehicleNumber())
                .vehicleType(updatedDriver.getVehicleType())
                .isAvailable(updatedDriver.getIsAvailable())
                .rating(updatedDriver.getRating())
                .build();
    }

    public DriverResponseDto updateLocation(
            DriverLocationUpdateRequestDto requestDto
    ) {

        // Get authenticated user email
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        // Fetch user from DB
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        // Fetch driver by user
        Driver driver = driverRepository.findByUser(user)
                .orElseThrow(() ->
                        new RuntimeException("Driver not found"));

        // Update location
        driver.setCurrentLatitude(requestDto.getLatitude());
        driver.setCurrentLongitude(requestDto.getLongitude());

        // Save updated driver
        Driver updatedDriver = driverRepository.save(driver);

        // Return response DTO
        return DriverResponseDto.builder()
                .id(updatedDriver.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .licenseNumber(updatedDriver.getLicenseNumber())
                .vehicleNumber(updatedDriver.getVehicleNumber())
                .vehicleType(updatedDriver.getVehicleType())
                .isAvailable(updatedDriver.getIsAvailable())
                .rating(updatedDriver.getRating())
                .currentLatitude(updatedDriver.getCurrentLatitude())
                .currentLongitude(updatedDriver.getCurrentLongitude())
                .build();
    }

    public List<DriverResponseDto> findNearbyDrivers(
            Double latitude,
            Double longitude,
            Double radius
    ) {

        // Fetch available drivers
        List<Driver> availableDrivers =
                driverRepository.findByIsAvailableTrue();

        // Filter nearby drivers
        return availableDrivers.stream()

                .filter(driver ->
                        driver.getCurrentLatitude() != null
                                && driver.getCurrentLongitude() != null
                )

                .filter(driver -> {

                    double distance = DistanceUtil.calculateDistance(
                            latitude,
                            longitude,
                            driver.getCurrentLatitude(),
                            driver.getCurrentLongitude()
                    );

                    return distance <= radius;
                })

                .map(driver -> DriverResponseDto.builder()
                        .id(driver.getId())
                        .firstName(driver.getUser().getFirstName())
                        .lastName(driver.getUser().getLastName())
                        .email(driver.getUser().getEmail())
                        .phoneNumber(driver.getUser().getPhoneNumber())
                        .licenseNumber(driver.getLicenseNumber())
                        .vehicleNumber(driver.getVehicleNumber())
                        .vehicleType(driver.getVehicleType())
                        .isAvailable(driver.getIsAvailable())
                        .rating(driver.getRating())
                        .currentLatitude(driver.getCurrentLatitude())
                        .currentLongitude(driver.getCurrentLongitude())
                        .build()
                )

                .toList();
    }
}