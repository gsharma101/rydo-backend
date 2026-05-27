package com.gaurav.rydo.repository.driver;

import com.gaurav.rydo.entity.Driver;
import com.gaurav.rydo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DriverRepository extends JpaRepository<Driver, Long> {

    Optional<Driver> findByUser(User user);

    boolean existsByLicenseNumber(String licenseNumber);

    boolean existsByVehicleNumber(String vehicleNumber);

    List<Driver> findByIsAvailableTrue();
}