package com.gaurav.rydo.dto.driver;

import com.gaurav.rydo.entity.enums.VehicleType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DriverResponseDto {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private String licenseNumber;

    private String vehicleNumber;

    private VehicleType vehicleType;

    private Boolean isAvailable;

    private Double rating;

    private Double currentLatitude;

    private Double currentLongitude;
}